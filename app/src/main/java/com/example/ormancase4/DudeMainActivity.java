package com.example.ormancase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class DudeMainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    SupportMapFragment mapFragment;
    private final int FINE_PERMISSION_CODE = 1;
    private Location userLocation;
    private LatLng userLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView distanceText;
    Button gotoNavigationButton;
    Button caseFindedButton;
    MarkerOptions closestCaseOptions;
    ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();
    DatabaseReference dbRef;
    MarkerOptions userMarkerOptions;
    boolean allMarkersInitialized = false;
    boolean activityCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dude_activity_main);
        getSupportActionBar().setTitle("Поиск ящиков");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FirebaseApp.initializeApp(DudeMainActivity.this);
        distanceText = findViewById(R.id.distanceText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        gotoNavigationButton = findViewById(R.id.goToNavigation);
        gotoNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DudeMainActivity.this, NavigationActivity.class));
            }
        });
        caseFindedButton = findViewById(R.id.caseFinded);
        caseFindedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DudeMainActivity.this, AfterFinding.class));
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> tasks = fusedLocationProviderClient.getLastLocation();
        tasks.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userLocation = new Location("");
                    userLocation.setLatitude(51.930398);
                    userLocation.setLongitude(79.073695);
                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(DudeMainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        initMarkers();
        if (!activityCreated) {
            animateCamera();
        }
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                onMarkerDragMethod(marker);
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                onMarkerDragMethod(marker);
            }
        });
    }
    public void onMarkerDragMethod(Marker marker) {
        MarkerOptions marketToMove = findMarkerByTitle(marker.getTitle());
        marketToMove.position(marker.getPosition());
        closestCaseOptions = MapWorker.getClosestMarker(markerOptionsList, userLocation, distanceText);
    }

    public void animateCamera() {
        activityCreated = true;
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Snackbar.make(this.getCurrentFocus(), "Пожалуйста, дайте разрешение на использование приложением данных о вашем местоположении", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void initMarkers() {
        userMarkerInit();
        dbRef = FirebaseDatabase.getInstance().getReference("Cases");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!allMarkersInitialized) { // new marker
                        for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                            MarkerOptions newMarker = new MarkerOptions();
                            setMarkerData(newMarker, caseSnapshot, true);
                            markerOptionsList.add(newMarker);
                            myMap.addMarker(newMarker);
                            allMarkersInitialized = true;
                            closestCaseOptions = MapWorker.getClosestMarker(markerOptionsList, userLocation, distanceText);
                        }
                    }
                    else { // updated marker
                        myMap.clear();
                        markerOptionsList.clear();
                        for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                            MarkerOptions newMarker = new MarkerOptions();
                            setMarkerData(newMarker, caseSnapshot, true);
                            markerOptionsList.add(newMarker);
                            myMap.addMarker(newMarker);
                            allMarkersInitialized = true;
                        }
                        userMarkerInit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userMarkerInit() {
        userLatLng = new LatLng(51.930398, 79.073695);
        userMarkerOptions = new MarkerOptions().position(userLatLng).title("Ваше местоположение");
        Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_emoji_people_45_cyan);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(MapWorker.DrawableToBitmap(drawable));
        userMarkerOptions.icon(icon);
        myMap.addMarker(userMarkerOptions);
    }

    private MarkerOptions findMarkerByTitle(String title) {
        for (MarkerOptions markerOptions : markerOptionsList) {
            if (markerOptions.getTitle().equals(title)) {
                return markerOptions;
            }
        }
        return null;
    }

    public void setMarkerData(MarkerOptions markerOptions, DataSnapshot snapshot, boolean isDraggable) {
        //there is error!
        String location = snapshot.child("Location").getValue(String.class);
        String fullness = snapshot.child("Fullness").getValue(String.class);
        String title = "Орман кейс " + snapshot.child("Number").getValue(Integer.class).toString() + " - " + fullness;
        markerOptions.title(title);
        markerOptions.position(MapWorker.convertLocationStringToLatLng(location));
        markerOptions.draggable(isDraggable);
        switch (fullness) {
            case "Полностью заполнен":
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                markerOptions.visible(true);
                break;
            case "Частично заполнен":
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                markerOptions.visible(true);
                break;
            case "Пустой":
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerOptions.visible(false);
                break;
            default:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                break;
        }
    }
}

/*
^
|
|
|
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.main_color)), 0, spannableString.length(), 0);
            menuItem.setTitle(spannableString);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        switch (title) {
            case "Состав ящика":
                startActivity(new Intent(DudeMainActivity.this, CaseInfo.class));
                return true;
            case "Вызов SOS":
                startActivity(new Intent(DudeMainActivity.this, CallSOS.class));
                return true;
            case "Инструкция":
                startActivity(new Intent(DudeMainActivity.this, AdditionalInfo.class));
                return true;

        }
        return true;
    }*/