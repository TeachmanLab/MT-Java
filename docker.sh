cd ./core
echo "bower install" && bower install && echo "bower install done!"
cd ../
pushd ../MT-Angular/
ng build --prod --build-optimizer --output-hashing none --base-href=/calm/angular/
popd
cp -rv ../MT-Angular/dist/training-prototype ./r01/src/main/resources/static/angular
cp r01/src/main/resources/docker-application-export.properties r01/src/main/resources/application.properties
./gradlew r01:war
cp r01/build/libs/r01-0.0.1.war calm.war

#cp r01/src/main/resources/docker-application-import.properties r01/src/main/resources/application.properties
#./gradlew r01:war
#cp r01/build/libs/r01-0.0.1.war calm_import.war

cp r01/src/main/resources/application.properties.example r01/src/main/resources/application.properties

docker build . -t mt-calm 
