package com.lingoware.lingow.buswatch.app;

import com.lingoware.lingow.buswatch.common.service.BusWatchService;
import com.lingoware.lingow.buswatch.server.MockService;

/**
 * Created by lingow on 10/05/15.
 */
public class BuswatchServiceHolder {

    static BuswatchServiceHolder buswatchServiceHolder;
    BusWatchService service;

    private BuswatchServiceHolder() {
    }

    public static BuswatchServiceHolder getInstance() {
        if (buswatchServiceHolder == null) {
            buswatchServiceHolder = new BuswatchServiceHolder();
        }
        return buswatchServiceHolder;
    }

    public BusWatchService getService() {
        if (service == null) {
            initService();
        }
        return service;
    }

    private void initService() {
        //TODO Aqui es donde hay que crear la conexion al servicio. Por el momento solo usaremos MockService
        this.service = new MockService();
        /*this.service = new BusWatchService() {
            @Override
            public List<Route> getRoutes(LatLng position, double range) {
                List<Route> l= new ArrayList<>();
                l.add( new Route(1,"Test",5,5,5,5,5));
                return l;
            }
        };*/
    }

}
