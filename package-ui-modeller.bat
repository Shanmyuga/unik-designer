@echo off
echo Running maven clean...
call mvn clean

echo(
echo(
echo Running maven install...
call mvn install -Dmaven.test.skip=true

echo(
echo(
echo Creating dist folder...
mkdir dist

echo(
echo(
echo Copying UNIK UI Modeller Binary...
copy target\unikdesigner-0.0.1-SNAPSHOT.jar dist

echo(
echo(
echo Copying UNIK UI DB...
mkdir dist\src\main\resources\db
copy src\main\resources\db\unikDb.mv.db dist\src\main\resources\db

echo(
echo(
echo Starting UNIK UI Modeller...
cd dist
java -jar unikdesigner-0.0.1-SNAPSHOT.jar