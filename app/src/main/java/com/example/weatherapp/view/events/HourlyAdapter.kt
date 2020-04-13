package com.example.weatherapp.view.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.getFormattedTime
import com.example.weatherapp.loadImage
import com.example.weatherapp.model.Hourly
import kotlinx.android.synthetic.main.hour_card.view.*


class HourlyAdapter(var hourlyData: MutableList<Hourly>) :
    RecyclerView.Adapter<HourlyAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): myViewHolder =
        myViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.hour_card,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = hourlyData.size

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(hourlyData[position])
    }

    fun refresh(newEvents: List<Hourly>) {
        hourlyData.clear()
        hourlyData.addAll(newEvents)
        notifyDataSetChanged()
    }

    class myViewHolder(val v: View) : RecyclerView.ViewHolder(v) {


        fun bind(hour: Hourly) {
            v.thumb.loadImage(hour.logo)
            v.time.text = getFormattedTime(hour.localTime?.substring(0,2)?: "0")
            v.temp.text = "${hour.currentTemp!!.toInt()} \u2109"
        }
    }

}