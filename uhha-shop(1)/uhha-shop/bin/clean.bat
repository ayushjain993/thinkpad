@echo off
echo.
echo [��Ϣ] ������target����·����
echo.

%~d0
cd %~dp0

cd ..
call mvn clean
call rd uhha-ui\dist /s /q
call rd uhha-store-ui\dist /s /q
call rd uhha-uniApp\dist\build\h5 /s /q

call rd deploy /s /q

pause