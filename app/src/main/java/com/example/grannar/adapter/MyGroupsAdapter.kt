package com.example.grannar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.Groups.CityGroups

class MyGroupsAdapter(var grouplist: List<CityGroups>,
                      private val onGroupClick : (CityGroups) -> Unit
): RecyclerView.Adapter<MyGroupsAdapter.MyGroupsViewHolder>() {

    fun updateGroups(newGroups: List<CityGroups>) {
        grouplist = newGroups
        notifyDataSetChanged()
    }

    inner class MyGroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val groupCity: TextView = itemView.findViewById(R.id.tvCity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_groups_cell, parent, false)
        return MyGroupsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyGroupsViewHolder, position: Int) {
        val group = grouplist[position]
        holder.groupTitle.text = group.title
        holder.groupCity.text = group.city

        holder.itemView.setOnClickListener{
            onGroupClick(group)
        }
    }

    override fun getItemCount(): Int {
        return grouplist.size

    }


}