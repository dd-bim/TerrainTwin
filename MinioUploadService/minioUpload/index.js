export function upload() {

    // Get selected files from the input element.  
    document.querySelector('#statusUpdate').innerHTML = "";
    var files = document.querySelector("#selector").files;
    var bucket = document.querySelector("#bucket").value;
    if (bucket === "") document.querySelector('#statusUpdate').innerHTML += "Kein Projektordner gewählt.";
    var checkbox = document.querySelector("#metachecker").checked;

    var date = new Date().toISOString();
    var timestamp = date.replaceAll(':', '-').split('.')[0];
    var file = new File([files[0]], timestamp + '_' + files[0].name, { type: files[0].type });

    // create upload request and upload file
    retrieveNewURL(file, bucket, (file1, url) => {
        // Upload the file to the server.
        uploadFile(file1, bucket, url);
    });

    // do, if metadata file should be uploaded
    if (checkbox) {
        var elements = document.querySelector('#metadata').elements;

        // extract metadata
        var obj = {};
        var dgm = {};
        obj["id"] = uuid();
        obj["name"] = file.name;
        obj["created"] = date;
        obj["location"] = "https://terrain.dd-bim.org" + "/minio/" + bucket + "/" + file.name; //+ url.split(":")[3] 
        const ext = file.name.split(".")[1];
        switch (ext) {
            case "ifc": obj["mimeType"] = "application/x-step";
                break;
            case "dwg": obj["mimeType"] = "application/acad";
                break;
            case "dxf": obj["mimeType"] = "application/dxf";
                break;
            case "gml": obj["mimeType"] = "application/gml+xml";
                break;
            case "ttl": obj["mimeType"] = "text/turtle";
                break;
            case "owl": obj["mimeType"] = "application/rdf+xml";
                break;
            default: obj["mimeType"] = "";
        }

        for (var i = 0; i < elements.length; i++) {
            var item = elements.item(i);
            if (i<14) {
                obj[item.name] = item.value;
            } else {
                dgm[item.name] = item.value;
            }
        }
        var metas = {};
        metas["DIN SPEC 91391-2"] = obj;
        if (obj["type"] == "DGM")  metas["DGM"] = dgm;

        // prepare metadata file vor upload
        var metaFilename = file.name.split('.')[0] + '_' + 'metadata' + '.json';
        var blob = new Blob([JSON.stringify(metas)], { type: "application/json" });
        var metadata = new File([blob], metaFilename);

        // create upload request and upload metadata file
        retrieveNewURL(metadata, bucket, (metadata1, url) => {
            // Upload the file to the server.
            uploadFile(metadata1, bucket, url);
        });

        // clear form fields
        document.getElementById("metadata").reset(); 
    }
}

// `retrieveNewURL` accepts the name of the current file and invokes the `/presignedUrl` endpoint to
// generate a pre-signed URL for use in uploading that file: 
function retrieveNewURL(file, bucket, cb) {
    fetch(`/presignedUrl?name=${file.name}&bucket=${bucket}`).then((response) => {
        response.text().then((url) => {
            cb(file, url);
        });
    }).catch((e) => {
        console.error(e);
    });
}

// Use XMLHttpRequest to upload the files.
function uploadFile(file, bucket, obj) {

    var xhr = new XMLHttpRequest()
    var objSon = JSON.parse(obj);
    xhr.open('POST', objSon.urlStr.postURL, true);

    var fd = new FormData();
    Object.entries(objSon.urlStr.formData).forEach(([key, value]) => {
        fd.append(key, value);
    });

    console.log(fd.toString());
    fd.append('file', file);
    xhr.send(fd);

    xhr.onload = () => {
        if (xhr.status == 204) {
            document.querySelector('#statusUpdate').innerHTML += `${file.name} in den Ordner ${bucket} hochgeladen.<br>`;
        }
    }
}

// on click delete bucket and his objects
export function deleteB() {
    document.querySelector('#statusUpdate').innerHTML = "";
    var bucket = document.querySelector('#bucket').value;
    fetch(`/deleteBucket?bucket=${bucket}`).then((response) => {
        response.text().then(res => {
            document.querySelector('#statusUpdate').innerHTML += `${res}`;
        })
    }).catch((e) => {
        console.error(e);
    });
}

function uuid() {
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx'.replace(/[x]/g, function (c) {
        var r = (dt + Math.random() * 16) % 16 | 0;
        dt = Math.floor(dt / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid;
}