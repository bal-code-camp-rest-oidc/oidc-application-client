#!/bin/sh

cd "$(dirname "$0")"
echo `pwd`

APACHEDS_SERVER=default

files=$(shopt -s nullglob dotglob; echo ./apacheds/data/$APACHEDS_SERVER/data/*)
if (( ${#files} ))
    then
        echo Deleting ApacheDS cache files ...
        rm -r ./apacheds/data/$APACHEDS_SERVER/data/*
fi

echo Wiped off ApacheDS instance!