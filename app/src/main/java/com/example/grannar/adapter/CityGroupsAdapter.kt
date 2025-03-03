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

class CityGroupsAdapter(var grouplist: List<CityGroups>): RecyclerView.Adapter<CityGroupsAdapter.ViewHolder>() {



    interface OnItemClickListener {
        fun showMoreInfoGroup(group: CityGroups)
    }


    //Keeps updating the group list

    fun updateGroups(newGroups: List<CityGroups>) {
        grouplist = newGroups
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var etTitle: TextView
        var btNext: ImageButton



        init {
            etTitle = itemView.findViewById(R.id.tvGroupName)
            btNext = itemView.findViewById(R.id.btnNextPage)

            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    // listener.showMoreInfoGroup(grouplist[position])
                }
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityGroupsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_page_aviable_groups_cell, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CityGroupsAdapter.ViewHolder, position: Int) {

        holder.etTitle.text = grouplist[position].title

    }

    override fun getItemCount(): Int {
        Log.d("!!!","Number of groups ${grouplist.size}")
        return grouplist.size

    }


}