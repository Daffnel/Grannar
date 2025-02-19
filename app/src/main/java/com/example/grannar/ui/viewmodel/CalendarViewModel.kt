package com.example.grannar.ui.viewmodel

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

    private val _events = MutableLiveData<List<EventsData>>()
    val events: LiveData<List<EventsData>> get() = _events

    fun getEvents(year: Int, month: Int){
        repository.getEventsForMonth(year, month){ eventList ->
            _events.value = eventList
        }
    }

    // TODO Sorter ut Events efter månad







    /**
     * Skapar en array utfrån vilken dag månaden startar på
     * samt antalet dagar i månaden
     */
    fun daysInMonthArray(date: LocalDate): ArrayList<String> {

    val daysInMonthArray = arrayListOf<String>()
    val yearMonth = YearMonth.from(date)

    val daysInMonth = yearMonth.lengthOfMonth()                // Antal dagar i månaden
    val firstOfMonth = date.withDayOfMonth(1)     //  Hämta 1:a dagen i månaden
    val dayOfWeek =

    firstOfMonth.dayOfWeek.value - 1                    //  Hämta veckodagen (1=måndag, 7= söndag value gör om text till 1 -> 7
                                                            // -1 eftersom första dagen i veckan är måndag

    for (i in 1..42) {                              // 7x6 = 42 rutor
        if (i <= dayOfWeek) {
            daysInMonthArray.add(" ")                           // Tomma rutor före månadens start
        } else if (i - dayOfWeek <= daysInMonth) {
            daysInMonthArray.add((i - dayOfWeek).toString())    // Faktiska datum
        } else {
            daysInMonthArray.add(" ")                           // Tomma rutor efter månadens slut
        }
    }
    return daysInMonthArray
}

    /**
     *
     * Formaterar ett datumobjekt i formen ååå mmmm
     *
     */

    fun monthYearFromDate(date: LocalDate): String {

        val formatedDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MMMM")

        return date.format(formatedDate) //  Använd `format()` på `date`

        //tack chatGpt

    }


}