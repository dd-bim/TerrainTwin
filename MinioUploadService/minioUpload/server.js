import { Client } from 'minio';
import path from 'path';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
import dotenv from 'dotenv';
dotenv.config({ path: '../.env'});

// connect to MinIO
const client = new Client({
    endPoint: process.env.SERVER_URL,
    port: 9000,
    useSSL: false,
    accessKey: process.env.ACCESS_KEY,
    secretKey: process.env.SECRET_KEY,
});

// express is a small HTTP server wrapper, but this works with any HTTP server
import express from 'express';
const server = express();
server.use(express.static(__dirname));

// get presignedURL with bucket and name, if bucket not exists, create bucket
server.get('/presignedUrl', (req, res) => {
    client.makeBucket(req.query.bucket, 'eu-central-1', function (err) {
        if (err) {
            return console.log(err);
        }
        else return console.log('Bucket created successfully.');
    });

    var policy = client.newPostPolicy()
    policy.setKey(req.query.name)
    policy.setBucket(req.query.bucket)

    setTimeout(() => {
        client.presignedPostPolicy(policy, function (e, urlStr, formData) {
            if (e) throw e
            res.end(JSON.stringify({ urlStr, formData }))
        })
    }, 100);
})

server.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
})

// delete bucket and his objects
server.get(`/deleteBucket`, (req, res) => {
    var buck = req.query.bucket;
    var objectsList = [];
   
    // bug fixed on ...\node_modules\minio\dist\main\xml-parsers.js:387
    var objectsStream = client.listObjects(buck);

    objectsStream.on('error', function (e) {
        console.log(e);
        return res.end(`${buck} existiert nicht.`);
    })

    objectsStream.on('data', function (obj) {
        objectsList.push(obj.name);
    })

    objectsStream.on('end', function () {

        client.removeObjects(buck, objectsList, function (e) {
            if (e) {
                return res.end('Dateien konnten nicht gelöscht werden.', e)
            }
            console.log('Removed the objects successfully')
        })
        setTimeout(() => {
            client.removeBucket(buck, function (err) {
                if (err) return res.end(`${buck} konnte nicht gelöscht werden.`)
                let str = "";
                objectsList. forEach(obj =>{
                    str += obj+"<br>";
                })
                res.end(`${buck} und folgenden Inhalt gelöscht:<br>${str}`);
            })
        }, 100);
    })
})

server.listen(process.env.WEB_PORT)