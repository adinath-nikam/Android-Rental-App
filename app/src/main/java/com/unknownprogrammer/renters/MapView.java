package com.unknownprogrammer.renters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.unknownprogrammer.renters.R.id.map_view;

public class MapView extends Fragment {

    View view;
    private GoogleMap Map;
    Marker Rmarker;
    private FloatingActionButton profile_fab,settings_fab,sat_fab,add_home_fab,current_loc_fab,main_fab;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private boolean isOpen = true;
    private boolean isSatelliteView = false;
    private static final int REQUEST_CODE = 101;
    private add_residence_model marker_data;
    com.google.android.gms.maps.MapView mapView;
    LocationTrack locationTrack;
    Marker current_loc_marker;;


//    LocationRequest locationRequest;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    public MapView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        setupViews();
        Mapinit();


        //Buttons
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {
                    settings_fab.startAnimation(fab_close);
                    sat_fab.startAnimation(fab_close);
                    add_home_fab.startAnimation(fab_close);
                    main_fab.startAnimation(fab_anticlock);
                    settings_fab.setClickable(false);
                    sat_fab.setClickable(false);
                    add_home_fab.setClickable(false);
                    isOpen = false;
                }else{
                    add_home_fab.startAnimation(fab_open);
                    sat_fab.startAnimation(fab_open);
                    settings_fab.startAnimation(fab_open);
                    main_fab.startAnimation(fab_clock);
                    settings_fab.setClickable(true);
                    sat_fab.setClickable(true);
                    add_home_fab.setClickable(true);
                    isOpen = true;
                }
            }
        });

        settings_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "settings", Toast.LENGTH_SHORT).show();
            }
        });

        add_home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),add_residence.class));
            }
        });
        sat_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSatelliteView){
                    Map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    isSatelliteView = false;
                }
                else {
                    Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    isSatelliteView = true;
                }
            }
        });

        profile_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ProfileActivity.class));
            }
        });

        current_loc_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentLocation();
            }
        });
        //Buttons

        return view;
    }




    private void setupViews(){

        profile_fab = view.findViewById(R.id.profile_fab_id);
        current_loc_fab = view.findViewById(R.id.current_location_fab_id);
        settings_fab = view.findViewById(R.id.settings_fab_id);
        sat_fab = view.findViewById(R.id.satellite_fab_id);
        main_fab = view.findViewById(R.id.fab_id);
        add_home_fab = view.findViewById(R.id.add_home_fab_id);


        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_anticlock);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


    }


        private void Mapinit(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //OnMapReady
                Map = googleMap;
                Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Map.getUiSettings().setZoomGesturesEnabled(true);
                Map.getUiSettings().setMapToolbarEnabled(false);
                CurrentLocation();


                // Fetch Data From Firebase

                FirebaseDatabase.getInstance().getReference()
                        .child("UNSORTED PROPERTIES")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                                for (DataSnapshot s : dataSnapshot.getChildren()) {
                                    marker_data = s.getValue(add_residence_model.class);
                                    final LatLng location = new LatLng(marker_data.latitude, marker_data.longitude);
                                    Map.addMarker(new MarkerOptions().position(location).snippet(marker_data.Residence_country).title("\u20B9"+marker_data.Residence_pricing)).showInfoWindow();

                                    Map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {

                                        }
                                    });



                                }

                                Map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {

                                        Toast.makeText(getActivity(), marker_data.getResidence_name(), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });






                //OnMApReady
            }
        });



    }

    //current location method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    CurrentLocation();
                }
        }
    }

    //current location method

    public void CurrentLocation() {
        locationTrack = new LocationTrack(getActivity());

        if(current_loc_marker != null){
            current_loc_marker.remove();
        }


        if (locationTrack.canGetLocation()) {

            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            LatLng latLng = new LatLng(latitude,longitude);

            current_loc_marker = Map.addMarker(new MarkerOptions().position(latLng)
                    .title("Here you go!"));
            current_loc_marker.showInfoWindow();
            Map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,25));

            Toast.makeText(getActivity(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
    }

}
