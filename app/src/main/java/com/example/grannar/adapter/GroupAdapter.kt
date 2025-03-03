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
        val group = groupList[position]
        holder.bind(group)
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

        fun bind(group: CityGroups) {
            groupNameTextView.text = group.title


            itemView.setOnClickListener {
                itemClickListener(group)
            }
        }
    }



}

//class GroupAdapter(
//    private var cityGroupList: List<CityGroups>,
//    private val itemClickListener: (CityGroups) -> Unit
//) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
//
//    private var filteredList: List<CityGroups> = cityGroupList
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_chatt, parent, false)
//        return GroupViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
//        val cityGroup = filteredList[position]
//        holder.bind(cityGroup)
//    }
//
//    override fun getItemCount(): Int = filteredList.size
//
//    // Function to update the list and notify RecyclerView of changes
//    fun updateData(newCityGroups: List<CityGroups>) {
//        cityGroupList = newCityGroups
//        filteredList = newCityGroups
//        notifyDataSetChanged() // Notify the adapter that the data has changed
//    }
//
//    // Function to filter the list based on the query
//    fun filter(query: String) {
//        filteredList = if (query.isEmpty()) {
//            cityGroupList
//        } else {
//            cityGroupList.filter { it.name.contains(query, ignoreCase = true) }
//        }
//        notifyDataSetChanged()
//    }
//
//    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
//
//        fun bind(cityGroup: CityGroups) {
//            groupNameTextView.text = cityGroup.name
//
//            // When the item is clicked, call the itemClickListener to open the group chat
//            itemView.setOnClickListener {
//                itemClickListener(cityGroup)
//            }
//        }
//    }
//}
