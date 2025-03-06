package com.example.grannar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.Groups.CityGroups
import com.example.grannar.data.model.Group

class GroupAdapter(
    private var groupList: List<CityGroups>,
    private val itemClickListener: (CityGroups) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var filteredList: List<CityGroups> = groupList



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_chatt, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        if (filteredList.isEmpty()) {

            holder.bindEmpty()
        } else {
            val group = filteredList[position]
            holder.bind(group)
        }

    }

    override fun getItemCount(): Int = filteredList.size




    fun updateData(newGroups: List<CityGroups>) {
        this.groupList = newGroups
        this.filteredList = newGroups
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            groupList
        } else {
            groupList.filter { it.title.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }


    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        private val groupCitynameTextView: TextView = itemView.findViewById(R.id.groupCitynameTextView)

        fun bind(group: CityGroups) {
            groupNameTextView.text = group.title
            groupCitynameTextView.text=group.city


            itemView.setOnClickListener {
                itemClickListener(group)
            }
        }

        fun bindEmpty() {
            groupNameTextView.text = "No groups found"
            groupCitynameTextView.text = ""
        }
    }



}

