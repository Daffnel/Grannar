package com.example.grannar.ui.fragment
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grannar.R
import com.example.grannar.adapter.CalendarAdapter
import com.example.grannar.adapter.CalenderEventAdapter
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.data.repository.EventsRepository
import com.example.grannar.databinding.FragmentCalendarBinding
import com.example.grannar.ui.viewmodel.CalendarViewModel
import com.example.grannar.ui.viewmodel.CalenderViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate


class CalendarFragment : Fragment(), CalenderEventAdapter.OnItemClickListener {

    private lateinit var viewModel: CalendarViewModel
    private val eventsRepository = EventsRepository()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    private lateinit var layoutManagerEvents: LinearLayoutManager
    private lateinit var adapterEvents: CalenderEventAdapter

   private var selectedYear = LocalDate.now().year
    private var selectedMonth = LocalDate.now().monthValue



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentCalendarBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, CalenderViewModelFactory(eventsRepository))[CalendarViewModel::class.java]

        setupRecyclerView()

        viewModel.getEventsHomeDisply() //TODO test

        viewModel.getEvents(selectedYear, selectedMonth)

        viewModel.events.observe(viewLifecycleOwner) { eventList ->
            adapterEvents.updateData(eventList)
        }


        binding.btnNextMonth.setOnClickListener {
            showNextMoth()
        }

        binding.btnPrevMonth.setOnClickListener {
            showPrevMoth()
        }

        binding.btnAddEvent.setOnClickListener {
             addNewEventPopUp()
            //Toast.makeText(context, "knappjäveln fungerar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupRecyclerView() {
        layoutManagerEvents = LinearLayoutManager(requireContext())
        binding.eventsRecyclerView.layoutManager = layoutManagerEvents

        adapterEvents = CalenderEventAdapter(emptyList(), this)
        binding.eventsRecyclerView.adapter = adapterEvents

    }

    private fun showPrevMoth() {

        viewModel.selectedDate = viewModel.selectedDate.minusMonths(1)
        setMonthView()

        viewModel.getEvents(viewModel.selectedDate.year, viewModel.selectedDate.monthValue)
    }

    private fun showNextMoth() {
        viewModel.selectedDate = viewModel.selectedDate.plusMonths(1)
        setMonthView()

        viewModel.getEvents(viewModel.selectedDate.year, viewModel.selectedDate.monthValue)
    }

    fun setMonthView(){

        binding.tvCalendarMontYear.text = viewModel.monthYearFromDate(viewModel.selectedDate)

        val daysInMonth: ArrayList<String> = viewModel.daysInMonthArray(viewModel.selectedDate)

        val layoutManager = GridLayoutManager(requireContext(), 7)
        binding.calenderRecyclerview.layoutManager = layoutManager
        val adapter = CalendarAdapter(daysInMonth)
        binding.calenderRecyclerview.adapter = adapter


    }

    //en popup för att kunna registera een ny aktivitet

    private fun addNewEventPopUp(){

        val vy= layoutInflater.inflate(R.layout.calender_make_new_event_dialog,null)

        val builder = AlertDialog.Builder(requireContext())

        builder.setView(vy)

        val popupMenu = builder.create()

        val btnPickDate: ImageButton = vy.findViewById(R.id.btnNewEventPopUpPickDate)
        val tvSelectedDate: TextView = vy.findViewById(R.id.tvNewEventPopUpSelectedDate)
        val btnAddEvent: Button = vy.findViewById(R.id.btnAddNewEventPopUp)
        val btnCancel: Button = vy.findViewById(R.id.btnbtnAddNewEventPopUpCancel)





        btnAddEvent.setOnClickListener {

        }

        btnCancel.setOnClickListener {

            popupMenu.dismiss()         //Stänger popupmenyn

            Log.d("!!!","Stänger")
        }

        btnPickDate.setOnClickListener {
            showDatePicDialog(tvSelectedDate)

        }



        popupMenu.show()


    }
    fun showDatePicDialog(choosenDate: TextView): IntArray {

        val calendar = Calendar.getInstance()   //hämta dagens datum (år, månad och dag)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val date = IntArray(3)

        val datePickerDialog = DatePickerDialog(requireContext(),{ _, selectedYear, selectedMonth , selectedDay ->
            date[0] = selectedYear
            date[1] = selectedMonth + 1             //obs!! månaderna går 0 till 11
            date[2] = selectedDay

            val dayMonthText = EventsData.makeDayMonth(selectedDay,selectedMonth +1)     //snygga till det i formatet 1 mars
            val formatedText = "Valt datum: $dayMonthText $selectedYear"
            choosenDate.text = formatedText

        }, year, month, day)        //öppna med dagens datum




        datePickerDialog.show()



        return date


    }

    //Popup meny för att bekräfta en aktivitet

    override fun showPopUpDialog(events: EventsData){

        Log.d("!!!","start popup??")

        val bottomSheetDialog = BottomSheetDialog(requireContext())
         val view = LayoutInflater.from(context).inflate(R.layout.calendar_popup,null)

         val tvTitle = view.findViewById<TextView>(R.id.tvPopUpDialogTitle)
         val tvDate = view.findViewById<TextView>(R.id.tvPopUpDate)
         val tvMoreInfo = view.findViewById<TextView>(R.id.tvPopUpMoreInfo)

         val btnAccept = view.findViewById<Button>(R.id.btnPopUpJoinEvent)
         val btnCancel = view.findViewById<Button>(R.id.btnPopUpCancel)

         val date ="${events.day}/${events.month}/${events.year}"
        tvTitle.text = events.title

        // TODO fixa i ordnning datum strängarna tvDate.text =
         tvDate.text = date
         tvMoreInfo.text = events.moreInfo

         //knapparna för att Deltaga eller inte på en aktivitet

         btnAccept.setOnClickListener {
             events.attend = true
             Toast.makeText(context, "Du deltar i eventet!", Toast.LENGTH_SHORT).show()
             bottomSheetDialog.dismiss()
         }

         btnCancel.setOnClickListener {
             events.attend = false
             bottomSheetDialog.dismiss()
         }

         bottomSheetDialog.setContentView(view)
         bottomSheetDialog.show()

     }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}