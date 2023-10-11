package com.example.ormancase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    int markerIdx = 1;
    ArrayList<LatLng> latLngList = new ArrayList<>();
    private Polyline polyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Поиск ящиков");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_back_arrow);
        distanceText = findViewById(R.id.distanceText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        gotoNavigationButton = findViewById(R.id.goToNavigation);
        gotoNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
        caseFindedButton = findViewById(R.id.caseFinded);
        caseFindedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AfterFinding.class);
                startActivity(intent);
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
                    userLocation = location;
                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        addAllMarkersOnMap();
        animateCamera();
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                String markerTitle = marker.getTitle();
                for (int i = 0; i < markerOptionsList.size(); i++) {
                    if (markerOptionsList.get(i).getTitle().equals(marker.getTitle())) {
                        markerOptionsList.get(i).position(marker.getPosition());
                    }
                }
                findClosestMarker();
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
            }
        });
    }

    public void animateCamera() {
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mTimeSquare, 15f));
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

    private void addAllMarkersOnMap() {
        //adding user marker
        userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        MarkerOptions userMarkerOptions = new MarkerOptions().position(userLatLng).title("Ваше местоположение");
        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        myMap.addMarker(userMarkerOptions);
        addNewMarker(new LatLng(52.227918, 76.985597));
        addNewMarker(new LatLng(52.199979, 76.932092));
        addNewMarker(new LatLng(52.251392, 76.88235));
        addNewMarker(new LatLng(52.132182, 77.003868));
        addNewMarker(new LatLng(52.249578, 77.026490));
        findClosestMarker();
    }

    private int findClosestMarker() {
        int minimalDistance = 10000000;
        for (int i = 0; i < markerOptionsList.size(); i++) {
            Location tmp = new Location("");
            tmp.setLatitude(markerOptionsList.get(i).getPosition().latitude);
            tmp.setLongitude(markerOptionsList.get(i).getPosition().longitude);
            int distanceToThatMarker = (int) userLocation.distanceTo(tmp);
            if (minimalDistance > distanceToThatMarker) {
                minimalDistance = distanceToThatMarker;
                closestCaseOptions = markerOptionsList.get(i);
                Log.d("aboba", String.valueOf(minimalDistance));
            }
        }
        distanceText.setText("Расстояние между вами и ближайшим орман кейсом - " + String.valueOf(minimalDistance) + " метров");
        if (polyLine!=null) {
            polyLine.remove();
        }
        drawPolyline();
        return minimalDistance;
    }
    private void addNewMarker(LatLng latLng) {
        latLngList.add(latLng);
        MarkerOptions newMarker = new MarkerOptions().position(latLng).title("Орман кейс " + (markerIdx)).draggable(true);
        markerOptionsList.add(newMarker);
        myMap.addMarker(newMarker);
        markerIdx++;
    }
    private void drawPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(userLatLng, closestCaseOptions.getPosition())
                .color(Color.parseColor("#0085FF"))
                .width(10);
        polyLine = myMap.addPolyline(polylineOptions);
    }
}