# Get the directory where this script is located
$targetPath = $PSScriptRoot

# Navigate to the target directory
Set-Location -Path $targetPath

# Execute the Gradle command
& ".\gradlew.bat" runMAC_01aSimulation `
    "-Dperformance.inputters=1" `
    "-Dperformance.checkers=1" `
    "-Dperformance.existing=0" `
    "-Dperformance.rampup.minutes=2" `
    "-Dperformance.duration.minutes=20"

#Use this to kill tests
#TASKKILL /F /IM java.exe 