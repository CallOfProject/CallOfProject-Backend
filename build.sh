#! /bin/bash

TARGET_DIR=$1
FULL_PATH=$TARGET_DIR/"call-of-project"


run_services() {
    local FULL_PATH="$1"
    local USER_INPUT
    echo "Running call-of-project services..."
    echo "bash $FULL_PATH/run.sh"
    sleep 5    
    bash run.sh $FULL_PATH
}


if [ -d "$FULL_PATH" ]; then
     read -p "Do you want to run call-of-project services directly? (y/n): " USER_INPUT

    if [ "$USER_INPUT" = "y" ]; then
        run_services $FULL_PATH
        exit 0
    
    elif [ "$USER_INPUT" = "n" ]; then
        rm -rf "$FULL_PATH"
        echo "Folder removed successfully!: $FULL_PATH"
        exit 1
    
    else
        echo "Invalid input. Please enter 'y' or 'n'."
        echo "You can run services with this command: bash run.sh $FULL_PATH"
        exit 1
    fi
    
else
    echo "Folder does not exists!: $FULL_PATH"
fi

mkdir $FULL_PATH

# Build api apigateway
cd apigateway
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..

# Build Eureka Server
cd EurekaServer
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..

#Build Auth Service
cd Services/AuthenticationService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build Project Service
cd ProjectService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..

# Build Community Service
cd CommunityService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build Email Service
cd EmailService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..



#Build EnvironmentService
cd EnvironmentService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build FilterAndSearchService
cd FilterAndSearchService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..



# Build InterviewService
cd InterviewService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build NotificationService
cd NotificationService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build SchedulerService
cd SchedulerService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build TicketService
cd TicketService
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..


# Build task-service
cd task-service
rm -rf /target/*.jar
mvn package -DskipTests
cd target
cp *jar $FULL_PATH
cd ..
mvn clean
cd ..