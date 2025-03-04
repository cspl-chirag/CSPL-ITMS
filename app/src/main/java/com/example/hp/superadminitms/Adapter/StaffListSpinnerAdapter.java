package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.StaffDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class StaffListSpinnerAdapter extends ArrayAdapter<StaffDatum> {
    Context context;
    List<StaffDatum> partsData = new ArrayList<>();
    private TextView tvCourierAgencyName;

    public StaffListSpinnerAdapter(Context context, int textViewResourceId, List<StaffDatum> partsData) {
        super(context, textViewResourceId, partsData);
        this.context = context;
        this.partsData = partsData;
    }


    @Override
    public int getCount() {
        return partsData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_part, viewGroup, false);
        tvCourierAgencyName = v.findViewById(R.id.tvPartName);
        if (partsData.get(i).getStaffCode() != null) {
            tvCourierAgencyName.setText(partsData.get(i).getStaffCode() + " - " + partsData.get(i).getFirstName() + " " + partsData.get(i).getMiddleName() + " " + partsData.get(i).getLastName());
        } else {
            tvCourierAgencyName.setText(partsData.get(i).getFirstName() + " " + partsData.get(i).getMiddleName() + " " + partsData.get(i).getLastName());
        }
        return v;
    }
}
