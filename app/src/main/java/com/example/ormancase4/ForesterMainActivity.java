package com.example.ormancase4;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
public class ForesterMainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    SupportMapFragment mapFragment;
    private final int FINE_PERMISSION_CODE = 1;
    private Location userLocation;
    private LatLng userLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView distanceText;
    MarkerOptions closestCaseOptions;
    ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();
    ArrayList<LatLng> latLngList = new ArrayList<>();
    DatabaseReference dbRef;
    MarkerOptions userMarkerOptions;
    boolean allMarkersInitialized = false;
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forester_activity_main);
        getSupportActionBar().setTitle("Поиск ящиков");
        FirebaseApp.initializeApp(ForesterMainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
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
                    mapFragment.getMapAsync(ForesterMainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        initMarkers();
        animateCamera();
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
                    if (!allMarkersInitialized) {
                        for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                            String location = caseSnapshot.child("Location").getValue(String.class);
                            LatLng latLng = null;
                            if (location != null) {
                                latLng = MapWorker.convertLocationStringToLatLng(location);
                            }
                            int fullnessRange = caseSnapshot.child("Fullness").getValue(Integer.class);
                            int markerNumber = caseSnapshot.child("Number").getValue(Integer.class);
                            String title = "Орман кейс " + String.valueOf(markerNumber);
                            latLngList.add(latLng);
                            MarkerOptions newMarker = new MarkerOptions().position(latLng).draggable(true);
                            setMarkerData(fullnessRange, newMarker, title);
                            markerOptionsList.add(newMarker);
                            myMap.addMarker(newMarker);
                            allMarkersInitialized = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int number = snapshot.child("Number").getValue(Integer.class);
                int fullnessRange = snapshot.child("Fullness").getValue(Integer.class);
                String title = "Орман кейс " + String.valueOf(number);
                for (MarkerOptions markerOptions : markerOptionsList) {
                    if (markerOptions.getTitle().contains(title)) { // если это именно тот кейс у которого изменены данные
                        setMarkerData(fullnessRange, markerOptions, title);
                        myMap.clear(); // Clear the map and add all markers again
                        for (MarkerOptions mo : markerOptionsList) {
                            myMap.addMarker(mo);
                        }
                        userMarkerInit();
                        closestCaseOptions = MapWorker.getClosestMarker(markerOptionsList, userLocation, distanceText); // Recalculate closest marker
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userMarkerInit() {
        userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        userMarkerOptions = new MarkerOptions().position(userLatLng).title("Ваше местоположение");
        Drawable drawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.baseline_emoji_people_45);
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

    public void setMarkerData(int fullnessRange, MarkerOptions markerOptions, String title) {
        switch (fullnessRange) {
            case 3:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                markerOptions.title(title + " - Полностью наполнен");
                break;
            case 2:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerOptions.title(title + " - Частично наполнен");
                break;
            case 1:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerOptions.title(title + " - Пустой");
                break;
            default:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                markerOptions.title(title + "ДАННЫЕ НЕ ПОЛУЧЕНЫ");
                break;
        }
    }

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
                startActivity(new Intent(ForesterMainActivity.this, CaseInfo.class));
                return true;
            case "Вызов SOS":
                startActivity(new Intent(ForesterMainActivity.this, CallSOS.class));
                return true;
            case "Инструкция":
                startActivity(new Intent(ForesterMainActivity.this, AdditionalInfo.class));
                return true;

        }
        return true;
    }
}
