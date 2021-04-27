@REM cd ConfigServer 
@REM del "target\ConfigServer-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM cd ..\EurekaServer
@REM del "target\EurekaServer-1.0.jar" 
@REM call mvn package -Dmaven.test.skip=true
@REM cd ..\
cd MinIOUploadService 
del "target\MinIOUploadService-1.0.jar"
call mvn package -Dmaven.test.skip=true
@REM cd ..\Csv2RdfConverterService 
@REM del "target\Csv2RdfConverterService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM cd ..\PostgresImportService 
@REM del "target\PostgresImportService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM cd ..\GraphDBImportService
@REM del "target\GraphDBImportService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM cd ..\GatewayService
@REM del "target\GatewayService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
cd ..
docker rmi schi11er/tt_configserver schi11er/tt_eurekaserver schi11er/tt_gateway-service schi11er/tt_postgres-import-service schi11er/tt_graphdb-import-service schi11er/tt_minio-upload-service schi11er/tt_csv2rdf-converter-service
cmd /k
