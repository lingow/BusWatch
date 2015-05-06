package com.lingoware.lingow.buswatch.common.beans;

import com.lingoware.lingow.buswatch.common.util.LatLng;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by lingow on 5/05/15.
 */
@XmlRootElement
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
