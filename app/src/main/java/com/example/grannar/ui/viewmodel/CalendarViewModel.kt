package com.example.grannar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.data.repository.EventsRepository
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarViewModel(private val repository: EventsRepository): ViewModel() {

    var selectedDate: LocalDate = LocalDate.now()

    //kopplade till kalendern
    private val _events = MutableLiveData<List<EventsData>>()
    val events: LiveData<List<EventsData>> get() = _events

    fun getEvents(year: Int, month: Int){  //hämtar aktiviteter per månad
        repository.getEventsForMonth(year, month){ eventList ->
            val sortedList = eventList.sortedBy { it.day }
            _events.value = sortedList
        }
    }

   //kopplade till att visa aktuella Aktiviteter i hemskärmen
    private val _eventsHomeDisplay = MutableLiveData<List<EventsData>>()
    val  eventsHomeDisplay: LiveData<List<EventsData>> get() = _eventsHomeDisplay

    fun getEventsHomeDisply(){
        repository.getAttendEvents(){ attendedList ->
            Log.d("!!!", "Attended List: $attendedList")
            _eventsHomeDisplay.value = attendedList
        }
    }

    fun addEvent(event: EventsData){
        repository.addEventToFirebase(event)
    }
    /**
     * Creates an array based on what day the month starts at
     * as well as the number of days in that month
     */
    fun daysInMonthArray(date: LocalDate): ArrayList<String> {

    val daysInMonthArray = arrayListOf<String>()
    val yearMonth = YearMonth.from(date)

    val daysInMonth = yearMonth.lengthOfMonth()              // number of days in the month
    val firstOfMonth = date.withDayOfMonth(1)     //  gets the 1st day of the month
    val dayOfWeek =

    firstOfMonth.dayOfWeek.value - 1                        // gets the week days (1=Monday, 7= Sunday value is converted to text 1 -> 7
                                                            // -1 because the first day of the week is Monday (in our app)

    for (i in 1..42) {                                 // 7x6 = 42 cells
        if (i <= dayOfWeek) {
            daysInMonthArray.add(" ")                           // empty cells before the start of the month
        } else if (i - dayOfWeek <= daysInMonth) {
            daysInMonthArray.add((i - dayOfWeek).toString())    // the actual date
        } else {
            daysInMonthArray.add(" ")                           // empty cells after the month ends
        }
    }
    return daysInMonthArray
}

    /**
     *
     * Formats a date object from year then month
     *
     */

    fun monthYearFromDate(date: LocalDate): String {

        val formatedDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMMM")

        return date.format(formatedDate) //  use `format()` on `date`

    }
}