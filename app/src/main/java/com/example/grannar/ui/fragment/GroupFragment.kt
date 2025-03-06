package com.example.grannar.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.adapter.CityGroupsAdapter
import com.example.grannar.adapter.MyGroupsAdapter
import com.example.grannar.data.firebase.FirebaseManager
import com.example.grannar.data.model.Group
import com.example.grannar.data.model.JoinRequest
import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.data.repository.CityGroupsViewModel
//import com.example.grannar.ui.viewmodel.CityGroupsViewModel

import com.example.grannar.databinding.FragmentGroupsBinding
import com.example.grannar.ui.viewmodel.CityGroupsViewModelFactory

class GroupFragment: Fragment(R.layout.fragment_groups) {

    private lateinit var viewModel: CityGroupsViewModel
    private val repository = FirebaseManager()
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseManager: FirebaseManager

    var layoutManagerSecond: RecyclerView.LayoutManager? = null
    var layoutManagerMyGroups: RecyclerView.LayoutManager? = null
    var adapterSecond: RecyclerView.Adapter<CityGroupsAdapter.ViewHolder>? = null
    var myGroupsAdapter: RecyclerView.Adapter<MyGroupsAdapter.MyGroupsViewHolder>? = null


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

        firebaseManager = FirebaseManager()
        val factory = CityGroupsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CityGroupsViewModel::class.java)


        showRecyclerviewAvailableGroups()
        showRecyclerviewMyGroups()

        binding.btnAddGroup.setOnClickListener {

            addNewGroupDialog()

        }
        observeJoinRequests()


        // click icon notification
        binding.imageViewnofification.setOnClickListener {
            showJoinRequests()
        }


        //Test för grupper man är medlem i

//        viewModel.mygroups.observe(viewLifecycleOwner, Observer { groups ->
//            if (groups.isNullOrEmpty()) {
//                Log.d("!!!", "Ingen grupp tillgänglig eller användaren är inte medlem i några grupper.")
//            } else {
//                groups.forEach { group ->
//                    Log.d("!!!", "Användaren är medlem i grupp: ${group.title}")
//                }
//            }
//        })
//
//        binding.btnAddGroup.setOnClickListener {
//            val groupId = "R9zd0O1tb8Xzlw8cMRO3"
//            viewModel.joinGroup(groupId)
//        }


    }

    private fun showRecyclerviewAvailableGroups() {

        layoutManagerSecond = LinearLayoutManager(requireContext())
        binding.recommendedGroupsRecyclerView.layoutManager = layoutManagerSecond
        adapterSecond = CityGroupsAdapter(emptyList())
        binding.recommendedGroupsRecyclerView.adapter = adapterSecond

        //Gets all available groups
        viewModel.getGroupByCity()

        viewModel.groups.observe(viewLifecycleOwner){groups ->
         (adapterSecond as CityGroupsAdapter).updateGroups(groups)
        }


    }

    //Updates my groups' recyclerview
    private fun showRecyclerviewMyGroups() {
        layoutManagerMyGroups = LinearLayoutManager(requireContext())
        binding.myGroupsRecyclerView.layoutManager = layoutManagerMyGroups

        myGroupsAdapter = MyGroupsAdapter(emptyList()) { group ->
            val dialog = GroupInfoDialog(group) { groupId ->
                // Add logging here
                Log.d("GroupFragment", "Group ID: $groupId")
                if (groupId.isNullOrEmpty()) {
                    Log.e("GroupFragment", "Group ID is null or empty")
                    Toast.makeText(requireContext(), "Invalid group ID", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.leaveGroup(groupId)
                }
            }
            dialog.show(parentFragmentManager, "GroupInfoDialog")
        }
        binding.myGroupsRecyclerView.adapter = myGroupsAdapter

        viewModel.getGroupsWhenMember()

        viewModel.mygroups.observe(viewLifecycleOwner) { groups ->
            if (groups.isEmpty()) {
                Log.d("GroupFragment", "User is not part of any group")
            }
            (myGroupsAdapter as MyGroupsAdapter).updateGroups(groups)
        }
    }

    fun showMoreInfoGroup(group: Group){


        val vy = layoutInflater.inflate(R.layout.group_show_more_info_popup,null)
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(vy)
        val dialogRuta = builder.create()

        val tvGroupTitle: TextView

        tvGroupTitle = vy.findViewById(R.id.etGroupShowMoreInfoTitle)

        dialogRuta.show()

    }

    /***
     *
     * PopUp dialog to add a new group
     */

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

        // adds the user's city to the UI
        viewModel.getUserCity()
        viewModel.userCity.observe(viewLifecycleOwner){city ->
            tvCityName.text = city
        }


       val btnAddGroup = vy.findViewById<Button>(R.id.btnAddGNewGroupOk).setOnClickListener {

           val city = viewModel.userCity.value // latest value of the user's city

           if (city != null) {
               viewModel.addNewGroup(etTitle.text.toString(),etMoreInfo.text.toString(),city)
           }
           dialogRuta.dismiss()
            showRecyclerviewAvailableGroups()     //updates the recylerview
       }

       val btnAddGroupCancel = vy.findViewById<Button>(R.id.btnAddNewGroupCancel).setOnClickListener{

           // End

           dialogRuta.dismiss()
       }
          dialogRuta.show()
    }

    //A function that monitors join requests.

    private fun observeJoinRequests() {
        firebaseManager.getJoinRequestsForAdmin { requests ->
            if (requests.isNotEmpty()) {

                showNotification(true)
            } else {

                showNotification(false)
            }
        }
    }

    // fun change  icon
    private fun showNotification(hasNewNotification: Boolean) {
        if (hasNewNotification) {
            binding.imageViewnofification.setImageResource(R.drawable.ic_notifications_new)
        } else {
            binding.imageViewnofification.setImageResource(R.drawable.ice_notification)
        }
    }

    //Function that displays a list of join requests
    private fun showJoinRequests() {
        firebaseManager.getJoinRequestsForAdmin { requests ->
            if (requests.isNotEmpty()) {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Join Requests")
                    .setItems(requests.map { "${it.userName} - ${it.groupName}" }.toTypedArray()) { _, which ->
                        val request = requests[which]
                        showApproveDialog(request)
                    }
                    .create()

                dialog.show()
            } else {
                Toast.makeText(requireContext(), "No new join requests", Toast.LENGTH_SHORT).show()
            }
            showNotification(requests.isNotEmpty())
        }
    }

    //Function that displays a dialog to confirm or reject a join request based on the user's choice (yes/no)

    private fun showApproveDialog(request: JoinRequest) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Approve Request")
            .setMessage("Do you want to approve ${request.userName} to join ${request.groupName}?")
            .setPositiveButton("Yes") { _, _ ->
                firebaseManager.approveJoinRequest(request.groupId, request.userId) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "User added to group", Toast.LENGTH_SHORT).show()
                        showJoinRequests()
                    } else {
                        Toast.makeText(requireContext(), "Failed to add user to group", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                if (request.requestId.isNotEmpty()) {
                    firebaseManager.rejectJoinRequest(request.requestId) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Request rejected", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to reject request", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid request ID", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
