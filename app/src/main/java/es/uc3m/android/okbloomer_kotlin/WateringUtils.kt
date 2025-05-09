package es.uc3m.android.okbloomer_kotlin

import android.util.Log

//check to see if the plant needs watering
fun needsWater(lastWateredMillis: Long, frequencyInDays: Float): Boolean {
    val now = System.currentTimeMillis()
    val frequencyMillis = (frequencyInDays * 24 * 60 * 60 * 1000).toLong() //the user enters the frequency in days
    //testing ying
    //val frequencyMillis = (frequencyInDays * 1000).toLong() //the user enters the frequency in seconds
    return now >= (lastWateredMillis + frequencyMillis)
}