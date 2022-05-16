package com.example.myapplication

// Data class that holds meta info about each existing session from backend.
data class SessionInfo(val sessionID: String, val robotState: String, val collision: java.lang.Boolean)

data class Session(
    val _id: String,
    val sessionID: String,
    val robotState: String,
    val collision: java.lang.Boolean,
    val positions: Map<String, Positions> = mapOf(),
    val collisionPos: Map<String, CollisionPos> = mapOf()
)
// Data classes belonging to Session data class.
data class Positions(val posX: List<String>, val posY: List<String>)
data class CollisionPos(val colX: List<String>, val colY: List<String>)

// Data class to host the x and y values
data class DataPoint(val xVal: Int, val yVal: Int)

// Data class that holds info of ids from existing collision images.
data class CollisionInfo(val imgName: String)