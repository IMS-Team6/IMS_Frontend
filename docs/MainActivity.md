# MainActivity
Start screen initial activity for the application that is connected to bottom menu and its fragments. The main activity contains a permission object and a main class.
#  Permission object
   -Annotated element require API. The API is called on the given API level or higher.
   -Contans an array for SDk permission for accesing coarse location, bluetooth and bluetooth admin. 
   -There are also applied SDK permission levels 29,30 and 31 SDK permissions.
   
#  MainActivity class
   - in oncreate function set up bottom navigation bar is created where bottom menu is implemented.
   - A create bluetooth manager is captured in a object from java class "bluetoothManager".
   - A botton created for bluetooth connection. This botton is using method setOnClickListener where it calls Bluetooth adapter for connection handlings with if statements.
   - Get and Update collion objects that containing a list cillionInfo and returning collion images.

- My Bluetooth service Implemetation
   - In general MyBluetoothService contains the imports, class, inner class and thread, functions and static varibles.
   - Three static global integer varibles representing READ, WRITE and TOAST classifications. 
    - READ varible is used in the overrid function which is responsible for running the bluetooth connection. The function "overrid fun run()" is listening to the input stream until an exception occurs. Which is handled with try and catch blocks.
   - WRITE varible shares the sent message with the UI activity when connections is recieved. The function write is called from main activity when trying to send data from remote device.
   - TOAST varible is used in write function when error occurs when sending data. The toast varible is for sending error message.
   - The service of main activity for bluetooth is handled with Bluetooth Adapter. The function cancel() is implemeted for shutting down the connection with a try block. When closing socket connection fails it is handled with a catch block. The READ, WRITE and TOAST is handled in Log.e.
   - The cancel function in Bluetooth adapter is handled with try and catch block, where try block is trying to close the socket and catch is throwing and error in log.e.
   - 
 # Key words in main activity
onCreate:
Creates the UI for bluetooth and collision functions.
 
BluetoothDevice:
Discovery has found a device
 
onReceive :
 
bool isConnected :
Returns a bool informing if a connection is established to the selected bluetooth device.
 
connect() :
Attempts a connection to the selected device.
 
setupBluetoothPermissions:
Uses specific permission depending on the sdk version.
 
BluetoothManager: 
obtain an instance of an BluetoothAdapter
 
checkPermissions: 
Cheks permission
 
bluetoothConnectBtn: 
Activate the bluetooth and target a specific uuid.
 
writeData:
Writing data in ByteArray
 
Socket:
to manage the connection. On the client side

requestMultiplePermissions:
Requests the needed permission to run the app
