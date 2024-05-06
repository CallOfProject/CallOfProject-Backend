#!/bin/bash

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RESET='\033[0m'

cd Services/EnvironmentService
mvn package -DskipTests
cd target

java -jar EnvironmentService-1.0.0.jar &
ENV_SERVICE_PID=$!

echo -e "${BLUE} Environment service running on backgorund... $RESET"
sleep 3

# Authentication Service Tests
clear
echo -e "${BLUE} Starting Authentication Service Tests...${RESET}"
sleep 3
cd ../../AuthenticationService
mvn test
cd ..
kill -9 $ENV_SERVICE_PID
sleep 2
clear
echo -e "${GREEN} Completed Authentication Service Tests... $RESET"
sleep 3

#Project Service Tests
clear
echo -e "${BLUE} Starting Project Service Tests... $RESET"
sleep 5
cd ProjectService
mvn test
cd ..
sleep 2
clear
echo -e "${GREEN} Completed Project Service Tests... $RESET"
sleep 3

#Community Service Tests
clear
echo -e "${BLUE} Starting Communication Service Tests... $RESET"
sleep 5
cd CommunityService
mvn test
cd ..
sleep 2
clear
echo -e "${GREEN} Completed Communication Service Tests... $RESET"
sleep 3

#Interview Service Tests
clear
echo -e "${BLUE} Starting Interview Service Tests... $RESET"
sleep 5
cd InterviewService
mvn test
cd ..
sleep 2
clear
echo -e "${GREEN} Completed Interview Service Tests... $RESET"
sleep 3

#Task Service Tests
clear
echo -e "${BLUE} Starting Task Service Tests... $RESET"
sleep 5
cd task-service
mvn test
cd ..
sleep 2
clear
echo -e "${GREEN} Completed Task Service Tests... $RESET"
echo "---------------------------------------"
echo -e "${GREEN} Completed Unit Tests $RESET"