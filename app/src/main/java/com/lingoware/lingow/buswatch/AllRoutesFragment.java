package com.lingoware.lingow.buswatch;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllRoutesFragment extends RouteFragment implements RouteFetcher.RouteUpdateListener {

    private static final String ROUTES = "com.lingoware.lingow.buswatch.AllRoutesFragment.ROUTES";
    RouteItemAdapter routeListadapter;
    ArrayList<Route> routes;

    public AllRoutesFragment() {
        // Required empty public constructor
    }

    public static AllRoutesFragment newInstance() {
        AllRoutesFragment f = new AllRoutesFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(ROUTES, new ArrayList<Parcelable>());
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_all_routes, container, false);

        ListView listView = (ListView) v.findViewById(R.id.routelist_view);
        if (savedInstanceState != null) {
            routes = savedInstanceState.getParcelableArrayList(ROUTES);
        } else {
            routes = getArguments().getParcelableArrayList(ROUTES);
        }
        routeListadapter = new RouteItemAdapter(this.getActivity(), android.R.layout.simple_list_item_1, routes);
        listView.setAdapter(routeListadapter);
        Button btnNewRoute = new Button(getActivity());
        btnNewRoute.setText(R.string.addNewRouteButtonText);
        listView.addFooterView(btnNewRoute);
        btnNewRoute.setId(R.id.btnAddRoute);
        btnNewRoute.setOnClickListener((View.OnClickListener) getActivity());
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void routesUpdated(List<Route> routes) {
        routeListadapter.routesUpdated(routes);
        this.routes.clear();
        this.routes.addAll(routes);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ROUTES, routes);

    }
}
