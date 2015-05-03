package com.lingoware.lingow.buswatch;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
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

    protected Route(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            routePoints = new ArrayList<LatLng>();
            in.readList(routePoints, LatLng.class.getClassLoader());
        } else {
            routePoints = null;
        }
        if (in.readByte() == 0x01) {
            unitPoints = new ArrayList<LatLng>();
            in.readList(unitPoints, LatLng.class.getClassLoader());
        } else {
            unitPoints = null;
        }
        serviceScore = in.readDouble();
        unitScore = in.readDouble();
        securityScore = in.readDouble();
        comfortScore = in.readDouble();
        overallScore = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (routePoints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(routePoints);
        }
        if (unitPoints == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(unitPoints);
        }
        dest.writeDouble(serviceScore);
        dest.writeDouble(unitScore);
        dest.writeDouble(securityScore);
        dest.writeDouble(comfortScore);
        dest.writeDouble(overallScore);
    }

    @Override
    public String toString() {
        return super.toString() + name;
    }
}
