package com.example.locationtracker.activity


import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.Constants
import com.example.locationtracker.R
import com.example.locationtracker.databinding.LtActivityGoogleMapBinding
import com.example.locationtracker.model.LatLongData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class LTGoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: LtActivityGoogleMapBinding
    private lateinit var latLongList: ArrayList<LatLng>
    private lateinit var carMarker: Marker
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var carIcon: BitmapDescriptor
    private lateinit var markers: ArrayList<Marker>
    private var isAnimating = false
    private var animationIndex = 0
    private var handler: Handler = Handler()


    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LtActivityGoogleMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        latLongList = ArrayList()
        val parcelableArray =
            intent.getParcelableArrayListExtra<LatLongData>(Constants.LAT_LONG_LIST)
        print(latLongList)
        if (parcelableArray != null) {
            for (latLong in parcelableArray) {
                latLongList.add(LatLng(latLong.latitude, latLong.longitude))
            }
        }
        val mapView =
            supportFragmentManager.findFragmentById(R.id.mv_location) as SupportMapFragment
        mapView.getMapAsync(this)

        binding.fabPlay.setOnClickListener {
            if (isAnimating) {
                stopAnimation()
            } else {
                startAnimation()
            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLongList.first(), 12f))

        // Initialize the markers list
        markers = ArrayList()

        // Add markers for all locations
        for (latLng in latLongList) {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .anchor(0.5f, 0.5f)
            )
            if (marker != null) {
                markers.add(marker)
            }
        }

        // to add the car marker
        val carBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_car_image)
        carIcon = BitmapDescriptorFactory.fromBitmap(carBitmap)
        carMarker = googleMap.addMarker(
            MarkerOptions()
                .icon(carIcon)
                .position(latLongList.first())
                .anchor(0.5f, 0.5f)  // Center the car image on the marker position
        )!!

        // Draw polyline for the route
        polylineOptions = PolylineOptions()
            .color(Color.BLUE)
            .width(5f)
        for (latLng in latLongList) {
            polylineOptions.add(latLng)
        }
        googleMap.addPolyline(polylineOptions)

    }

    /**
     * to change the angle of image
     */
    private fun getBearing(start: LatLng, end: LatLng): Float {
        val deltaLng = end.longitude - start.longitude
        val deltaLat = end.latitude - start.latitude

        val angle = Math.atan2(deltaLng, deltaLat)

        // Convert radians to degrees
        return Math.toDegrees(angle).toFloat()
    }

    /**
     * to animate the location history
     */
    private fun animateLocationHistory() {
        latLongList.forEachIndexed { index, latLng ->
            if (!isAnimating) {
                return
            }
            handler.postDelayed({
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 2000, null)
                carMarker.position = latLng

                if (index < latLongList.size - 1) {
                    val nextLatLng = latLongList[index + 1]
                    val bearing = getBearing(carMarker.position, nextLatLng)
                    carMarker.rotation = bearing
                    markers[index].setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
                }
                googleMap.addPolyline(polylineOptions)
            }, index * 2000L)
        }
    }

    private fun startAnimation() {
        isAnimating = true
        binding.fabPlay.setImageResource(R.drawable.ic_stop)

        animateLocationHistory()
    }

    private fun stopAnimation() {
        isAnimating = false
        binding.fabPlay.setImageResource(R.drawable.ic_play)
        handler.removeCallbacksAndMessages(null)
    }
}