#!/bin/sh
echo Requires docker-compose
echo Requires npm
echo Starting 'judge0' docker container as local code execution API
cd judge0
docker-compose up -d
cd ..
echo Installing dependencies
npm install
echo Starting web application...
npm run serve

