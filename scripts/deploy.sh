#!/usr/bin/env bash
#rm -R src/main/resources/static/*
#npm run build
mvn clean package
echo 'Copy files...'
scp -i ~/.ssh/tkey.key \
target/my-shares-0.0.1-SNAPSHOT.jar \
ubuntu@150.136.84.103:/home/ubuntu
echo 'Start server...'
ssh -i ~/.ssh/tkey.key ubuntu@150.136.84.103 << EOF
nohup java -jar my-shares-0.0.1-SNAPSHOT.jar > log.txt &
EOF
echo 'Bye'