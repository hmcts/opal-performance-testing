# Get the directory where this script is located
$targetPath = $PSScriptRoot

# Navigate to the target directory
Set-Location -Path $targetPath

# Execute the Gradle command
& ".\gradlew.bat" runConvertToDefendantAccountSimulation   `

#Use this to kill tests
#TASKKILL /F /IM java.exe