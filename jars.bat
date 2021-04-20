cd ConfigServer 
del "target\ConfigServer-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..\EurekaServer
del "target\EurekaServer-1.0.jar" 
call mvn package -Dmaven.test.skip=true
cd ..\Csv2RdfConverterService 
del "target\Csv2RdfConverterService-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..\MinIOUploadService 
del "target\MinIOUploadService-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..\PostgresImportService 
del "target\PostgresImportService-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..\GraphDBImportService
del "target\GraphDBImportService-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..\GatewayService
del "target\GatewayService-1.0.jar"
call mvn package -Dmaven.test.skip=true
cd ..
docker rmi configserver eurekaserver gateway-service postgres-import-service graphdb-import-service minio-upload-service csv2rdf-converter-service
docker rmi schi11er/tt_configserver schi11er/tt_eurekaserver schi11er/tt_gateway-service schi11er/tt_postgres-import-service schi11er/tt_graphdb-import-service schi11er/tt_minio-upload-service schi11er/tt_csv2rdf-converter-service
cmd /k
