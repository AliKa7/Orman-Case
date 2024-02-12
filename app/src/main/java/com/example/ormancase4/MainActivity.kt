package com.example.ormancase4

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import com.example.ormancase4.MapWorker.Companion.convertLocationStringToLatLng
import com.example.ormancase4.MapWorker.Companion.getClosestMarker
import com.example.ormancase4.MapWorker.Companion.toBitmapFromDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var myMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private val FINE_PERMISSION_CODE = 1
    lateinit var userLocation: Location
    private lateinit var userLatLng: LatLng
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var distanceText: TextView
    private lateinit var gotoNavigationButton: Button
    private lateinit var caseFoundButton: Button
    lateinit var closestCaseOptions: MarkerOptions
    var markerOptionsList = ArrayList<MarkerOptions>()
    private lateinit var dbRef: DatabaseReference
    private lateinit var userMarkerOptions: MarkerOptions
    var allMarkersInitialized = false
    private var activityCreated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Поиск ящиков"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        FirebaseApp.initializeApp(this@MainActivity)
        distanceText = findViewById(R.id.distanceText)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        gotoNavigationButton = findViewById(R.id.goToNavigation)
        gotoNavigationButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    NavigationActivity::class.java
                )
            )
        }
        caseFoundButton = findViewById(R.id.caseFinded)
        caseFoundButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AfterFinding::class.java
                )
            )
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }
        val tasks = fusedLocationProviderClient.lastLocation
        tasks.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = Location("")
                userLocation.latitude = 51.90905450331772
                userLocation.longitude = 79.0954135445567
                mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this@MainActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        initMarkers()
        if (!activityCreated) {
            animateCamera()
        }
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isCompassEnabled = true
        myMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                onMarkerDragMethod(marker)
            }

            override fun onMarkerDragEnd(marker: Marker) {}
            override fun onMarkerDragStart(marker: Marker) {
                onMarkerDragMethod(marker)
            }
        })
    }

    fun onMarkerDragMethod(marker: Marker) {
        val marketToMove = findMarkerByTitle(marker.title)
        marketToMove!!.position(marker.position)
        closestCaseOptions = getClosestMarker(
            markerOptionsList,
            userLocation, distanceText, LanguageModel.getCurrentLanguage(this@MainActivity)
        )
    }

    private fun animateCamera() {
        activityCreated = true
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                this.currentFocus?.let {
                    Snackbar.make(
                        it,
                        "Пожалуйста, дайте разрешение на использование приложением данных о вашем местоположении",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initMarkers() {
        userMarkerInit()
        dbRef = FirebaseDatabase.getInstance().getReference("Cases")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (!allMarkersInitialized) { // new marker
                        for (caseSnapshot in snapshot.children) {
                            val newMarker = MarkerOptions()
                            setMarkerData(newMarker, caseSnapshot, true)
                            markerOptionsList.add(newMarker)
                            myMap.addMarker(newMarker)
                            allMarkersInitialized = true
                            closestCaseOptions = getClosestMarker(
                                markerOptionsList,
                                userLocation, distanceText,
                                LanguageModel.getCurrentLanguage(this@MainActivity)
                            )
                        }
                    } else { // updated marker
                        myMap.clear()
                        markerOptionsList.clear()
                        for (caseSnapshot in snapshot.children) {
                            val newMarker = MarkerOptions()
                            setMarkerData(newMarker, caseSnapshot, true)
                            markerOptionsList.add(newMarker)
                            myMap.addMarker(newMarker)
                            allMarkersInitialized = true
                        }
                        userMarkerInit()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun userMarkerInit() {
        userLatLng = LatLng(51.90905450331772, 79.0954135445567)
        userMarkerOptions = MarkerOptions().position(userLatLng).title("Ваше местоположение")
        val drawable = AppCompatResources.getDrawable(
            applicationContext,
            R.drawable.baseline_emoji_people_45_cyan
        )
        val icon = BitmapDescriptorFactory.fromBitmap(
            toBitmapFromDrawable(
                drawable!!
            )
        )
        userMarkerOptions.icon(icon)
        myMap.addMarker(userMarkerOptions)
    }

    private fun findMarkerByTitle(title: String?): MarkerOptions? {
        for (markerOptions in markerOptionsList) {
            if (markerOptions.title == title) {
                return markerOptions
            }
        }
        return null
    }

    fun setMarkerData(markerOptions: MarkerOptions, snapshot: DataSnapshot, isDraggable: Boolean) {
        //there is error!
        val location = snapshot.child("Location").getValue(String::class.java)
        val fullness = snapshot.child("Fullness").getValue(String::class.java)
        val title = "Орман кейс " + snapshot.child("Number").getValue(Int::class.java)
            .toString() + " - " + fullness
        markerOptions.title(title)
        markerOptions.position(convertLocationStringToLatLng(location!!)!!)
        markerOptions.draggable(isDraggable)
        when (fullness) {
            "Полностью заполнен" -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                markerOptions.visible(true)
            }

            "Частично заполнен" -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                markerOptions.visible(true)
            }

            "Пустой" -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                markerOptions.visible(false)
            }

            else -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        }
    }
}