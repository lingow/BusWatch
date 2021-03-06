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
    protected List<List<LatLng>> routePaths = new ArrayList<>();
    protected double serviceScore;
    protected double unitScore;
    protected double securityScore;
    protected double comfortScore;
    protected double overallScore;
    protected int color;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<List<LatLng>> getRoutePaths() {
        return routePaths;
    }

    public void setRoutePaths(List<List<LatLng>> routePaths) {
        this.routePaths = routePaths;
    }

    public void addPath(List<LatLng> path) {
        this.routePaths.add(path);
    }
}
