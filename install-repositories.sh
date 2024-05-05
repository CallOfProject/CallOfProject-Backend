#! /bin/bash

cd Repositories

# Build CommonDataLib
cd CommonDataLib
mvn clean
mvn install
cd ..

# Build NoSQLRepositoryLib
cd NoSQLRepositoryLib
mvn clean
mvn install
cd ..

# Build AuthenticationRepositoryLib
cd AuthenticationRepositoryLib
mvn clean
mvn install
cd ..

# Build EnvironmentRepositoryLib
cd EnvironmentRepositoryLib
mvn clean
mvn install
cd ..


#Build ProjectRepositoryLib
cd ProjectRepositoryLib
mvn clean
mvn install
cd ..

# Build InterviewRepositoryLib
cd InterviewRepositoryLib
mvn clean
mvn install
cd ..

# Build TaskRepository
cd TaskRepository
mvn clean
mvn install
cd ..

#Build CommunityRepositoryLib
cd CommunityRepositoryLib
mvn clean
mvn install
cd ..