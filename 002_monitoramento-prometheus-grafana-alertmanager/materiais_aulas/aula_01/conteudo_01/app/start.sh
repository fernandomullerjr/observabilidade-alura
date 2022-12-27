#!/bin/bash
# script para iniciar a aplicação manualmente após o build
java -jar -Xms128M -Xmx128M -XX:PermSize=64m -XX:MaxPermSize=128m -Dspring.profiles.active=prod target/forum.jar