package com.lingoware.lingow.buswatch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFragmentAdapter extends FragmentStatePagerAdapter implements RouteFetcher.RouteUpdateListener {

    Map<Integer, RouteFragment> routes = new HashMap<Integer, RouteFragment>();
    List<RouteFragment> routesList = new ArrayList<RouteFragment>();
    AllRoutesFragment allRoutesFragment;

    public RouteFragmentAdapter(FragmentManager fm) {
        super(fm);
        allRoutesFragment = AllRoutesFragment.newInstance();
        add(-1, allRoutesFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return routesList.get(position);
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    private boolean add(Route r) {
        if (routes.containsKey(r.getId())) {
            return false;
        }
        return add(r.getId(), RouteFragment.newInstance(r));
    }

    private boolean add(int id, RouteFragment f) {
        routes.put(id, f);
        routesList.add(f);
        return true;
    }

    @Override
    public void routesUpdated(List<Route> routes) {
        allRoutesFragment.routesUpdated(routes);
        for (Route r : routes) {
            add(r);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
