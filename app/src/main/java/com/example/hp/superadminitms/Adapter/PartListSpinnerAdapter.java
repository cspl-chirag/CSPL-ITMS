package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.WholeListPartDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class PartListSpinnerAdapter extends ArrayAdapter<WholeListPartDatum> {
    Context context;
    List<WholeListPartDatum> partsData = new ArrayList<>();
    private TextView tvCourierAgencyName;

    public PartListSpinnerAdapter(Context context, int textViewResourceId, List<WholeListPartDatum> partsData) {
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
        if (partsData.get(i).getPartCode() != null) {
            tvCourierAgencyName.setText(partsData.get(i).getPartName() + " - " + partsData.get(i).getPartCode());
        } else {
            tvCourierAgencyName.setText(partsData.get(i).getPartName());
        }
        return v;
    }
}
