package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.DriverDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class AdpSpinnerDriver extends ArrayAdapter<DriverDatum> {
    Context context;
    List<DriverDatum> driverData = new ArrayList<>();
    private TextView tvRouteNo;

    public AdpSpinnerDriver(Context context, int textViewResourceId, List<DriverDatum> driverData) {
        super(context, textViewResourceId, driverData);
        this.context = context;
        this.driverData = driverData;
    }


    @Override
    public int getCount() {
        return driverData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_driver, viewGroup, false);
        tvRouteNo = v.findViewById(R.id.tvDriver);
        tvRouteNo.setText(driverData.get(i).getFirstName() + " " + driverData.get(i).getMiddleName() + " " + driverData.get(i).getLastName());
        return v;
    }
}
