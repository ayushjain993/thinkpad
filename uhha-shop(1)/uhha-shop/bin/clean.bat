@echo off
echo.
echo [信息] 清理工程target生成路径。
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