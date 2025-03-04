package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.RouteDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class RouteSpinnerAdapter extends ArrayAdapter<RouteDatum> {
    Context context;
    List<RouteDatum> routeData = new ArrayList<>();
    private TextView tvRouteNo;

    public RouteSpinnerAdapter(Context context, int textViewResourceId, List<RouteDatum> routeData) {
        super(context, textViewResourceId, routeData);
        this.context = context;
        this.routeData = routeData;
    }


    @Override
    public int getCount() {
        return routeData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_route, viewGroup, false);
        tvRouteNo = v.findViewById(R.id.tvRouteNo);
        tvRouteNo.setText(routeData.get(i).getRouteNo());
        return v;
    }
}
