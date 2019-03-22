#!/bin/bash -e

mvn exec:java -q -Dexec.mainClass="xyz.luan.faire.Main" -Dexec.args="$@"
