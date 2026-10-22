#!/bin/sh
# Ayudas para probar en el emulador. Uso: tools/emu.sh instalar | foto <nombre> | toque <x> <y>
ADB="/c/Users/ricar/AppData/Local/Android/Sdk/platform-tools/adb.exe"
SHOTS="$(dirname "$0")/../capturas"
mkdir -p "$SHOTS"
case "$1" in
  esperar)
    "$ADB" wait-for-device
    until [ "$("$ADB" shell getprop sys.boot_completed | tr -d '\r')" = "1" ]; do sleep 2; done
    echo listo ;;
  instalar)
    "$ADB" install -r "$(dirname "$0")/../app/build/outputs/apk/release/app-release.apk"
    "$ADB" shell dumpsys package com.brujula.vocacional | grep lastUpdateTime
    "$ADB" shell am start -n com.brujula.vocacional/.MainActivity ;;
  foto)
    sleep "${3:-1}"
    "$ADB" exec-out screencap -p > "$SHOTS/$2.png"
    echo "$SHOTS/$2.png" ;;
  toque) "$ADB" shell input tap "$2" "$3" ;;
  adb) shift; "$ADB" "$@" ;;
esac
