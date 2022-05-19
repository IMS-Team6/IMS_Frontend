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
###  populateSpinnerWithSessions
Takes the current fetched session ids stored in local variable and populates the existing spinner item within the MapHistoryFragment. Also sets a onItemSelectedListener on the spinner item.

| Parameters | Type |
| ----------- | ----------- | 
| sessions | List<SessionInfo> |

| Returns |
| --------|
| Void |

```
private fun populateSpinnerWithSessions(sessions: List<SessionInfo>) 
```
-----------------
###  stringToList
Takes in a string as a parameter and removes whitespaces, commas and converts the remaining content to a list of strings.

| Parameters | Type |
| ----------- | ----------- | 
| str | String |

| Returns |
| --------|
| List(String) |

```
private fun stringToList(str: String): List<String> 
```
-----------------
###  convertToDataPoints
Takes in two seperate strings which in turn represents the values of x and y-coordinates. Parses and converts the content to a list of datapoints.

| Parameters | Type |
| ----------- | ----------- | 
| xValues | String |
| yValues | String |

| Returns |
| --------|
| MutableList(DataPoint) |

```
private fun stringToList(str: String): List<String> 
```
-----------------
###  checkIfMaxMinX
Checks if the given x value is larger than the local variable 'xValMax' stored in the fragment class and if so, stores the new value in that same variable.

| Parameters | Type |
| ----------- | ----------- | 
| x | Int |

| Returns |
| --------|
| Void |

```
private fun checkIfMaxMinX(x: Int)
```
-----------------
###  checkIfMaxMinY
Checks if the given y value is larger than the local variable 'yValMax' stored in the fragment class and if so, stores the new value in that same variable.

| Parameters | Type |
| ----------- | ----------- | 
| y | Int |


| Returns |
| --------|
| Void |

```
private fun checkIfMaxMinX(x: Int)
```
-----------------
###  getScaleConstant
Takes in the maps size/boundaries as an argument, in the type of a Rect. And, later on checks which scaleconstant multiplied by the existing min and maximum x,y values fits in the given rectangle. Returns the biggest possible scaleconstant.

| Parameters | Type |
| ----------- | ----------- | 
| rect | Rect |

| Returns |
| --------|
| Int |

```
private fun getScaleConstant(rect: Rect): Int
```
-----------------
###  drawOnCanvas
Takes in two seperate variables that each contain a list of DataPoints. Later on checks if there exists any datapoints inside the given lists and if so, draws the values on the screen.

| Parameters | Type |
| ----------- | ----------- | 
| positions | MutableList(DataPoint) |
| collisions | MutableList(DataPoint) |

| Returns |
| --------|
| Void |

```
private fun drawOnCanvas(positions: MutableList<DataPoint>, collisions: MutableList<DataPoint>)
```
-----------------
###  fetchCollisionObjects
Takes a string url as an argument and calls a function that in turn performs a GET-request to the backend. If it succeeds, the function will receive a list of collisionobjects and later on parse the result and store information about collision objects in a local variable inside the MainActivity.

| Parameters | Type |
| ----------- | ----------- | 
| sUrl | String |

| Returns |
| --------|
| Void |

```
private fun fetchCollisionObjects(sUrl: String)
```
-----------------
###  fetchSession
Takes a string url as an arguemtn and make calls a funtion that in turns performs a GET-request to the backend. If it succeeds, the function will later on receive a session object containing information about the current sessions x and y values. Which it parses and calls the function drawOnCanvas.

| Parameters | Type |
| ----------- | ----------- | 
| sUrl | String |

| Returns |
| --------|
| Void |

```
private fun fetchSession(sUrl: String)
```
-----------------
###  fetchSessions
Takes a string url as an arguemtn and make calls a funtion that in turns performs a GET-request to the backend. If it succeeds, the function will later on receive a kust of session object containing meta information about the existing sessions in the database. Later on the function parses the results and calls populateSpinnerWithSessions.

| Parameters | Type |
| ----------- | ----------- | 
| sUrl | String |

| Returns |
| --------|
| Void |

```
private fun fetchSessions(sUrl: String)
```
-----------------
###  getRequest
Takes a string url as an arguemtn and make calls a funtion that in turns performs a GET-request to the backend. If it succeeds, the function will return the response body as a string. 

| Parameters | Type |
| ----------- | ----------- | 
| sUrl | String |

| Returns |
| --------|
| String |

```
private fun getRequest(sUrl: String): String?
```





