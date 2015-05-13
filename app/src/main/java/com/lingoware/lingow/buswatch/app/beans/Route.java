package com.lingoware.lingow.buswatch.app.beans;


import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Route {

    double overallScore;
    double securityScore;
    double unitScore;
    double comfortScore;
    double serviceScore;
    String name;
    int id;
    List<List<LatLng>> routePaths;
    int color;


    public Route() {
        routePaths = new ArrayList<>();
    }

    public Route(com.lingoware.lingow.buswatch.common.beans.Route route) {
        this();
        this.id = route.getId();
        this.name = route.getName();
        this.serviceScore = route.getServiceScore();
        this.comfortScore = route.getComfortScore();
        this.unitScore = route.getUnitScore();
        this.securityScore = route.getSecurityScore();
        this.overallScore = route.getOverallScore();
        for (List<com.lingoware.lingow.buswatch.common.util.LatLng> latLngList : route.getRoutePaths()) {
            List<LatLng> path = new ArrayList<>();
            for (com.lingoware.lingow.buswatch.common.util.LatLng l : latLngList) {
                path.add(new LatLng(l.latitude, l.longitude));
            }
            addPath(path);
        }

        this.color = route.getColor();
    }

    private void addPath(List<LatLng> path) {
        routePaths.add(path);
    }

    public String toString() {
        return super.toString() + name;
    }


    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getSecurityScore() {
        return securityScore;
    }

    public void setSecurityScore(double securityScore) {
        this.securityScore = securityScore;
    }

    public double getUnitScore() {
        return unitScore;
    }

    public void setUnitScore(double unitScore) {
        this.unitScore = unitScore;
    }

    public double getComfortScore() {
        return comfortScore;
    }

    public void setComfortScore(double comfortScore) {
        this.comfortScore = comfortScore;
    }

    public double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<List<LatLng>> getRoutePaths() {
        return routePaths;
    }

    public void setRoutePaths(List<List<LatLng>> routePaths) {
        this.routePaths = routePaths;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
