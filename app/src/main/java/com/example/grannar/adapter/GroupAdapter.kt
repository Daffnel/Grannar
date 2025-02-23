package com.example.grannar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.model.Group

class GroupAdapter(
    private var groupList: List<Group>,
    private val itemClickListener: (Group) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_chatt, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int = groupList.size

    // Function to update the list and notify RecyclerView of changes
    fun updateData(newGroups: List<Group>) {
        groupList = newGroups
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)

        fun bind(group: Group) {
            groupNameTextView.text = group.groupName

            // When the item is clicked, call the itemClickListener to open the group chat
            itemView.setOnClickListener {
                itemClickListener(group)
            }
        }
    }
}