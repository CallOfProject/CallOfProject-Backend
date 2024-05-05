#!/bin/bash
BASE_DIR=$(pwd)
TARGET_DIR=$1

pid_file="$BASE_DIR/service_pids.txt"
rm -f "$pid_file"

touch "$pid_file"

osascript -e "tell application \"Terminal\" to activate"

cd $TARGET_DIR
nohup java -jar EurekaServer-1.0.0.jar &
eureka_pid=$!
echo "Eureka Server starting... (PID: $eureka_pid)"

declare -a services=("apigateway-1.0.0.jar" "Authentication-Service-1.0.0.jar" "CommunityService-1.0.0.jar" "EmailService-1.0.0.jar" "EnvironmentService-1.0.0.jar" "FilterAndSearchService-1.0.0.jar" "InterviewService-1.0.0.jar" "NotificationService-1.0.0.jar" "SchedulerService-1.0.0.jar" "task-service-1.0.0.jar" "TicketService-1.0.0.jar")

declare -a service_pids

service_pids+=("$eureka_pid")

for service in "${services[@]}"
do
    osascript -e 'tell application "Terminal"' -e 'activate' -e 'tell application "System Events" to keystroke "t" using command down' -e 'delay 0.5' -e "do script \"cd $TARGET_DIR; java -jar $service\" in front window" -e 'end tell'      
    pid=$(ps -ef | grep "java -jar $service" | grep -v grep | awk '{print $2}')
    service_pids+=("$pid")
done

for pid in "${service_pids[@]}"
do
echo "$pid" >> "$pid_file"
done

echo "Servis PID'leri dosyaya yazıldı: $pid_file"