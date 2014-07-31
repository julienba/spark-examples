#!/bin/bash

for i in {1..10}; do 
    nb_records=$(shuf -i 1000-1000000 -n 1)
    echo "sbt13 'runMain org.jba.RandomLogWriter ${nb_records} /tmp/data/${i}.json'"
    sbt13 "runMain org.jba.RandomLogWriter ${nb_records} /tmp/data/${i}.json"
done

