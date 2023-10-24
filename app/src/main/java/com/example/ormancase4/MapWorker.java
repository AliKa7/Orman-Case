package com.example.ormancase4;

import android.graphics.Color;
import android.location.Location;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapWorker {
    public static PolylineOptions getNewPolylineOptions(LatLng userLatLng, MarkerOptions closestCaseOptions) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(userLatLng, closestCaseOptions.getPosition())
                .color(Color.parseColor("#0085FF"))
                .width(10);
        return polylineOptions;
    }
    public static MarkerOptions getClosestMarker(ArrayList<MarkerOptions> markerOptionsList, Location userLocation, TextView distanceText) {
        int minimalDistance = 10000000;
        MarkerOptions closestCaseOptions = null;
        for (int i = 0; i < markerOptionsList.size(); i++) {
            Location tmp = new Location("");
            tmp.setLatitude(markerOptionsList.get(i).getPosition().latitude);
            tmp.setLongitude(markerOptionsList.get(i).getPosition().longitude);
            int distanceToThatMarker = (int) userLocation.distanceTo(tmp);
            if (minimalDistance > distanceToThatMarker) {
                minimalDistance = distanceToThatMarker;
                closestCaseOptions = markerOptionsList.get(i);
            }
        }
        distanceText.setText(String.valueOf(minimalDistance) + " метров");
        return closestCaseOptions;
    }
    public static LatLng convertLocationStringToLatLng(String locationString) {
        String[] coordinates = locationString.split(",");
        if (coordinates.length == 2) {
            try {
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                return new LatLng(latitude, longitude);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
