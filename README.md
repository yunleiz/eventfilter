# Data sorting and filtering

## Requirements
* maven
* java 8+

This is a maven project, all the dependencies are specified in pom.xml file.

## Run the project
```Bash
maven package
mvn exec:java -Dexec.mainClass="com.yunlei.Main" -Dexec.args="<dest filename> <log filenames>"
// or just run
mvn exec:java -Dexec.mainClass="com.yunlei.Main"
```
If the dest filename and log filenames are not provided, the program will use example files under `./data` and create an
`out.csv` in current folder.

## Scalability

This code is designed to be easy to extend if new file reader needed or new file writer need.

### New reader
Extend the LogEntriesReader interface, and config the mappping from `file format` to `reader class` in ./config/config.xml.
The program will be able to pick up the new reader.

### New writer
Extend the LogWriter interface and register the new writer class in ./config/config.xml the program will pick up the new writer.
