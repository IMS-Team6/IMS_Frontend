# ImageFragment

## General
This fragment is responsible for fetching and visualizing collision images from a specific session. It contains a TextView for holding the current selected session id, an ImageView for representing the actual image and a spinner that is populated with existing images from a specific session.

<br>

## Properties
| Name | Type |
| ----------- | ----------- |
| viewOfLayout | View |
| imageObjects | List(CollisionInfo) |
| imgView | ImageView |
| txtView | TextView |
| sessionId | String |
| baseURL | String |

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
### populateSpinnerWithImageIds
A functions that collects all local image objects from this fragment and populates the spinner item with each respective image object entity. And creates and onItemSelectedListener on the spinner item.

| Parameters | Type |
| ----------- | ----------- | 
| Void | Void |


| Returns |
| --------|
| Void |

```
private fun populateSpinnerWithImageIds() 
```
-----------------
### showImageFromApi
Takes a string as a url and uses it with a library called to fetch the selected image and render it on the existing ImageView within this fragment.

| Parameters | Type |
| ----------- | ----------- | 
| sUrl | String |


| Returns |
| --------|
| Void |

```
private fun populateSpinnerWithImageIds() 
```