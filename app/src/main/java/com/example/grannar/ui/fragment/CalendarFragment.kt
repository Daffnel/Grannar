package com.example.grannar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.adpter.CalendarAdapter
import com.example.grannar.adpter.CalenderEventAdapter
import com.example.grannar.data.Calender.EventsData
import com.example.grannar.databinding.FragmentCalendarBinding
import com.example.grannar.ui.viewmodel.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate


class CalendarFragment : Fragment(), CalenderEventAdapter.OnItemClickListener {

    private lateinit var viewModel: CalendarViewModel

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CalendarAdapter.ViewHolder>? = null

    private var layoutManagerEvents: RecyclerView.LayoutManager? = null
    private var adapterEvents: RecyclerView.Adapter<CalenderEventAdapter.ViewHolder>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentCalendarBinding.inflate(inflater, container,false)


        setup()


        binding.btnNextMonth.setOnClickListener {
        showNextMoth()
    }

    binding.btnPrevMonth.setOnClickListener{
        showPrevMoth()
    }

        return binding.root
    }

    private fun setup() {
        viewModel.selectedDate = LocalDate.now()
        setMonthView()
        setEventsView()
    }

    private fun setEventsView() {

        //Mockup data
        val mockUpList: List<EventsData> = EventsData.mockUpData()

        layoutManagerEvents = LinearLayoutManager(requireContext())

        binding.eventsRecyclerView.layoutManager = layoutManagerEvents
        adapterEvents = CalenderEventAdapter(mockUpList,this)
        binding.eventsRecyclerView.adapter = adapterEvents
    }

    private fun showPrevMoth() {

        viewModel.selectedDate = viewModel.selectedDate.minusMonths(1)
        setMonthView()
        
    }

    private fun showNextMoth() {
        viewModel.selectedDate = viewModel.selectedDate.plusMonths(1)
        setMonthView()
    }

    fun setMonthView(){

        binding.tvCalendarMontYear.text = viewModel.monthYearFromDate(viewModel.selectedDate)

        val daysInMonth: ArrayList<String> = viewModel.daysInMonthArray(viewModel.selectedDate)

        layoutManager = GridLayoutManager(requireContext(), 7)

        binding.calenderRecyclerview.layoutManager = layoutManager
        adapter = CalendarAdapter(daysInMonth)
        binding.calenderRecyclerview.adapter = adapter


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

    fun onItemClick(event: EventsData) {
        Toast.makeText(requireContext(), "Du klickade på ${event.title}", Toast.LENGTH_SHORT).show()    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}