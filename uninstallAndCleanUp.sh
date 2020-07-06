#!/bin/sh

if ! command -v adb &> /dev/null
then
    echo "ADB needs to be in your PATH. The script will finish now."
    echo "Head over https://developer.android.com/studio/command-line/adb for more info about ADB."
    echo "Here's a good step-by-step guide on how to setup ADB in your PATH: https://wiki.lineageos.org/adb_fastboot_guide.html"
    exit
fi

adb uninstall com.kinandcarta.create.proxytoggle
adb shell settings delete global http_proxy
adb shell settings delete global global_http_proxy_host
adb shell settings delete global global_http_proxy_port
