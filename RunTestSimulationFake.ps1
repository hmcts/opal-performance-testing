# Get the directory where this script is located
$targetPath = $PSScriptRoot

# Navigate to the target directory
Set-Location -Path $targetPath

# Execute the Gradle command
& ".\gradlew.bat" runMAC_01bSimulation `
    "-Dperformance.inputters=1" `
    "-Dperformance.checkers=1" `
    "-Dperformance.majorCreditorUsers=1" `
    "-Dperformance.minorCreditorUsers=1" `
    "-Dperformance.pGUsers=1" `
    "-Dperformance.enforcementUsers=1" `
    "-Dperformance.searchViewUsers=1" `
    "-Dperformance.existing=0" `
    "-Dperformance.rampup.minutes=2" `
    "-Dperformance.duration.minutes=60"

#Use this to kill tests
#TASKKILL /F /IM java.exe