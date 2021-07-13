cp kaiser/src/main/resources/sartography_application.properties kaiser/src/main/resources/application.properties
./gradlew kaiser:war
cp kaiser/build/libs/kaiser-0.0.1.war kaiser.war