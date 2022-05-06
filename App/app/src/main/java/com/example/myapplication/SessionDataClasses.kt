package com.example.myapplication

data class SessionInfo(val sessionID: String, val robotState: String, val collision: java.lang.Boolean)

data class Session(
    val _id: String,
    val sessionID: String,
    val robotState: String,
    val positions: Map<String, Positions> = mapOf(),
    val collision: java.lang.Boolean,
    val collisionPos: Map<String, CollisionPos> = mapOf()
)

data class Positions(val posX: List<String>, val posY: List<String>)

data class CollisionPos(val colX: List<String>, val colY: List<String>)
