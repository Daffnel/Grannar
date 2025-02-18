package com.example.grannar.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.google.android.material.snackbar.Snackbar

class CalendarAdapter(var dayOfMonth: ArrayList<String>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var daySelected: Int? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var tvDayOfMonth: TextView    //det datum som visas i griden

        init{

            tvDayOfMonth = itemView.findViewById(R.id.tvDayOfMonth)

            //if some of the days number are pressed
            itemView.setOnClickListener { v: View ->
                val position = adapterPosition

               //TODO ta bort enbart för test
                Snackbar.make(v, "du valde ${dayOfMonth[position]} --  $daySelected",
                    Snackbar.LENGTH_SHORT).setAction("Action",null).show()

            }


        }

    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {

        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_cell,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {

        holder.tvDayOfMonth.text = dayOfMonth[position]

            if(daySelected == position){
                holder.tvDayOfMonth.setBackgroundResource(R.drawable.calender_selected)
            }

        holder.tvDayOfMonth.setOnClickListener {
            daySelected = position  // Uppdatera valt datum
            notifyDataSetChanged()  // Uppdatera RecyclerView hitta en bättre lösning??
        }


    }

    override fun getItemCount(): Int {

        return dayOfMonth.size
    }


}