﻿@echo off
::关闭DataServer
@taskkill /f /im DataServer.exe
::暂停1秒
ping 127.1 -n 1 >nul
::启动DataServer
start "" "D:\dataserver\DataServer.exe"
exit