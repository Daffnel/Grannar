package com.example.grannar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.R
import com.example.grannar.data.repository.CityGroupRepository
import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.ui.viewmodel.CityGroupsViewModel

import com.example.grannar.data.repository.EventsRepository

import com.example.grannar.databinding.FragmentGroupsBinding
import com.example.grannar.ui.viewmodel.CityGroupsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupFragment: Fragment(R.layout.fragment_groups){

    private lateinit var  viewModel: CityGroupsViewModel
    private val repository = CityGroupRepository()

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = CityGroupsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CityGroupsViewModel::class.java)


        //TODO testing only
        binding.btnAddGroup.setOnClickListener {

            viewModel.groupsByCity.observe(viewLifecycleOwner) { groups ->
                if (groups.isNotEmpty()) {
                    Log.d("!!!", "Found ${groups.size} groups!")
                } else {
                    Log.d("!!!", "No groups were found.")
                }
            }

            viewModel.getGroupByCity("Stockholm")
            Log.d("!!!", viewModel.groupsByCity.value.toString())

            viewModel.joinGroup("LdnllecSr4lFNQrO14Yk",)

      }

    }




    override fun onDestroy() {
        super.onDestroy()
    _binding = null
    }
}