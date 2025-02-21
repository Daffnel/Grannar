package com.example.grannar.data.repository

import com.example.grannar.data.Calender.EventsData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class EventsRepository {
    private val db = Firebase.firestore

    //Get the events from firestore if the chosen year and month matches anything in the database
    //Returns a list of matched events
    fun getEventsForMonth(year: Int, month: Int, callback: (List<EventsData>) -> Unit){
        db.collection("events")
            .whereEqualTo("year", year)
            .whereEqualTo("month", month)
            .get()
            .addOnSuccessListener { result ->
                val eventsList = mutableListOf<EventsData>()
                for (document in result){
                    val event = document.toObject(EventsData::class.java)
                    eventsList.add(event)
                }
                callback(eventsList)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}