package com.example.grannar.data.Calender

data class EventsData(
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val title: String = "",
    val moreInfo: String = "",
    var attend: Boolean = false
) {



    companion object {

        fun makeDayMonth(day: Int, month: Int): String{

            val monthOut: String

            when(month){
                1 -> monthOut = "jan"
                2 -> monthOut =  "feb"
                3 -> monthOut = "mars"
                4 -> monthOut = "apr"
                5 -> monthOut = "maj"
                6 -> monthOut = "juni"
                7 -> monthOut = "juli"
                8 -> monthOut = "aug"
                9 -> monthOut =  "sep"
                10 -> monthOut =  "okt"
                11-> monthOut = "nov"
                12 -> monthOut = "dec"
                else -> monthOut = "xxx"
            }

            return "$day, $monthOut"
        }
        fun mockUpData(): List<EventsData> {

            val mockEvents = listOf(
                EventsData(18, 2, 2025, "Möte med teamet", "Diskussion om sprint-planering", true),
                EventsData(20, 2, 2025, "Gympass", "Bänkpress och cardio", false),
                EventsData(25, 2, 2025, "Födelsedagsfest", "Firas hemma hos Alex", true),
                EventsData(3, 3, 2025, "Läkarbesök", "Årlig hälsokontroll", false),
                EventsData(10,3, 2025,"Deadline för projekt", "Skicka in rapporten till chefen",true
                ),
                EventsData(15, 3, 2025, "Fotbollsmatch", "Spela med vänner i parken", false),
                EventsData(21, 3, 2025, "Konsert", "Se favoritbandet live", true),
                EventsData(1, 4, 2025, "Aprilskämt", "Planera ett skämt för kollegorna", false),
                EventsData(5, 4, 2025, "Resa till Spanien", "Semester i Barcelona", true),
                EventsData(12, 4, 2025, "Bröllop", "Kusinen gifter sig", true)
            )

            return mockEvents
        }


    }
}