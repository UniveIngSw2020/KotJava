package com.example.test1;

public class ReciveItem {
    private final Double lat;
    private final Double lng;
    private final String name;
    private final int devices;
    public ReciveItem(Double lat, Double lng, int devices, String name ){
        this.lat = lat;
        this.lng = lng;
        this.devices = devices;
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public int getDevices() {
        return devices;
    }
}
