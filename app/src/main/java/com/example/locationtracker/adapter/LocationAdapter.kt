package com.example.locationtracker.adapter
// LocationAdapter.kt
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R
import com.example.locationtracker.listener.LocationTouchListener
import com.example.locationtracker.model.LocationData
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class LocationAdapter(
    private val locationDataList: ArrayList<LocationData>,
    private val listener: LocationTouchListener
) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {


    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDateAndTime: TextView = itemView.findViewById(R.id.tv_date_and_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lt_adapter_location_item, parent, false)
        return LocationViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationData = locationDataList[position]

        // Set data to views
        holder.tvAddress.text = locationData.address
        holder.tvDateAndTime.text = locationData.timestamp
        holder.itemView.setOnClickListener {
            listener.onLocationTouchClickListener(locationDataList, locationData)
        }
    }

    override fun getItemCount(): Int {
        return locationDataList.size
    }

}
