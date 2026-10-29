# Get the directory where this script is located
$targetPath = $PSScriptRoot

# Navigate to the target directory
Set-Location -Path $targetPath

# Execute the Gradle command
& ".\gradlew.bat" runAmendingEnforcementsToAccountsSimulation2 `
    "-Dperformance.inputters=0" `
    "-Dperformance.checkers=0" `
    "-Dperformance.majorCreditorUsers=0" `
    "-Dperformance.minorCreditorUsers=0" `
    "-Dperformance.pGUsers=0" `
    "-Dperformance.enforcementUsers=20" `
    "-Dperformance.searchViewUsers=0" `
    "-Dperformance.existing=0" `
    "-Dperformance.rampup.minutes=2" `
    "-Dperformance.duration.minutes=60"

#Use this to kill tests
#TASKKILL /F /IM java.exe