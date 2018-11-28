package com.company;

public class Coord {
    private double latitude;
    private double longitude;

    public Coord () {

    }

    public Coord(Coord coord) {
        this.latitude = coord.getLatitude();
        this.longitude = coord.getLongitude();
    }

    public Coord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                "} \n";
    }
}
