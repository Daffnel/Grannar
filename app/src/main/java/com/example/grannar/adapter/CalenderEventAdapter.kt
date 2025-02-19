package com.example.grannar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.Calender.EventsData


class CalenderEventAdapter(
    var events: List<EventsData>,
    val listener: OnItemClickListener): RecyclerView.Adapter<CalenderEventAdapter.ViewHolder>(){


    interface OnItemClickListener {
        fun showPopUpDialog(events: EventsData)
    }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
        var itemDate: TextView = itemView.findViewById(R.id.tvEventDate)
        var itemAttendIcon: ImageView = itemView.findViewById(R.id.ivAttend)


        init {
                itemView.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.showPopUpDialog(events[position])
                        Log.d("!!!","klickat")
                    }
                }

            }
        }

    fun updateData(newEvents: List<EventsData>){
        events = newEvents
        notifyDataSetChanged()
    }





    override fun onCreateViewHolder(viewGroup: ViewGroup,viewType: Int): CalenderEventAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_event_cell,viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CalenderEventAdapter.ViewHolder, position: Int) {
       holder.itemTitle.text = events[position].title
        holder.itemDate.text = EventsData.makeDayMonth(events[position].day,events[position].month)

        //sätt en ok bil om deltaga på eventet
        if(events[position].attend == true){

            holder.itemAttendIcon.setImageResource(R.drawable.ic_attend)
        }



    }

    override fun getItemCount(): Int {
       return events.size
    }


}