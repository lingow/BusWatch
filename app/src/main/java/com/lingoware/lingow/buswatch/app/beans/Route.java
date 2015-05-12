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
    List<LatLng> routePoints;
    List<LatLng> unitPoints;
    int color;


    public Route() {
        routePoints = new ArrayList<>();
        unitPoints = new ArrayList<>();
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
        for (com.lingoware.lingow.buswatch.common.util.LatLng l : route.getRoutePoints()) {
            routePoints.add(new LatLng(l.latitude, l.longitude));
        }
        for (com.lingoware.lingow.buswatch.common.util.LatLng l : route.getUnitPoints()) {
            unitPoints.add(new LatLng(l.latitude, l.longitude));
        }
        this.color = route.getColor();
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

    public List<LatLng> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<LatLng> routePoints) {
        this.routePoints = routePoints;
    }

    public List<LatLng> getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(List<LatLng> unitPoints) {
        this.unitPoints = unitPoints;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
