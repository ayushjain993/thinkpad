@echo off

%~d0
cd %~dp0

cd ..

mkdir deploy

echo.
echo [��Ϣ] ���Web���̣�����war/jar���ļ���
echo.
call mvn clean package -Dmaven.test.skip=true

xcopy uhha-admin\target\uhha-admin-*.jar deploy /s/y
xcopy uhha-store\target\uhha-store-*.jar deploy /s/y
xcopy uhha-web\target\uhha-web-*.jar deploy /s/y

echo.
echo [��Ϣ] ���ǰ�˹���
echo.

cd uhha-ui
call npm run build:prod
call xcopy dist ..\deploy\admin  /y /e /i /q
cd ..

cd uhha-store-ui
call npm run build:prod
call xcopy dist ..\deploy\store /y /e /i /q
cd ..

cd uhha-uniApp
call npm run build:h5
call xcopy dist\build\h5 ..\deploy\h5 /y /e /i /q
cd ..

pause