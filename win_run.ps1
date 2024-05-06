$BASE_DIR = Get-Location
$TARGET_DIR = $args[0]

$pid_file = "$BASE_DIR\service_pids.txt"
Remove-Item -Path $pid_file -ErrorAction Ignore
New-Item -Path $pid_file -ItemType File

function Start-NewTerminal {
    param(
        [string]$directory,
        [string]$command
    )
    wt -w 0 nt powershell -NoExit -Command "cd $directory; $command"
}

Set-Location -Path $TARGET_DIR

Start-Process -FilePath "java" -ArgumentList "-jar EurekaServer-1.0.0.jar" -NoNewWindow -PassThru
Start-Sleep -Seconds 2
$eureka_pid = (Get-Process -Name "java" | Where-Object { $_.Path -like "*EurekaServer-1.0.0.jar" }).Id
Write-Host "Eureka Server starting... (PID: $eureka_pid)"

$services = "apigateway-1.0.0.jar", "Authentication-Service-1.0.0.jar", "EnvironmentService-1.0.0.jar", "ProjectService-1.0.0.jar", "CommunityService-1.0.0.jar", "EmailService-1.0.0.jar", "FilterAndSearchService-1.0.0.jar", "InterviewService-1.0.0.jar", "NotificationService-1.0.0.jar", "SchedulerService-1.0.0.jar", "task-service-1.0.0.jar", "TicketService-1.0.0.jar"

$service_pids = @($eureka_pid)

foreach ($service in $services) {
    Start-NewTerminal -directory $TARGET_DIR -command "java -jar $service"
    Start-Sleep -Seconds 5
    $pid = (Get-Process -Name "java" | Where-Object { $_.Path -like "*$service" }).Id
    $service_pids += $pid
}

$service_pids | Out-File -FilePath $pid_file -Append

Write-Host "Service PIDs written to file: $pid_file"
