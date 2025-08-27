# instalando biblioteca comprehensive no maven local para importar no projeto

```
mvn install:install-file -Dfile='D:\ambiente_desenvolvedor_roboflex\zontec\workspaces\comprehensive(20250301).jar' -DgroupId='com.rfid.reader' -DartifactId=comprehensive -Dversion='1.0.0' -Dpackaging=jar
```

# Empacotando projeto .exe a partir do projeto java
```
jpackage --input D:\ambiente_desenvolvedor_roboflex\zontec\workspaces\zontec-rfid-reader\zontec-rfid-reader\target --name RfidReaderApplication.java --main-jar .\zontec-rfid-reader-0.0.1-SNAPSHOT.jar --main-class br.com.zontec.rfid.rfid_reader.RfidReaderApplication --type exe --dest D:\ambiente_desenvolvedor_roboflex\zontec\workspaces\zontec-rfid-reader\zontec-rfid-reader --vendor "ZontecRFIDReader" --win-menu --win-shortcut
```


jpackage ^
  --type exe ^
  --name "ZontecRfidReader" ^
  --app-version 1.0.0 ^
  --input "D:\ambiente_desenvolvedor_roboflex\zontec\workspaces\zontec-rfid-reader\zontec-rfid-reader\target" ^
  --main-jar "zontec-rfid-reader-0.0.1-SNAPSHOT.jar" ^
  --main-class "br.com.zontec.rfid.rfid_reader.RfidReaderApplication" ^
  --dest D:\ambiente_desenvolvedor_roboflex\zontec\workspaces\zontec-rfid-reader\zontec-rfid-reader ^
  --java-options "--enable-preview" ^
  --win-console ^
  --win-shortcut ^
  --win-dir-chooser ^
  --description "Serviço Zontec RFID Reader - roda na porta 8099" ^
  --vendor "ZontecRFIDReader" ^
  --runtime-image "D:\java\jdk-21.0.7+6\jmods"
