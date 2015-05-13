package com.lingoware.lingow.buswatch.app;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;
import com.lingoware.lingow.buswatch.R;
import com.lingoware.lingow.buswatch.common.beans.Route;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFragment extends Fragment {


    private static final String RUTA = "com.lingoware.lingow.buswatch.app.RouteFragment.ROUTE";

    int ratingStarsIds[] = {
            R.id.starsRouteRating,
            R.id.comfortStars,
            R.id.conditionStars,
            R.id.serviceStars,
            R.id.overallStars,
            R.id.SecurityStars
    };

    public RouteFragment() {

    }

    public static RouteFragment newInstance(Route r) {
        RouteFragment f = new RouteFragment();
        Bundle b = new Bundle();
        b.putParcelable(RUTA, Parcels.wrap(new com.lingoware.lingow.buswatch.app.beans.Route(r)));
        f.setArguments(b);
        return f;
    }

    public static Route generateNonParcelable(com.lingoware.lingow.buswatch.app.beans.Route route) {
        Route r =
                new Route(
                        route.getId(), route.getName(), route.getServiceScore(), route.getSecurityScore(), route.getUnitScore(),
                        route.getComfortScore(), route.getOverallScore(), route.getColor());
        int i = 0;
        for (List<LatLng> latLngList : route.getRoutePaths()) {
            List<com.lingoware.lingow.buswatch.common.util.LatLng> path = new ArrayList<>();
            for (LatLng l : latLngList) {
                path.add(new com.lingoware.lingow.buswatch.common.util.LatLng(l.latitude, l.longitude));
            }
            r.addPath(path);
        }

        return r;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_route, container, false);
        Route r;
        r = generateNonParcelable(
                (com.lingoware.lingow.buswatch.app.beans.Route) Parcels.unwrap(getArguments().getParcelable(RUTA)));
        if (r == null) {
            r = savedInstanceState.getParcelable(RUTA);
        }
        if (r != null) {
            TextView tv = ((TextView) v.findViewById(R.id.txtRouteName));
            tv.setText(r.getName());
            tv.setTextColor(r.getColor());
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.btnCheckin);
            fab.setOnClickListener((View.OnClickListener) getActivity());
            FloatingActionButton fabCheckout = (FloatingActionButton) v.findViewById(R.id.btnCheckout);
            fabCheckout.setOnClickListener((View.OnClickListener) getActivity());
            fabCheckout.setEnabled(false);
            fabCheckout.setVisibility(View.INVISIBLE);

            setupRatingBars(v, r);
        }
        return v;
    }

    private void setupRatingBars(View v, Route route) {
        for (int rid : ratingStarsIds) {
            RatingBar r = (RatingBar) v.findViewById(rid);
            LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.starFullySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.starPartiallySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);
            setRating(r, rid, route);
        }
    }

    private void setRating(RatingBar r, int rid, Route route) {
        r.setIsIndicator(true);
        r.setMax(5);
        switch (rid) {
            case R.id.starsRouteRating:
                r.setRating((float) route.getOverallScore());
                break;
            case R.id.comfortStars:
                r.setRating((float) route.getComfortScore());
                break;
            case R.id.conditionStars:
                r.setRating((float) route.getUnitScore());
                break;
            case R.id.serviceStars:
                r.setRating((float) route.getServiceScore());
                break;
            case R.id.overallStars:
                r.setRating((float) route.getOverallScore());
                break;
            case R.id.SecurityStars:
                r.setRating((float) route.getSecurityScore());
                break;
            default:
                Log.wtf("WTF", "Not an invalid star rating ID was passed to this method. " +
                        "Exiting without doing anything");
        }
    }

    public void setModificableRatingBars(View v, boolean modificable) {
        for (int rid : ratingStarsIds) {
            RatingBar r = (RatingBar) v.findViewById(rid);
            r.setIsIndicator(!modificable);
        }
    }
}
