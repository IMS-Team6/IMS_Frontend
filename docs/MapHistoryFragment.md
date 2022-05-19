# MapHistoryFragment

## General
This fragment is responsible for handling the fetching and visualization process of the position data from the mover. The fragment is also responsible to lead the user to another fragment called 'ImageFragment'.

<br>

## Properties
| Name | Type |
| ----------- | ----------- |
| viewofLayout | View |
| graphTitle | View | 
| mapView | View | 
| showCollisionButton | View |
| client | OkHttpClient |
| fetchedSession | Session (Custom Class) | 
| selectedSessionId | String | 
| xValMax | Int | 
| xValMin | Int | 
| yValMax | Int | 
| yValMin | Int | 
| baseUrl | String | 

<br>

## Functions
### onCreateView
```
 fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View 
```
Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation). This will be called between onCreate(android.os.Bundle) and onActivityCreated(android.os.Bundle).

Parameters | 
| ----------- | ----------- | 
| 





