#!/bin/bash

find . -name 'pom.xml' |xargs perl -pi -e "s/1.0.0-SNAPSHOT/1.0.0-${BUILD_NUMBER}/g"
