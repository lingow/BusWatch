package com.lingoware.lingow.buswatch.server;

import com.lingoware.lingow.buswatch.common.beans.Route;
import com.lingoware.lingow.buswatch.common.service.BusWatchService;
import com.lingoware.lingow.buswatch.common.util.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusWatchServiceImplementor implements BusWatchService {
    private final double EARTH_RADIUS_KILOMETERS = 6367.45;
    protected Map<Integer, Route> rutas = new HashMap<>();
    protected ArrayList<Integer> checkins = new ArrayList<>();
    protected HashMap<Integer, LatLng> unitPoints = new HashMap<>();
    protected HashMap<Integer, Integer> routeSession = new HashMap<>();
    protected HashMap<Integer, List<LatLng>> updates = new HashMap<>(); // This can be renamed to checkinLocation
    protected HashMap<Integer, Integer> activeRoutes = new HashMap<>();
    protected int checkinId = 0;
    protected int routeId = 0;

    @Override
    public List<Route> getRoutes(LatLng position, double range) {
        /* Get every route within range in meters */
        List<Route> ret = new ArrayList<>();
        for (Route r : rutas.values()) {
            if (inRouteRange(range, position, r)) {
                ret.add(r);
            }
        }
        return ret;
    }

    @Override
    public boolean addRoute(String name, LatLng startingPoint) {
        Route route = new Route();
        List<LatLng> routePoints = new ArrayList<>();
        routePoints.add(startingPoint);
        route.setName(name);
        route.setId(routeId);
        route.setComfortScore(0);
        route.setSecurityScore(0);
        route.setServiceScore(0);
        route.setUnitScore(0);
        route.setOverallScore(0);
        rutas.put(routeId, route);
        routeId++;
        return true;
    }

    @Override
    public List<LatLng> getUnitPoints(int routeId) {
        List<LatLng> up = new ArrayList<>();
        for (Map.Entry<Integer,LatLng> unitPoint : unitPoints.entrySet()) {
            if(unitPoint.getKey() == routeId) {
                up.add(unitPoint.getValue());
            }
        }
        return up;
    }

    @Override
    public boolean receiveUpdate(int checkinId, List<LatLng> latlng) {
        updates.put(checkinId, latlng);
        return true;
    }

    @Override
    public List<Integer> getActiveRoutes(int routeId){
        List<Integer> checkinList = new ArrayList<>();
        for (Map.Entry<Integer,Integer> route : activeRoutes.entrySet()) {
            if(route.getKey() == routeId) {
                checkinList.add(routeId);
            }
        }
        return checkinList;
    }

    private boolean inRouteRange(double range, LatLng position, Route r) {
        for (List<LatLng> latLngList : r.getRoutePaths()) {
            for (LatLng latLng : latLngList) {
                if (range > distanceInMeters(position, latLng)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pointInRangeOfLine(double range, LatLng P, LatLng A, LatLng B) {
        double cte = crossTrackError(A, B, P);
        return range > cte;
    }

    private double crossTrackError(LatLng a, LatLng b, LatLng p) {
        double angulardistance_AP = angularDistance(a, p);
        double bearing_AB = initialBearing(a, b);
        double bearing_AP = initialBearing(a, p);
        return Math.asin(Math.sin(angulardistance_AP) * Math.sin(bearing_AP - bearing_AB)) * EARTH_RADIUS_KILOMETERS * 1000;
    }

    private double initialBearing(LatLng a, LatLng b) {
        double y = Math.sin(b.longitude - a.longitude) * Math.cos(b.latitude);
        double x = Math.cos(a.latitude) * Math.sin(b.latitude) -
                Math.sin(a.latitude) * Math.cos(a.latitude) * Math.cos(b.longitude - b.longitude);
        return Math.atan2(y, x);
    }

    private double angularDistance(LatLng a, LatLng b) {
        double latitud_a_rad = toRadians(a.latitude);
        double latitude_b_rad = toRadians(b.latitude);
        double delta_lat_rad = toRadians(b.latitude - a.latitude);
        double delta_long_rad = toRadians(b.latitude - a.latitude);

        double tmp = Math.sin(delta_long_rad / 2) * Math.sin(delta_long_rad / 2) +
                Math.cos(latitud_a_rad) * Math.cos(latitude_b_rad) *
                        Math.sin(delta_long_rad / 2) * Math.sin(delta_long_rad / 2);
        return 2 * Math.atan2(Math.sqrt(tmp), Math.sqrt(1 - tmp));
    }

    private double toRadians(double a) {
        return a * Math.PI / 180;
    }

    private double distanceInMeters(LatLng A, LatLng B) {
        double earthRadius = EARTH_RADIUS_KILOMETERS;
        double latDiff = Math.toRadians(A.latitude - B.latitude);
        double lngDiff = Math.toRadians(A.longitude - B.longitude);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(A.latitude)) * Math.cos(Math.toRadians(B.latitude)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1000;

        return new Float(distance * meterConversion).floatValue();
    }

    @Override
    public int checkin(int routeId, LatLng latlng) {
        checkinId++;
        checkins.add(checkinId);
        routeSession.put(checkinId, routeId);
        activeRoutes.put(routeId,checkinId);
        List<LatLng> checkinLocation = new ArrayList<>();
        checkinLocation.add(latlng);
        updates.put(checkinId, checkinLocation);

        return checkinId;
    }

    @Override
    public boolean checkout(int checkinId) {
        checkins.remove(checkinId);
        routeSession.remove(checkinId);
        activeRoutes.remove(checkinId);
        return true;
    }

    @Override
    public boolean rateRoute(int routeId, double comfort, double security, double service, double unit,
                             double overall) {
        Route route = rutas.get(routeId);
        route.setComfortScore(comfort);
        route.setSecurityScore(security);
        route.setServiceScore(service);
        route.setUnitScore(unit);
        route.setOverallScore(overall);
        return true;
    }

    @Override
    public HashMap<String,Double> calculateScores(int routeId)  {
        Route route = rutas.get(routeId);
        HashMap<String, Double> scores = new HashMap<>();
        double comfort = route.getComfortScore();
        double overall = route.getOverallScore();
        double security = route.getSecurityScore();
        double service = route.getServiceScore();
        double unit = route.getUnitScore();
        scores.put("comfort", comfort);
        scores.put("security", security);
        scores.put("service", service);
        scores.put("unit", unit);
        scores.put("overall", overall);
        return scores;
    }


}
