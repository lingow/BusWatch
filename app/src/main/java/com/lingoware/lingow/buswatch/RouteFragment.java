package com.lingoware.lingow.buswatch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFragment extends Fragment {


    private static final String RUTA = "com.lingoware.lingow.buswatch.RouteFragment.ROUTE";

    public RouteFragment() {

    }

    public static RouteFragment newInstance(Route r) {
        RouteFragment f = new RouteFragment();
        Bundle b = new Bundle();
        b.putParcelable(RUTA, r);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_route, container, false);
        Route r;
        r = getArguments().getParcelable(RUTA);
        if (r == null) {
            r = savedInstanceState.getParcelable(RUTA);
        }
        if (r != null) {
            /*TODO Realmente rellenar un template con la informacion de la ruta */
            ((TextView) v.findViewById(R.id.txtRouteName)).setText(r.getName());
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.btnCheckin);
            fab.setOnClickListener((View.OnClickListener) getActivity());
        }
        return v;
    }
}
