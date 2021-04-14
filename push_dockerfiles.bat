docker tag configserver schi11er/tt_configserver
docker push schi11er/tt_configserver
docker tag eurekaserver schi11er/tt_eurekaserver
docker push schi11er/tt_eurekaserver
docker tag gateway-service schi11er/tt_gateway-service
docker push schi11er/tt_gateway-service
docker tag csv2rdf-converter-service schi11er/tt_csv2rdf-converter-service
docker push schi11er/tt_csv2rdf-converter-service
docker tag postgres-import-service schi11er/tt_postgres-import-service
docker push schi11er/tt_postgres-import-service
docker tag graphdb-import-service schi11er/tt_graphdb-import-service
docker push schi11er/tt_graphdb-import-service
docker tag minio-upload-service schi11er/tt_minio-upload-service
docker push schi11er/tt_minio-upload-service