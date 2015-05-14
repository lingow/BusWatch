package com.lingoware.lingow.buswatch.common.service;

import com.lingoware.lingow.buswatch.common.beans.Route;
import com.lingoware.lingow.buswatch.common.util.LatLng;

import java.util.HashMap;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by lingow on 5/05/15.
 */

@WebService(name = "BusWatchService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface BusWatchService {

    @WebMethod(action = "getRoutes", operationName = "getRoutes")
    @WebResult(name = "routeList")
    public abstract List<Route> getRoutes(
            @WebParam(partName = "position") LatLng position, double range);

    @WebMethod(action = "addRoute", operationName = "addRoute")
    public abstract boolean addRoute(String name, LatLng latLng);

    @WebMethod(action = "rateRoute", operationName = "rateRoute")
    public abstract boolean rateRoute(int routeId, double comfortScore, double securityScore,
                                      double serviceScore, double unitScore, double overallScore);

    @WebMethod(action = "getScores", operationName = "getScores")
    @WebResult(name = "scoresMap")
    public abstract HashMap<String,Double> calculateScores(int routeId);

    @WebMethod(action = "checkin", operationName = "checkin")
    @WebResult(name = "checkinId")
    public abstract int checkin(int routeId, LatLng latlng);

    @WebMethod(action = "checkout", operationName = "checkout")
    public abstract boolean checkout(int checkinId, double securityScore, double serviceScore,
                                     double comfortScore, double overalScore, double conditionScore);

    @WebMethod(action = "getUnitPoints", operationName = "getUnitPoints")
    @WebResult(name = "unitPoints")
    public abstract List<LatLng> getUnitPoints(int routeId);

    @WebMethod(action = "receiveUpdate", operationName = "receiveUpdate")
    public abstract boolean receiveUpdate(int checkinId, LatLng latlng);

}
