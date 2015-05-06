package com.lingoware.lingow.buswatch.server;

import com.lingoware.lingow.buswatch.common.BusWatchService;
import com.lingoware.lingow.buswatch.common.beans.Route;
import com.lingoware.lingow.buswatch.common.util.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by lingow on 5/05/15.
 */
@WebService(name = "BusWatchService",
        endpointInterface = "com.lingoware.lingow.buswatch.common.BusWatchService",
        portName = "BusWatchServicePort",
        serviceName = "BusWatchService")
public class BusWatchServiceImplementor implements BusWatchService {
    @Override
    public List<Route> getRoutes(@WebParam(partName = "position") LatLng position) {
        List<Route> rutas = new ArrayList<Route>();
        rutas.add(new Route(1, "51C"));
        rutas.add(new Route(2, "450"));
        rutas.add(new Route(3, "626"));
        return rutas;
    }
}
