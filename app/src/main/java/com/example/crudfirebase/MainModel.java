package com.example.crudfirebase;

public class MainModel {

    String Version, Motor, Color, Marca, imgURL;

    public MainModel(){

    }

    public MainModel(String version, String motor, String color, String marca, String imgURL) {
        Version = version;
        Motor = motor;
        Color = color;
        Marca = marca;
        this.imgURL = imgURL;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getMotor() {
        return Motor;
    }

    public void setMotor(String motor) {
        Motor = motor;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
