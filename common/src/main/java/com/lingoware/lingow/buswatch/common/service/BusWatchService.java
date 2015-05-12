package com.lingoware.lingow.buswatch.common.service;

import com.lingoware.lingow.buswatch.common.beans.Route;
import com.lingoware.lingow.buswatch.common.util.LatLng;

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

    public abstract boolean addRoute(String name, LatLng latLng);

}
