package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.CourierAgencyDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class SpinnerCourierAgencyAdapter extends ArrayAdapter<CourierAgencyDatum> {
    Context context;
    List<CourierAgencyDatum> courierAgencyData = new ArrayList<>();
    private TextView tvCourierAgencyName;

    public SpinnerCourierAgencyAdapter(Context context, int textViewResourceId, List<CourierAgencyDatum> courierAgencyData) {
        super(context, textViewResourceId, courierAgencyData);
        this.context = context;
        this.courierAgencyData = courierAgencyData;
    }


    @Override
    public int getCount() {
        return courierAgencyData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_courier_agency, viewGroup, false);
        tvCourierAgencyName = v.findViewById(R.id.tvCourierAgencyName);
        tvCourierAgencyName.setText(courierAgencyData.get(i).getCourierAgencyName() + " ");
        return v;
    }
}
