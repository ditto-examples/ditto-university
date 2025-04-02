#!/bin/bash

# Get the directory of the script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Define source and destination paths
SOURCE_FILE="$SCRIPT_DIR/DittoManager-Finished.kt"
DEST_FILE="$SCRIPT_DIR/../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt"

# Check if source file exists
if [ ! -f "$SOURCE_FILE" ]; then
    echo "Error: Source file $SOURCE_FILE does not exist"
    exit 1
fi

# Copy the file
cp "$SOURCE_FILE" "$DEST_FILE"

# Check if copy was successful
if [ $? -eq 0 ]; then
    echo "Successfully copied DittoManager-Finished.kt to DittoManager.kt"
else
    echo "Error: Failed to copy file"
    exit 1
fi