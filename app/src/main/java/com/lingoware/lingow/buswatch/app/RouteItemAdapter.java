package com.lingoware.lingow.buswatch.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lingoware.lingow.buswatch.R;
import com.lingoware.lingow.buswatch.common.beans.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteItemAdapter extends BaseAdapter implements RouteFetcher.RouteUpdateListener, ListAdapter {
    List<Route> routes = new ArrayList<Route>();
    Context context;

    public RouteItemAdapter(Context context, List<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @Override
    public void routesUpdated(List<Route> routes) {
        ThreadPreconditions.checkOnMainThread();
        this.routes = routes;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Route getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.layout_route_item, parent, false);

        TextView routeName = (TextView) rootView.findViewById(R.id.txtRouteItemName);
        TextView routeTime = (TextView) rootView.findViewById(R.id.txtWaitTime);

        Route r = getItem(position);
        routeName.setText(r.getName());
        routeName.setTextColor(r.getColor());

        return rootView;
    }

}
