cd ConfigServer 
del "target\ConfigServer-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\ConfigServer-1.0.jar" "..\files"
cd ..\EurekaServer
del "target\EurekaServer-1.0.jar" 
call mvn package -Dmaven.test.skip=true
copy /Y "target\EurekaServer-1.0.jar" "..\files"
cd ..\Csv2RdfConverterService 
del "target\Csv2RdfConverterService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\Csv2RdfConverterService-1.0.jar" "..\files"
cd ..\MinIOUploadService 
del "target\MinIOUploadService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\MinIOUploadService-1.0.jar" "..\files"
cd ..\PostgresImportService 
del "target\PostgresImportService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\PostgresImportService-1.0.jar" "..\files"
cd ..\GraphDBImportService
del "target\GraphDBImportService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\GraphDBImportService-1.0.jar" "..\files"
cd ..\GatewayService
del "target\GatewayService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\GatewayService-1.0.jar" "..\files"
cd ..
cmd /k
