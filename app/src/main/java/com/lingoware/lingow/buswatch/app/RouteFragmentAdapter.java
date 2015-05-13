package com.lingoware.lingow.buswatch.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.lingoware.lingow.buswatch.common.beans.Route;

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

    SparseArray<RouteFragment> registeredFragments = new SparseArray<RouteFragment>();
    private List<DataSetChangesListener> dataSetChangesListeners = new ArrayList<>();


    public RouteFragmentAdapter(FragmentManager fm) {
        super(fm);
        allRoutesFragment = AllRoutesFragment.newInstance();
        add(-1, allRoutesFragment);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    public void addDataSetChangesListener(DataSetChangesListener d) {
        dataSetChangesListeners.add(d);
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
        for (int i = 0; i < routes.size(); i++) {
            add(routes.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RouteFragment fragment = (RouteFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public RouteFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (DataSetChangesListener d : dataSetChangesListeners) {
            d.onDataSetChanged();
        }
    }

    public interface DataSetChangesListener {
        public abstract void onDataSetChanged();
    }
}
