package com.lingoware.lingow.buswatch.app.beans;


import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Route {
    int id;
    String name;
    List<LatLng> routePoints;
    List<LatLng> unitPoints;
    double serviceScore;
    double unitScore;
    double securityScore;
    double comfortScore;
    double overallScore;

    public Route(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Route() {

    }

    public Route(com.lingoware.lingow.buswatch.common.beans.Route route) {
        this.id = route.getId();
        this.name = route.getName();
        this.serviceScore = route.getServiceScore();
        this.comfortScore = route.getComfortScore();
        this.unitScore = route.getUnitScore();
        this.securityScore = route.getSecurityScore();
        this.overallScore = route.getOverallScore();
        this.routePoints = new ArrayList<LatLng>();
        for (com.lingoware.lingow.buswatch.common.util.LatLng l : route.getRoutePoints()) {
            routePoints.add(new LatLng(l.latitude, l.longitude));
        }
        this.unitPoints = new ArrayList<LatLng>();
        for (com.lingoware.lingow.buswatch.common.util.LatLng l : route.getUnitPoints()) {
            unitPoints.add(new LatLng(l.latitude, l.longitude));
        }
    }

    @Override
    public String toString() {
        return super.toString() + name;
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

    public double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public double getUnitScore() {
        return unitScore;
    }

    public void setUnitScore(double unitScore) {
        this.unitScore = unitScore;
    }

    public double getSecurityScore() {
        return securityScore;
    }

    public void setSecurityScore(double securityScore) {
        this.securityScore = securityScore;
    }

    public double getComfortScore() {
        return comfortScore;
    }

    public void setComfortScore(double comfortScore) {
        this.comfortScore = comfortScore;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }
}
