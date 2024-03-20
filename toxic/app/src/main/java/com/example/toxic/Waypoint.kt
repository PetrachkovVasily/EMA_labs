package com.example.toxic

import java.io.Serializable

class Waypoint(var street : String, var home : String, var flat : String) : Serializable
{
    override fun toString(): String {
        return "$street st., $home, $flat"
    }
}