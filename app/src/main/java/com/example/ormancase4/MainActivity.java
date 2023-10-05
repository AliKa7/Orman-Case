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
    private Location currentLocation;
    private LatLng userLocationPoint;
    private Polyline polyLine;
    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView distanceText;
    Button toUserLocationButton;
    Button gotoNavigationButton;
    Button caseFindedButton;
    MarkerOptions case1Options = new MarkerOptions().position(new LatLng(52.227918, 76.985597)).title("case 111").draggable(true);
    MarkerOptions case2Options;
    MarkerOptions case3Options;
    MarkerOptions case4Options;
    MarkerOptions case5Options;
    MarkerOptions closestCaseOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toUserLocationButton = findViewById(R.id.toUserLocation);
        distanceText = findViewById(R.id.distanceText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        toUserLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera();
            }
        });
        gotoNavigationButton = findViewById(R.id.goToInstruction);
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
                Intent intent = new Intent(MainActivity.this, CaseInstruction.class);
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
                    currentLocation = location;
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
            public void onMarkerDrag(@NonNull Marker marker) {}

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                /*case1Options.position(marker.getPosition());
                lineBtwnUserNBox.remove();
                drawPolyline();
                findDistanceBetweenTwo(userLocationPoint, case1Options.getPosition());*/
                String markerTitle = marker.getTitle();
                switch (markerTitle) {
                    case "Орман кейс 1": case1Options.position(marker.getPosition()); break;
                    case "Орман кейс 2": case2Options.position(marker.getPosition()); break;
                    case "Орман кейс 3": case3Options.position(marker.getPosition()); break;
                    case "Орман кейс 4": case4Options.position(marker.getPosition()); break;
                    case "Орман кейс 5": case5Options.position(marker.getPosition()); break;
                }
                polyLine.remove();
                findClosestMarker();
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {}
        });
    }
    public void animateCamera() {
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocationPoint, 15f));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mTimeSquare, 15f));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
            else {
                Snackbar.make(this.getCurrentFocus(), "Пожалуйста, разрешите разрешение на использование приложением данных о вашем местоположении", Snackbar.LENGTH_LONG).show();
            }
        }
    }
    private void addAllMarkersOnMap() {
        LatLng abai = new LatLng(52.285426, 76.937295);
        userLocationPoint = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions userMarkerOptions = new MarkerOptions().position(userLocationPoint).title("Ваше местоположение");
        userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        myMap.addMarker(userMarkerOptions);

        ArrayList<MarkerOptions> cases = new ArrayList<>();
        case1Options = new MarkerOptions().position(new LatLng(52.227918, 76.985597)).title("Орман кейс 1").draggable(true);
        myMap.addMarker(case1Options);
        case2Options = new MarkerOptions().position(new LatLng(52.199979, 76.932092)).title("Орман кейс 2").draggable(true);
        cases.add(case2Options);
        case3Options = new MarkerOptions().position(new LatLng(52.251392, 76.882356)).title("Орман кейс 3").draggable(true);
        cases.add(case3Options);
        case4Options = new MarkerOptions().position(new LatLng(52.132182, 77.003868)).title("Орман кейс 4").draggable(true);
        cases.add(case4Options);
        case5Options = new MarkerOptions().position(new LatLng(52.249578, 77.026490)).title("Орман кейс 5").draggable(true);
        cases.add(case5Options);
        for (MarkerOptions thisCaseOptions : cases) {
            myMap.addMarker(thisCaseOptions);
        }
        findClosestMarker();
        //findDistanceBetweenTwo(userLocationPoint, new LatLng(52.227918, 76.985597));
        //drawPolyline();
    }
    private int findDistanceBetweenTwo(LatLng a, LatLng b) {
        Location lA = new Location("");
        lA.setLatitude(a.latitude);
        lA.setLongitude(a.longitude);

        Location lB = new Location("");
        lB.setLatitude(b.latitude);
        lB.setLongitude(b.longitude);
        int distance = (int) lA.distanceTo(lB);
        distanceText.setText("Расстояние между вами и ближайшим орман кейсом - "+String.valueOf(distance)+" метров");
        return distance;
    }
    private int findClosestMarker() {
        Location userLocation = new Location("");
        userLocation.setLatitude(userLocationPoint.latitude);
        userLocation.setLongitude(userLocationPoint.longitude);
        Integer[] distances = new Integer[5];
        Location d1 = new Location("");
        d1.setLatitude(case1Options.getPosition().latitude);
        d1.setLongitude(case1Options.getPosition().longitude);
        distances[0] = (int) userLocation.distanceTo(d1);
        Location d2 = new Location("");
        d2.setLatitude(case2Options.getPosition().latitude);
        d2.setLongitude(case2Options.getPosition().longitude);
        distances[1] = (int) userLocation.distanceTo(d2);
        Location d3 = new Location("");
        d3.setLatitude(case3Options.getPosition().latitude);
        d3.setLongitude(case3Options.getPosition().longitude);
        distances[2] = (int) userLocation.distanceTo(d3);
        Location d4 = new Location("");
        d4.setLatitude(case4Options.getPosition().latitude);
        d4.setLongitude(case4Options.getPosition().longitude);
        distances[3] = (int) userLocation.distanceTo(d4);
        Location d5 = new Location("");
        d5.setLatitude(case5Options.getPosition().latitude);
        d5.setLongitude(case5Options.getPosition().longitude);
        distances[4] = (int) userLocation.distanceTo(d5);
        int minimalDinstance = 1000000000;
        for (int i=0; i<distances.length; i++) {
            if (minimalDinstance>distances[i]) {
                minimalDinstance = distances[i];
                if (distances[i]==(int) userLocation.distanceTo(d1)) {
                    closestCaseOptions = case1Options;
                }
                else if (distances[i]==(int) userLocation.distanceTo(d2)) {
                    closestCaseOptions = case2Options;
                }
                else if (distances[i]==(int) userLocation.distanceTo(d3)) {
                    closestCaseOptions = case3Options;
                }
                else if (distances[i]==(int) userLocation.distanceTo(d4)) {
                    closestCaseOptions = case4Options;
                }
                else if (distances[i]==(int) userLocation.distanceTo(d5)) {
                    closestCaseOptions = case5Options;
                }
            }
        }
        distanceText.setText("Расстояние между вами и ближайшим орман кейсом - "+String.valueOf(minimalDinstance)+" метров");
        drawPolyline();
        return minimalDinstance;
    }
    private void drawPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(userLocationPoint, closestCaseOptions.getPosition())
                .color(Color.parseColor("#0085FF"))
                .width(10);
        polyLine = myMap.addPolyline(polylineOptions);
    }
}