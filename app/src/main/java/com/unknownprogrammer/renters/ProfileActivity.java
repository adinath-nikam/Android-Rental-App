package com.unknownprogrammer.renters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView username,useremail,userphonenumber;
    ImageView user_profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupToolbar();
        setupViews();
        getUserdata();
        logout();

    }

    private void logout() {
    findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
        }
    });
    }

    private void getUserdata() {

        username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        useremail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//        userphonenumber.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        Glide.with(ProfileActivity.this).load(FirebaseAuth.getInstance().getCurrentUser()
        .getPhotoUrl()).into(user_profile_pic);

    }

    private void setupViews() {

        username = findViewById(R.id.username_id);
        useremail = findViewById(R.id.useremail_id);
        user_profile_pic = findViewById(R.id.user_profile_pic);
        userphonenumber = findViewById(R.id.user_phone_id);

    }

    private void setupToolbar() {

        toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("PROFILE");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
