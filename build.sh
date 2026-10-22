#!/bin/sh
# Compila el APK de Brújula. Uso: ./build.sh (release) o ./build.sh debug
export JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"
cd "$(dirname "$0")"
/c/Users/ricar/gradle-dist/gradle-9.3.1/bin/gradle.bat $([ "$1" = debug ] && echo assembleDebug || echo assembleRelease) --console=plain > build-log.txt 2>&1
echo "exit $?"
grep -E "^e: |error:|FAILED|BUILD|What went wrong" -A3 build-log.txt | head -60
