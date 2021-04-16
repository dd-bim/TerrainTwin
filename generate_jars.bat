cd ConfigServer 
del "target\ConfigServer-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\ConfigServer-1.0.jar" "..\files"
@REM cd ..\EurekaServer
@REM del "target\EurekaServer-1.0.jar" 
@REM call mvn package -Dmaven.test.skip=true
@REM copy /Y "target\EurekaServer-1.0.jar" "..\files"
cd ..\Csv2RdfConverterService 
del "target\Csv2RdfConverterService-1.0.jar"
call mvn package -Dmaven.test.skip=true
copy /Y "target\Csv2RdfConverterService-1.0.jar" "..\files"
@REM cd ..\MinIOUploadService 
@REM del "target\MinIOUploadService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM copy /Y "target\MinIOUploadService-1.0.jar" "..\files"
@REM cd ..\PostgresImportService 
@REM del "target\PostgresImportService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM copy /Y "target\PostgresImportService-1.0.jar" "..\files"
@REM cd ..\GraphDBImportService
@REM del "target\GraphDBImportService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM copy /Y "target\GraphDBImportService-1.0.jar" "..\files"
@REM cd ..\GatewayService
@REM del "target\GatewayService-1.0.jar"
@REM call mvn package -Dmaven.test.skip=true
@REM copy /Y "target\GatewayService-1.0.jar" "..\files"
cd ..
cmd /k
