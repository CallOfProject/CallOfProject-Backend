$BASE_DIR = Get-Location

$pid_file = "$BASE_DIR\service_pids.txt"

Get-Content $pid_file | ForEach-Object {
    $pid = $_
    Stop-Process -Id $pid -Force
    Write-Host "PID $pid killed."
}

Clear-Content $pid_file
Write-Host "PID file cleared."
