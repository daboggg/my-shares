#!/usr/bin/env bash
#rm -R src/main/resources/static/*
#npm run build
mvn clean package
echo 'Copy files...'
scp -i ~/.ssh/tkey.key \
target/my-shares-0.0.1-SNAPSHOT.jar \
opc@193.122.207.217:/home/opc
echo 'Start server...'
ssh -i ~/.ssh/tkey.key opc@193.122.207.217 << EOF
nohup java -jar my-shares-0.0.1-SNAPSHOT.jar > log.txt &
EOF
echo 'Bye'