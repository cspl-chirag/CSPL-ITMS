package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.PartStoreDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class PartStoreListSpinnerAdapter extends ArrayAdapter<PartStoreDatum> {
    Context context;
    List<PartStoreDatum> storeData = new ArrayList<>();
    private TextView tvCourierAgencyName;

    public PartStoreListSpinnerAdapter(Context context, int textViewResourceId, List<PartStoreDatum> partsData) {
        super(context, textViewResourceId, partsData);
        this.context = context;
        this.storeData = partsData;
    }


    @Override
    public int getCount() {
        return storeData.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.st_spinner_part, viewGroup, false);
        tvCourierAgencyName = v.findViewById(R.id.tvPartName);

        tvCourierAgencyName.setText(storeData.get(i).getStoreName());
        return v;
    }
}
