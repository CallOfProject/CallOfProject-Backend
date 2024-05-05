#!/bin/bash

BASE_DIR=$(pwd)
pid_file="$BASE_DIR/service_pids.txt"

while IFS= read -r pid; do
    kill -9 "$pid"
    echo "PID $pid killed."
done < "$pid_file"

> "$pid_file"
echo "PID file cleared."
