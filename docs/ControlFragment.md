# ControlFragment

## General
This fragment is responsible for showing and controlling the mower. It can show if the mower is connected through bluetooth. There is also a Switch which indicates whether the mower should be in manual mode or autonomous mode. If the mode is in autonomous mode, it can't be triggered by the direction buttons but if the mode is switched to manual the buttons will be activated.
<br>

## Properties
| Name | Type |
| ----------- | ----------- |
| viewofLayout | View |
| txtView | TextView |
| txtView2 | TextView |
| txtView3 | TextView |
| controlSwitch | Switch |
| controlUp | Button |
| controlDown | Button |
| controlLeft | Button |
| controlRight | Button |

<br>

## Functions
-----------------
### onViewCreated
Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view. This gives subclasses a chance to initialize themselves once they know their view hierarchy has been completely created. The fragment's view hierarchy is not however attached to its parent at this point

| Parameters | Type |
| ----------- | ----------- | 
| view | View |
| savedInstanceState | Bundle |


```
override fun onViewCreated(view: View, savedInstanceState: Bundle?)
```
-----------------
