package com.example.ormancase4

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapWorker {
    companion object {
        @JvmStatic
        fun getNewPolylineOptions(
            userLatLng: LatLng?,
            closestCaseOptions: MarkerOptions
        ): PolylineOptions {
            return PolylineOptions()
                .add(userLatLng, closestCaseOptions.position)
                .color(Color.parseColor("#0085FF"))
                .width(10f)
        }

        @SuppressLint("SetTextI18n")
        @JvmStatic
        fun getClosestMarker(
            markerOptionsList: ArrayList<MarkerOptions>,
            userLocation: Location,
            distanceText: TextView
        ): MarkerOptions {
            var minimalDistance = 10000000
            lateinit var closestCaseOptions: MarkerOptions
            for (i in markerOptionsList.indices) {
                val tmp = Location("")
                tmp.latitude = markerOptionsList[i].position.latitude
                tmp.longitude = markerOptionsList[i].position.longitude
                val distanceToThatMarker = userLocation.distanceTo(tmp).toInt()
                if (minimalDistance > distanceToThatMarker) {
                    minimalDistance = distanceToThatMarker
                    closestCaseOptions = markerOptionsList[i]
                }
            }
            distanceText.text = "$minimalDistance метров"
            return closestCaseOptions
        }

        @JvmStatic
        fun convertLocationStringToLatLng(locationString: String): LatLng? {
            val coordinates = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (coordinates.size == 2) {
                try {
                    val latitude = coordinates[0].toDouble()
                    val longitude = coordinates[1].toDouble()
                    return LatLng(latitude, longitude)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        @JvmStatic
        fun toBitmapFromDrawable(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }

}
