# ConnectFragment

## General
This fragment is responsible for showing the bluetooth status of the device. The functionality of bluetooth is implemented within MainActivity. However, this fragment holds basic functions, such as onResume, that checks if user is connected to the mover or not, and updates the UI accordingly.

<br>

## Properties
| Name | Type |
| ----------- | ----------- |
| viewofLayout | View |
| bluetoothStatusText | TextView |

<br>

## Functions
-----------------
### onCreateView
Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). This will be called between onCreate(android.os.Bundle) and onActivityCreated(android.os.Bundle).

| Parameters | Type |
| ----------- | ----------- | 
| inflater | LayoutInflater |
| container | ViewGroup |
| savedInstanceState | Bundle |

| Returns |
| --------|
| View |

```
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View 
```
-----------------
### onResume
Called when the fragment is visible to the user and actively running. This is generally tied to Activity.onResume of the containing Activity's lifecycle.

| Parameters | Type |
| ----------- | ----------- | 
| Void | Void |


| Returns |
| --------|
| Void |

```
override fun onResume()
```
-----------------
### updateBluetoothConnectionStatus
Checks if device is connected to the mover via bluetooth and updated the ConnectFragment UI accordingly.

| Parameters | Type |
| ----------- | ----------- | 
| Void | Void |


| Returns |
| --------|
| Void |

```
private fun updateBtConnectionStatus()
```