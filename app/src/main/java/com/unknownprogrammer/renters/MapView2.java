package com.unknownprogrammer.renters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapView2 extends AppCompatActivity {


    private GoogleMap Map;
    com.google.android.gms.maps.MapView mapView;

    Double latitude,longitude;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view2);

        Bundle extras = getIntent().getExtras();
        latitude = Double.valueOf(extras.getString("latitude"));
        longitude = Double.valueOf(extras.getString("longitude"));

        latLng = new LatLng(latitude,longitude);

        mapView = findViewById(R.id.map_view_2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        Mapinit();



    }

    private void Mapinit() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //OnMapReady
                Map = googleMap;
                Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Map.getUiSettings().setZoomGesturesEnabled(true);
                Map.getUiSettings().setMapToolbarEnabled(false);

                Map.addMarker(new MarkerOptions().position(latLng));
                Map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,24));

            }
        });

    }

}
