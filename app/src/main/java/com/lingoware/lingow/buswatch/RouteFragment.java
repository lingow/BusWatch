package com.lingoware.lingow.buswatch;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFragment extends Fragment {


    private static final String RUTA = "com.lingoware.lingow.buswatch.RouteFragment.ROUTE";
    private static final String COLOR = "com.lingoware.lingow.buswatch.RouteFragment.COLOR";

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

    public static RouteFragment newInstance(Route r, int c) {
        RouteFragment f = new RouteFragment();
        Bundle b = new Bundle();
        b.putParcelable(RUTA, r);
        b.putInt(COLOR, c);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_route, container, false);
        Route r;
        int color = Color.BLACK;
        color = getArguments().getInt(COLOR);
        r = getArguments().getParcelable(RUTA);
        if (r == null) {
            r = savedInstanceState.getParcelable(RUTA);
        }
        if (r != null) {
            TextView tv = ((TextView) v.findViewById(R.id.txtRouteName));
            tv.setText(r.getName());
            tv.setTextColor(color);
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.btnCheckin);
            fab.setOnClickListener((View.OnClickListener) getActivity());
            FloatingActionButton fabCheckout = (FloatingActionButton) v.findViewById(R.id.btnCheckout);
            fabCheckout.setOnClickListener((View.OnClickListener) getActivity());
            fabCheckout.setEnabled(false);
            fabCheckout.setVisibility(View.INVISIBLE);

            //TODO todavia falta llenar las estrellas con la informacion correspondiente
            setupRatingBars(v);
        }
        return v;
    }

    private void setupRatingBars(View v) {
        for (int rid : ratingStarsIds) {
            RatingBar r = (RatingBar) v.findViewById(rid);
            LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.starFullySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.starPartiallySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);
            r.setIsIndicator(true);
        }
    }

    public void setModificableRatingBars(View v, boolean modificable) {
        for (int rid : ratingStarsIds) {
            RatingBar r = (RatingBar) v.findViewById(rid);
            r.setIsIndicator(!modificable);
        }
    }
}
