package com.example.grannar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.grannar.data.repository.EventsRepository

import com.example.grannar.databinding.FragmentGroupBinding

class GroupFragment: Fragment(){


    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateNewGroup.setOnClickListener {

        }

    }


    override fun onDestroy() {
        super.onDestroy()
    _binding = null
    }
}