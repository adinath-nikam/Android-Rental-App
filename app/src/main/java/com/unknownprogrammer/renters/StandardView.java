package com.unknownprogrammer.renters;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class StandardView extends Fragment {

    View view;
    RecyclerView recyclerView;
    CircleImageView seller_pic;
    ImageView bs_iv;
    private  int CALL_PERMISSION_CODE = 1;
    ProgressDialog progressDialog;
    private TextView view_res_name,view_city_name,view_state_name,view_address,view_price,view_pincode,post_date,seller_name,View_phone;
    public StandardView() {
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
        view = inflater.inflate(R.layout.fragment_standard_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Data..");
        progressDialog.setMessage("Please Wait..");



        RetriveData();



        return view;
    }

    private void RetriveData() {

        progressDialog.show();

        //RV


        FirebaseRecyclerOptions<ResidenceModel> options =
                new FirebaseRecyclerOptions.Builder<ResidenceModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("UNSORTED PROPERTIES")
                                ,ResidenceModel.class)
                        .build();


        FirebaseRecyclerAdapter<ResidenceModel, ResidenceViewHolder> adapter
                = new FirebaseRecyclerAdapter<ResidenceModel, ResidenceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ResidenceViewHolder holder, final int position, @NonNull final ResidenceModel model) {
                holder.residence_name.setText(model.getResidence_name());
                holder.residence_country.setText(model.getResidence_country());
                holder.residence_city.setText(model.getResidence_city());

                Glide.with(getActivity()).load(model.getImgUrl()).into(holder.residence_iv);

                progressDialog.dismiss();





                holder.viewonMap_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),MapView2.class);
                        String latitude,longitude;
                        latitude = String.valueOf(model.getLatitude());
                        longitude = String.valueOf(model.getLongitude());
                        Bundle extras = new Bundle();
                        extras.putString("latitude", latitude);
                        extras.putString("longitude", longitude);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
                holder.details_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        View dialogView = getLayoutInflater().inflate(R.layout.residence_view_bottomsheet, null);
                        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                        dialog.setContentView(dialogView);
                        view_address = dialogView.findViewById(R.id.view_address_name);
                        view_res_name = dialogView.findViewById(R.id.view_res_name);
                        view_city_name = dialogView.findViewById(R.id.view_city_name);
                        view_price = dialogView.findViewById(R.id.view_price_name);
                        view_pincode = dialogView.findViewById(R.id.view_pincode_name);
                        view_state_name = dialogView.findViewById(R.id.view_state_name);
                        post_date = dialogView.findViewById(R.id.post_date);
                        seller_name = dialogView.findViewById(R.id.seller_name);
                        seller_pic = dialogView.findViewById(R.id.seller_pic);
                        View_phone = dialogView.findViewById(R.id.contactBtnID);
                        bs_iv = dialogView.findViewById(R.id.bs_iv);
                        //
                        view_address.setText(model.getResidence_address());
                        view_res_name.setText(model.getResidence_name());
                        view_city_name.setText(model.getResidence_city());
                        view_price.setText(model.getResidence_pricing());
                        view_pincode.setText(String.valueOf(model.getResidence_pincode()));
                        view_state_name.setText(model.getResidence_state());
                        post_date.setText(model.getDate());

                        Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser()
                                .getPhotoUrl()).into(seller_pic);
                        seller_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                        View_phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //request permission
                                if(ContextCompat.checkSelfPermission(getActivity(),
                                        Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {

                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getResidence_phone()));
                                    startActivity(intent);

                                }else {
                                    requestStoragePermission();
                                }
                                //request permission
                            }
                        });

                        dialog.show();

                    }
                });



            }

            @NonNull
            @Override
            public ResidenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.residence_item, parent, false);
                ResidenceViewHolder viewHolder = new ResidenceViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        /////////
        //RV



    }


    public class ResidenceViewHolder extends RecyclerView.ViewHolder{

        ImageView residence_iv;
        Button viewonMap_btn,details_btn;
        TextView residence_name,residence_country,residence_city;
        public ResidenceViewHolder(@NonNull View itemView) {
            super(itemView);

            residence_name = itemView.findViewById(R.id.res_name);
            residence_city = itemView.findViewById(R.id.res_city);
            residence_country = itemView.findViewById(R.id.res_country);
            viewonMap_btn = itemView.findViewById(R.id.viewon_map_btn);
            details_btn = itemView.findViewById(R.id.details_btn);
            residence_iv = itemView.findViewById(R.id.res_iv);



        }
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed for Calling")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
        }
    }


}



