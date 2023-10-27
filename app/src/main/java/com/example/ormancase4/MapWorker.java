package com.example.ormancase4;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
    public static Bitmap DrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
