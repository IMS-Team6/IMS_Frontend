# MainActivity

Behålla?:
- My Bluetooth service kit
   - The MyBluetoothService.kt file contains the imports, class, inner class and thread, functions and static varibles.
   - Three static global integer varibles for READ, WRITE and TOAST. 
    - READ varible is used in the overrid function which is responsible for running the bluetooth connection. The function "overrid fun run()" is listening to the input stream until an exception occurs. Which is handled with try and catch blocks.
   - WRITE varible shares the sent message with the UI activity when connections is recieved. The function write is called from main activity when trying to send data from remote device.
   - TOAST varible is used in write function when error occurs when sending data. The toast varible is for sending error message.
   - function cancel is implemeted for shutting down the connection with a try block. When closing socket connection fails it is handled with a catch block.