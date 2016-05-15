#!/bin/bash

sudo yum update -y

sudo service httpd start

aws s3 sync s3://marcotedone-devopsbuddy-config/ ~/.devopsbuddy/

sudo service devopsbuddy start