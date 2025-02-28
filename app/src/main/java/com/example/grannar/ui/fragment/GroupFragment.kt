package com.example.grannar.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.adapter.CityGroupsAdapter
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.ui.viewmodel.CityGroupsViewModel

import com.example.grannar.databinding.FragmentGroupsBinding
import com.example.grannar.ui.viewmodel.CityGroupsViewModelFactory

class GroupFragment: Fragment(R.layout.fragment_groups){

    private lateinit var  viewModel: CityGroupsViewModel
    private val repository = FirebaseManager()
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

     var layoutManagerSecond: RecyclerView.LayoutManager? = null
     var adapterSecond: RecyclerView.Adapter<CityGroupsAdapter.ViewHolder>? = null



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


        showRecyclerviewAvailableGroups()

        binding.btnAddGroup.setOnClickListener {

            addNewGroupDialog()

            }


    }

    private fun showRecyclerviewAvailableGroups() {

        layoutManagerSecond = LinearLayoutManager(requireContext())
        binding.recommendedGroupsRecyclerView.layoutManager = layoutManagerSecond
        adapterSecond = CityGroupsAdapter(emptyList())
        binding.recommendedGroupsRecyclerView.adapter = adapterSecond

        //Hämta alla tillgänliga grupper
        viewModel.getGroupByCity()

        viewModel.groups.observe(viewLifecycleOwner){groups ->
         (adapterSecond as CityGroupsAdapter).updateGroups(groups)
        }






    }

    private fun addNewGroupDialog() {



        val vy = layoutInflater.inflate(R.layout.add_new_group_dialog,null)
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(vy)
        val dialogRuta = builder.create()

        val tvCityName: TextView
        val etTitle: EditText
        val etMoreInfo: EditText

        tvCityName = vy.findViewById(R.id.tvAddNewGroupCityName)
        etTitle = vy.findViewById(R.id.etAddNewGroupTitle)
        etMoreInfo = vy.findViewById(R.id.etAddNewGroupMoreInfo)

        // lägg ut anvädarens stad i ui
        viewModel.getUserCity()
        viewModel.userCity.observe(viewLifecycleOwner){city ->
            tvCityName.text = city
        }



       val btnAddGroup = vy.findViewById<Button>(R.id.btnAddGNewGroupOk).setOnClickListener {

           val city = viewModel.userCity.value // senaste värdet av användarens stad

           if (city != null) {
               viewModel.addNewGroup(etTitle.text.toString(),etMoreInfo.text.toString(),city)
           }
           dialogRuta.dismiss()
            showRecyclerviewAvailableGroups()     //updatera recylerviewn
       }

       val btnAddGroupCancel = vy.findViewById<Button>(R.id.btnAddNewGroupCancel).setOnClickListener{

           // Avsluta

           dialogRuta.dismiss()
       }
          dialogRuta.show()
    }


    override fun onDestroy() {
        super.onDestroy()
    _binding = null
    }
}

