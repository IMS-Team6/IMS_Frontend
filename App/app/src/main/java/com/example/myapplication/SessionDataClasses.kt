package com.example.myapplication

data class SessionInfo(val sessionID: String, val robotState: String, val collision: java.lang.Boolean)

data class Session(
    val _id: String,
    val sessionID: String,
    val robotState: String,
    val positions: Positions,
    val collision: java.lang.Boolean,
    val collisionPos: CollisionPos
)

data class Positions(val posX: IntArray, val posY: IntArray)

data class CollisionPos(val colX: IntArray, val colY: IntArray)
