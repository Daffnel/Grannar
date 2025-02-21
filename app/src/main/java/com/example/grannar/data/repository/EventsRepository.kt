package com.example.grannar.data.repository

import android.util.Log
import com.example.grannar.adapter.CalenderEventAdapter
import com.example.grannar.data.Calender.EventsData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.Calendar

class EventsRepository {
    private val db = Firebase.firestore

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

   //Funktions som hämtar alla event som här markerad attend = true begränsar oss till aktuell månad och år

    fun getAttendEvents(callback: (List<EventsData>) -> Unit) {
        //Aktuellt år och månad
        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)

        Log.d("!!!","$year -- $month")

        db.collection("events")
            .whereEqualTo("year", year)
            .whereEqualTo("month", month +1 )
            .whereEqualTo("attend", true)
            .get()
            .addOnSuccessListener { result ->
                val eventsList = mutableListOf<EventsData>()
                for (document in result) {
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