@echo off
SET "source=C:\Users\T-GAMER\Desktop\futebolistico\target\futebolistico-1.0.0-rc.1.jar"
SET "destination=C:\Users\T-GAMER\Desktop\server-example\plugins\futebolistico-1.0.0-rc.1.jar"
SET "folder1=C:\Users\T-GAMER\Desktop\server-example\plugins\futebolistico"
SET "folder2=C:\Users\T-GAMER\Desktop\server-example\plugins\bStats"

IF EXIST "%destination%" (
    DEL "%destination%"
)

IF EXIST "%folder1%" (
    RMDIR /S /Q "%folder1%"
)

IF EXIST "%folder2%" (
    RMDIR /S /Q "%folder2%"
)


COPY "%source%" "%destination%"


CALL C:\Users\T-GAMER\Desktop\server-example\run.bat
PAUSE