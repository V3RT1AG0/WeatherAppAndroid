package com.example.weatherapp.view.events;


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.loadImage
import com.example.weatherapp.model.Daily
import kotlinx.android.synthetic.main.event_cardview.view.*


class WeatherAdapter(var weeklyData: MutableList<Daily>) :
    RecyclerView.Adapter<WeatherAdapter.myViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): myViewHolder =
        myViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.event_cardview,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = weeklyData.size

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(weeklyData[position])
    }

    fun refresh(newEvents: List<Daily>) {
        weeklyData.clear()
        weeklyData.addAll(newEvents)
        notifyDataSetChanged()
    }

    class myViewHolder(val v: View) : RecyclerView.ViewHolder(v) {


        fun bind(day: Daily) {
            v.poster_thumb.loadImage(day.logo)
            v.high.text = "${day.highTemp!!.toInt()} \u2109"
            v.low.text = "${day.lowTemp!!.toInt()} \u2109"
            v.day.text = day.weekday
        }
    }

}