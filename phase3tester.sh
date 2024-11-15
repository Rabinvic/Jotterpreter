#!/bin/bash

# Change directory to 'bin'
cd ./bin || { echo "Directory 'bin' not found"; exit 1; }

# Iterate over all .jott files in '../phase3testcases' recursively
for file in ../phase3testcases/*.jott; do
    # Check if the file exists
    if [[ -f "$file" ]]; then
        echo "Running Jott on $file"
        java Jott "$file"
        echo ""
    else
        echo "No .jott files found in '../phase3testcases'"
        break
    fi
done
