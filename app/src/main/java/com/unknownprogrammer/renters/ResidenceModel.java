package com.unknownprogrammer.renters;

public class ResidenceModel {

        String residence_address,residence_city,residence_state,residence_country,residence_name,residence_pincode,residence_pricing,time,date,user_Uid,residence_phone,ImgUrl;
        Double latitude,longitude;

    public ResidenceModel(String residence_address, String residence_city, String residence_state, String residence_country, String residence_name, String residence_pincode, String residence_pricing, String time, String date, String user_Uid, String residence_phone, String imgUrl, Double latitude, Double longitude) {
        this.residence_address = residence_address;
        this.residence_city = residence_city;
        this.residence_state = residence_state;
        this.residence_country = residence_country;
        this.residence_name = residence_name;
        this.residence_pincode = residence_pincode;
        this.residence_pricing = residence_pricing;
        this.time = time;
        this.date = date;
        this.user_Uid = user_Uid;
        this.residence_phone = residence_phone;
        ImgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ResidenceModel() {
    }

    public String getResidence_address() {
        return residence_address;
    }

    public void setResidence_address(String residence_address) {
        this.residence_address = residence_address;
    }

    public String getResidence_city() {
        return residence_city;
    }

    public void setResidence_city(String residence_city) {
        this.residence_city = residence_city;
    }

    public String getResidence_state() {
        return residence_state;
    }

    public void setResidence_state(String residence_state) {
        this.residence_state = residence_state;
    }

    public String getResidence_country() {
        return residence_country;
    }

    public void setResidence_country(String residence_country) {
        this.residence_country = residence_country;
    }

    public String getResidence_name() {
        return residence_name;
    }

    public void setResidence_name(String residence_name) {
        this.residence_name = residence_name;
    }

    public String getResidence_pincode() {
        return residence_pincode;
    }

    public void setResidence_pincode(String residence_pincode) {
        this.residence_pincode = residence_pincode;
    }

    public String getResidence_pricing() {
        return residence_pricing;
    }

    public void setResidence_pricing(String residence_pricing) {
        this.residence_pricing = residence_pricing;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_Uid() {
        return user_Uid;
    }

    public void setUser_Uid(String user_Uid) {
        this.user_Uid = user_Uid;
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
