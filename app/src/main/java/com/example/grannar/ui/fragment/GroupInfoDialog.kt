package com.example.grannar.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.grannar.R
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.repository.CityGroupsViewModel

class GroupInfoDialog(private val group: CityGroups, private val leaveGroup: (String) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.group_info_dialog, null)

        //Uppdaterar xml filen med informationen vi vill visa
        view.findViewById<TextView>(R.id.groupTitle).text = group.title
        view.findViewById<TextView>(R.id.groupMembers).text = "Medlemmar: ${group.members?.size ?: 0}"
        view.findViewById<TextView>(R.id.groupCity).text = "Stad: ${group.city}"
        view.findViewById<TextView>(R.id.groupMoreInfo).text = "Info: ${group.moreInfo}"

        //knapp för att lämna gruppen som man just nu är inne och kollar i
        val btnLeave = view.findViewById<Button>(R.id.btnLeaveGroup)
        btnLeave.setOnClickListener {
            leaveGroup(group.id)
            dismiss()
        }

        //knapp för att stänga ner popup rutan
        val btnClose = view.findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}