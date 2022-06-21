package com.unknownprogrammer.renters;

public class add_residence_model {
    String Residence_name,Residence_address,Residence_city, Residence_pincode,Residence_country,Residence_state,Residence_pricing,User_Uid,Time, Date,residence_phone,ImgUrl;
    Double latitude,longitude;

    public add_residence_model(String residence_name, String residence_address, String residence_city, String residence_pincode, String residence_country, String residence_state, String residence_pricing, String user_Uid, String time, String date, String residence_phone, String imgUrl, Double latitude, Double longitude) {
        Residence_name = residence_name;
        Residence_address = residence_address;
        Residence_city = residence_city;
        Residence_pincode = residence_pincode;
        Residence_country = residence_country;
        Residence_state = residence_state;
        Residence_pricing = residence_pricing;
        User_Uid = user_Uid;
        Time = time;
        Date = date;
        this.residence_phone = residence_phone;
        ImgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public add_residence_model() {
    }

    public String getResidence_name() {
        return Residence_name;
    }

    public void setResidence_name(String residence_name) {
        Residence_name = residence_name;
    }

    public String getResidence_address() {
        return Residence_address;
    }

    public void setResidence_address(String residence_address) {
        Residence_address = residence_address;
    }

    public String getResidence_city() {
        return Residence_city;
    }

    public void setResidence_city(String residence_city) {
        Residence_city = residence_city;
    }

    public String getResidence_pincode() {
        return Residence_pincode;
    }

    public void setResidence_pincode(String residence_pincode) {
        Residence_pincode = residence_pincode;
    }

    public String getResidence_country() {
        return Residence_country;
    }

    public void setResidence_country(String residence_country) {
        Residence_country = residence_country;
    }

    public String getResidence_state() {
        return Residence_state;
    }

    public void setResidence_state(String residence_state) {
        Residence_state = residence_state;
    }

    public String getResidence_pricing() {
        return Residence_pricing;
    }

    public void setResidence_pricing(String residence_pricing) {
        Residence_pricing = residence_pricing;
    }

    public String getUser_Uid() {
        return User_Uid;
    }

    public void setUser_Uid(String user_Uid) {
        User_Uid = user_Uid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getResidence_phone() {
        return residence_phone;
    }

    public void setResidence_phone(String residence_phone) {
        this.residence_phone = residence_phone;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
