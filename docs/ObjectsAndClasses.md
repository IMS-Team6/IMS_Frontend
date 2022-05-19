# Objects and Data Classes

## SessionInfo
A data class that holds metadata about a specific session which is used further on to fetch an actual session object from the backend.

| Name | Type |
| ----------- | ----------- |
| sessionID | String |
| robotState | String |
| collision | java.lang.Boolean |

----------------------------

## Session
A data class that represent the actual session object, which contains enough information to be able to display the position and collision data from a specific session.

| Name | Type |
| ----------- | ----------- |
| _id | String |
| sessionID | String |
| robotState | String |
| collision | java.lang.Boolean |
| positions | Map(String, Positions) |
| positions | Map(String, CollisionPos) |
| collisionImgExists | java.lang.Boolean |

----------------------------

## Positions
A data class that holds two lists of x and y values which represents the positons of the mover.

| Name | Type |
| ----------- | ----------- |
| posX | List(String) |
| posY | List(String) |

----------------------------

## CollisionPos
A data class that holds two lists of x and y values which represents the collisions of the mover.

| Name | Type |
| ----------- | ----------- |
| colX | List(String) |
| colY | List(String) |

----------------------------

## DataPoint
A data class that holds x and y values of a specific position or collision from a session.

| Name | Type |
| ----------- | ----------- |
| xVal | Int |
| yVal | Int |

----------------------------

## CollisionInfo
A data class that metadata about a specific image.

| Name | Type |
| ----------- | ----------- |
| sessionID | String |
| imgName | String |