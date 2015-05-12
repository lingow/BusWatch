package com.lingoware.lingow.buswatch.common.beans;

import com.lingoware.lingow.buswatch.common.util.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by lingow on 5/05/15.
 */
@XmlRootElement
public class Route {
    protected int id;
    protected String name;
    protected List<LatLng> routePoints = new ArrayList<>();
    protected double serviceScore;
    protected double unitScore;
    protected double securityScore;
    protected double comfortScore;
    protected double overallScore;
    protected int color;
    List<LatLng> unitPoints = new ArrayList<>();

    public Route(int id, String name, double serviceScore, double securityScore, double unitScore, double comfortScore, double overallScore, int color) {
        this();
        this.id = id;
        this.name = name;
        this.serviceScore = serviceScore;
        this.securityScore = securityScore;
        this.unitScore = unitScore;
        this.comfortScore = comfortScore;
        this.overallScore = overallScore;
        this.color = color;
    }

    public Route() {

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

    public void addRoutePoint(double lat, double lon, int pos) {
        routePoints.add(pos, new LatLng(lat, lon));
    }

    public void addUnitPoint(double latitude, double longitude) {
        unitPoints.add(new LatLng(latitude, longitude));
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
