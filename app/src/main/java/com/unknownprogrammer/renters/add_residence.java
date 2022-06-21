package com.unknownprogrammer.renters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class add_residence extends AppCompatActivity {

    String[] country = { "India", "USA", "China", "Japan", "Other"};
    String[] state = { "India", "USA", "China", "Japan", "Other"};
    EditText R_address,R_name,R_pincode,R_city,R_pricing,R_phone;
    String currentTime,currentDate;
    String Address,Name,Pincode,State,City,Country,Latitude="",Longitude="",Pricing,PhoneNumber;
    ScrollView scrollView;
    LatLng latLng;
    // Uri indicates, where the image will be picked from
    private Uri filePath = null;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    private int indicator = 0;
    private ImageView select_imgview;

    Task<Uri> Downloaduri;

    private static String Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    LocationTrack locationTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_residence);
        
        setupToolbar();
        setupViews();
        DateTime();

        CountrySpinner();
        StateSpinner();

        addResidencebtn();

        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indicator == 0) {
                    findViewById(R.id.left).setBackgroundColor(Color.parseColor("#FF0000"));
                    findViewById(R.id.right).setBackgroundColor(Color.BLUE);
                }
                indicator = 1;
                findViewById(R.id.right).setBackgroundColor(Color.BLUE);
                CurrentLocation();
            }
        });

        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indicator == 1) {
                    findViewById(R.id.right).setBackgroundColor(Color.parseColor("#FF0000"));
                    findViewById(R.id.left).setBackgroundColor(Color.BLUE);
                }
                indicator = 0;
                findViewById(R.id.left).setBackgroundColor(Color.BLUE);
            }
        });

        findViewById(R.id.select_img_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });



    }

    private void DateTime() {
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }


    private void CountrySpinner() {

        final Spinner spin = (Spinner) findViewById(R.id.country_spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country = spin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }

    private void StateSpinner(){
        final Spinner spin = (Spinner) findViewById(R.id.state_spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State = spin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,state);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    private void setupViews() {
        R_name = findViewById(R.id.residence_name_id);
        R_address = findViewById(R.id.residence_address_id);
        R_city = findViewById(R.id.residence_city_id);
        R_pincode = findViewById(R.id.pincode_id);
        scrollView = findViewById(R.id.scrollView_id);
        scrollView.setVerticalScrollBarEnabled(false);
        R_pricing = findViewById(R.id.residence_price_id);
        select_imgview = findViewById(R.id.select_img);
        R_phone = findViewById(R.id.residence_phone_id);

    }



    private void addResidencebtn() {

        findViewById(R.id.add_residence_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Address = R_name.getText().toString();
                Name = R_name.getText().toString();
                Pincode = R_pincode.getText().toString();
                City = R_city.getText().toString();
                Pricing = R_pricing.getText().toString();
                PhoneNumber = R_phone.getText().toString();

                if(Address.isEmpty() || Name.isEmpty() || Pincode.isEmpty() || City.isEmpty() || Latitude.isEmpty() || Longitude.isEmpty() || Pricing.isEmpty() || filePath == null || PhoneNumber.isEmpty() || PhoneNumber.length() != 10){
                    if(Address.isEmpty()){
                        R_address.setError("Required");
                        R_address.requestFocus();
                    }
                    if(Pricing.isEmpty()){
                        R_pricing.setError("Required");
                        R_pricing.requestFocus();
                    }
                    if(Pincode.isEmpty()){
                        R_pincode.setError("Required");
                        R_pincode.requestFocus();
                    }
                    if(City.isEmpty()){
                        R_city.setError("Required");
                        R_city.requestFocus();
                    }
                    if(Name.isEmpty()){
                        R_name.setError("Required");
                        R_name.requestFocus();
                    }
                    if(Latitude.isEmpty() || Longitude.isEmpty()){
                        Snackbar.make(v,"Please Select Location",Snackbar.LENGTH_LONG).show();
                    }
                    if(filePath == null){
                        Snackbar.make(v,"Please Select Image",Snackbar.LENGTH_LONG).show();
                    }
                    if(PhoneNumber.isEmpty()){
                        R_phone.setError("Required");
                        R_phone.requestFocus();
                    }
                    if(PhoneNumber.length() != 10){
                        Snackbar.make(v,"Enter Valid Phone Number",Snackbar.LENGTH_LONG).show();
                    }
                }
                else{

                    uploadImage();

                }
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.add_residence_toolbar);
        toolbar.setTitle("Add Residence");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
    }
    private void UploadPropertydata(String ImgUrl){


        add_residence_model addModel = new add_residence_model(Name,Address,City,Pincode,Country,State,Pricing,Userid,currentTime,currentDate,PhoneNumber,ImgUrl,Double.valueOf(Latitude),Double.valueOf(Longitude));

        FirebaseDatabase.getInstance().getReference()
                .child("SORTED PROPERTIES")
                            .child(Country)
                            .child(State)
                            .child(Pincode)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(currentDate)
                            .child(currentTime)
                .setValue(addModel);

        FirebaseDatabase.getInstance().getReference()
                .child("UNSORTED PROPERTIES")
                .child(currentTime)
                .setValue(addModel);
        Toast.makeText(add_residence.this, "Successfully Registered your Property", Toast.LENGTH_SHORT).show();


        finish();
        startActivity(new Intent(add_residence.this,MainActivity.class));
    }


    //current location
    public void CurrentLocation() {
        locationTrack = new LocationTrack(add_residence.this);
        Latitude = String.valueOf(locationTrack.getLatitude());
        Longitude = String.valueOf(locationTrack.getLongitude());
        latLng = new LatLng(locationTrack.getLatitude(),locationTrack.getLongitude());
        Toast.makeText(getApplicationContext(),String.valueOf(latLng), Toast.LENGTH_SHORT).show();
    }


    //current location

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Select Image method


    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                select_imgview.setVisibility(View.VISIBLE);
                select_imgview.setImageBitmap(bitmap);
                scrollView.fullScroll(10);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }










    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            FirebaseStorage.getInstance().getReference()
                    .child("Residence Images/")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(currentDate)
                    .child(currentTime)
                    .putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    //Get Download Url
                                    Downloaduri = FirebaseStorage.getInstance().getReference()
                                            .child("Residence Images/")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(currentDate)
                                            .child(currentTime).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String Imgurl = String.valueOf(uri);
                                                    UploadPropertydata(Imgurl);
                                                }
                                            });
                                    //Get Download Url




                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


    //
}
