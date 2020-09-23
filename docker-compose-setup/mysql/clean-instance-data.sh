#!/bin/sh

cd "$(dirname "$0")"
echo `pwd`

files=$(shopt -s nullglob dotglob; echo ./data/*)
if (( ${#files} ))
    then
        echo Deleting MySQL data files ...
        rm -r ./data/*
fi

echo Wiped off MySQL data!