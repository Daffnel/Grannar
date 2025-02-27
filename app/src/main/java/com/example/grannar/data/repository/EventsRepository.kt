package com.example.grannar.data.repository

import android.util.Log
import android.widget.Toast
import com.example.grannar.adapter.CalenderEventAdapter
import com.example.grannar.data.Calender.EventsData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.Calendar

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

   //Function that gets all events that are marked as attend = true, limited to current month and year

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

    //Function to add event objects to firestore
    fun addEventToFirebase(event: EventsData){
        db.collection("events")
            .document(event.id)
            .set(event)
            .addOnSuccessListener {
                Log.i("Firebase", "Event added!")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to add event", e)
            }
    }


}