@echo off

set PWD=%CD%

echo Restoring the mongo db...
cd src\main\resources\db\mongo\unik_db
mongorestore --host localhost --port 27017 -d test_unik .

cd %PWD%
echo(
echo(
echo Imported successfully...
