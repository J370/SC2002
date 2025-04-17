@echo off
setlocal enabledelayedexpansion

for /R %%f in (*.java) do (
    set filename=%%~nxf
    if not "!filename!"=="OfficerController.java" (
        if not "!filename!"=="ManagerController.java" (
            javac "%%f"
        )
    )
)
endlocal