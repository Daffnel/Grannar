package com.example.grannar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.adpter.CalendarAdapter
import com.example.grannar.databinding.FragmentCalendarBinding
import com.example.grannar.ui.viewmodel.CalendarViewModel
import kotlinx.coroutines.selects.select
import java.time.LocalDate


class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CalendarAdapter.ViewHolder>? = null

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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}