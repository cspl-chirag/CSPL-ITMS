package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.BusDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class VehicleSpinnerAdapter extends ArrayAdapter<BusDatum> {

    Context context;
    List<BusDatum> busData = new ArrayList<>();
    private TextView tvBus;

    public VehicleSpinnerAdapter(Context context, int textViewResourceId, List<BusDatum> busData) {
        super(context, textViewResourceId, busData);
        this.context = context;
        this.busData = busData;
    }


    @Override
    public int getCount() {
        return busData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_vehicle, viewGroup, false);
        tvBus = v.findViewById(R.id.tvVehicleRegNo);
        tvBus.setText(busData.get(i).getVehicleCode() + "-" + busData.get(i).getVehicleRegNo());
        return v;
    }
}
