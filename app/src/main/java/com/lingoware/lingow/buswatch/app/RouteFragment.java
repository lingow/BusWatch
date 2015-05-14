package com.lingoware.lingow.buswatch.app;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFragment extends Fragment {


    private static final String RUTA = "com.lingoware.lingow.buswatch.app.RouteFragment.ROUTE";
    private static final String CHECKEDINMODE = "com.lingoware.lingow.buswatch.app.RouteFragment.CHECKEDINMODE";


    int ratingStarsIds[] = {
            R.id.starsRouteRating,
            R.id.comfortStars,
            R.id.conditionStars,
            R.id.serviceStars,
            R.id.overallStars,
            R.id.SecurityStars
    };
    Route route;
    private TextView txtCheckMode;
    private TextView txtReviewTitle;
    private TextView txtReviewMessage;
    private FloatingActionButton fabCheckin;
    private FloatingActionButton fabCheckout;
    private List<RatingBar> ratingBars = new ArrayList<>();
    private boolean checkedInMode = false;

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
        Parcelable p = getArguments().getParcelable(RUTA);
        if (p != null) {
            route = generateNonParcelable((com.lingoware.lingow.buswatch.app.beans.Route) Parcels.unwrap(p));
        }
        txtCheckMode = ((TextView) v.findViewById(R.id.txtCheckIn));
        txtReviewTitle = ((TextView) v.findViewById(R.id.txtReviewTitle));
        txtReviewMessage = ((TextView) v.findViewById(R.id.txtReviewMessage));
        fabCheckin = ((FloatingActionButton) v.findViewById(R.id.btnCheckin));
        fabCheckout = ((FloatingActionButton) v.findViewById(R.id.btnCheckout));
        for (int rbid : ratingStarsIds) {
            ratingBars.add((RatingBar) v.findViewById(rbid));
        }
        initViewContent(v);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (route != null) {
            outState.putParcelable(RUTA,
                    Parcels.wrap(new com.lingoware.lingow.buswatch.app.beans.Route(route)));
        }
        outState.putBoolean(CHECKEDINMODE, checkedInMode);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (route == null) {
                route = savedInstanceState.getParcelable(RUTA);
            }
            checkedInMode = savedInstanceState.getBoolean(CHECKEDINMODE);
        }
        initViewContent(getView());
    }

    private void initViewContent(View v) {
        if (route != null) {
            setupOveralView(v);
            if (checkedInMode) {
                setCheckedInMode();
            } else {
                setCheckedOutMode();

            }
        }
    }

    public void setCheckedOutMode() {
        checkedInMode = false;
        txtCheckMode.setText("CheckIn");
        txtReviewTitle.setText("Route's Rating");
        txtReviewMessage.setText("");
        fabCheckout.setEnabled(false);
        fabCheckout.setVisibility(View.INVISIBLE);
        fabCheckout.invalidate();
        fabCheckin.setEnabled(true);
        fabCheckin.setVisibility(View.VISIBLE);
        fabCheckin.invalidate();
        setRating();
    }

    public void setCheckedInMode() {
        checkedInMode = true;
        txtCheckMode.setText("CheckOut");
        txtReviewTitle.setText("Please Rate the Route");
        txtReviewMessage.setText("Review will be sent upon checkout");
        fabCheckin.setEnabled(false);
        fabCheckin.setVisibility(View.INVISIBLE);
        fabCheckin.invalidate();
        fabCheckout.setEnabled(true);
        fabCheckout.setVisibility(View.VISIBLE);
        fabCheckout.invalidate();
        clearRatings();
    }

    private void clearRatings() {
        for (RatingBar r : ratingBars) {
            if (r.getId() != R.id.starsRouteRating) {
                r.setIsIndicator(false);
                r.setRating(0);
            }
        }
    }

    private void setupOveralView(View v) {
        TextView tv = ((TextView) v.findViewById(R.id.txtRouteName));
        tv.setText(route.getName());
        tv.setTextColor(route.getColor());
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.btnCheckin);
        fab.setOnClickListener((View.OnClickListener) getActivity());
        FloatingActionButton fabCheckout = (FloatingActionButton) v.findViewById(R.id.btnCheckout);
        fabCheckout.setOnClickListener((View.OnClickListener) getActivity());
        fabCheckout.setEnabled(false);
        setupRatingBars();
    }

    private void setupRatingBars() {
        for (RatingBar r : ratingBars) {
            LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.starFullySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.starPartiallySelected), PorterDuff.Mode.SRC_ATOP);
            if (r.getId() != R.id.starsRouteRating) {
                r.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        ratingBar.setRating(rating);
                    }
                });
                stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);

            }
        }
    }

    private void setRating() {
        for (RatingBar r : ratingBars) {
            r.setIsIndicator(true);
            r.setMax(5);
            switch (r.getId()) {
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
    }

    public Map<Integer, Float> getRatings() {
        Map<Integer, Float> ret = new HashMap<>();
        for (RatingBar r : ratingBars) {
            if (r.getId() != R.id.starsRouteRating) {
                ret.put(r.getId(), r.getRating());
            }
        }
        return ret;
    }
}
