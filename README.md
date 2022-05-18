# IMS_Frontend

Frontend Documentation

# Introduction, purpose:
Application created in Android Studio. Purpose of the app is to control the mower and fetch data from backend.

# Step by step
1. Download Android Studio and open repository https://github.com/IMS-Team6/IMS_Frontend/tree/main/App
2. Clone the code and display it in Android Studio
3. Connect the android phone to the computer and choose Physical connected device in Device Manager
4. Run the application

# Implementation details in Kotlin:

Visuals, UX, Functions, imports, android manifest-> Permissions,
Gradle scripts and dependencies, packages, android SDK

# Code structure
- Main Activity kit
- DataClasses kit
- Paint objects kit
- My Bluetooth service kit
   - The MyBluetoothService.kt file contains the imports, class, inner class and thread, functions and static varibles.
   - Three static global integer varibles for READ, WRITE and TOAST. 
    - READ varible is used in the overrid function which is responsible for running the bluetooth connection. The function "overrid fun run()" is listening to the input stream until an exception occurs. Which is handled with try and catch blocks.
   - WRITE varible shares the sent message with the UI activity when connections is recieved. The function write is called from main activity when trying to send data from remote device.
   - TOAST varible is used in write function when error occurs when sending data. The toast varible is for sending error message.
   - function cancel is implemeted for shutting down the connection with a try block. When closing socket connection fails it is handled with a catch block.
# fragments
- Control fragment
- Connect fragment
- Map History fragment

# Resource (res) root:
layout, menu, mipmap, navigation, values
# Functions
Bluetooth Connection
Fetching images from backend
