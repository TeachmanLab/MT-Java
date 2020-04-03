cp r01/src/main/resources/uva_export_application.properties r01/src/main/resources/application.properties
./gradlew r01:war
cp r01/build/libs/r01-0.0.1.war calm.war

cp r01/src/main/resources/uva_import_application.properties r01/src/main/resources/application.properties
./gradlew r01:war
cp r01/build/libs/r01-0.0.1.war calm_import.war

rm r01/src/main/resources/application.properties
