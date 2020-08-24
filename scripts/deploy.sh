#!/usr/bin/env bash
mvn clean package
echo 'Copy files...'
scp -i ~/.ssh/tkey.key \
target/my-shares-0.0.1-SNAPSHOT.jar \
ubuntu@129.213.153.243:/home/ubuntu
echo 'Start server...'
ssh -i ~/.ssh/tkey.key ubuntu@129.213.153.243 << EOF
nohup java -jar my-shares-0.0.1-SNAPSHOT.jar > log.txt &
EOF