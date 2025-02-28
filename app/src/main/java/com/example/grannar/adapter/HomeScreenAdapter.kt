package com.example.grannar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.Calender.EventsData

class HomeScreenAdapter(var events: List<EventsData>): RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var tvEventDate: TextView
        var tvEventTitle: TextView

        init{
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle)
            tvEventDate = itemView.findViewById(R.id.tvEventDate)
        }
    }

    override fun onCreateViewHolder( viewGroup: ViewGroup,viewType: Int): HomeScreenAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_event_cell,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomeScreenAdapter.ViewHolder, position: Int) {
        holder.tvEventDate.text = EventsData.makeDayMonth(events[position].day,events[position].month)
        holder.tvEventTitle.text = events[position].title
    }

    override fun getItemCount(): Int {
        return events.size
    }
}