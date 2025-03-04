package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.LogsheetDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDROID-PC on 20/10/2018.
 */

public class LogsheetOPAdapter extends ArrayAdapter<LogsheetDatum> {

    Context context;
    List<LogsheetDatum> logsheetData = new ArrayList<>();
    private TextView tvLogsheet;

    public LogsheetOPAdapter(Context context, int textViewResourceId, List<LogsheetDatum> logsheetData) {
        super(context, textViewResourceId, logsheetData);
        this.context = context;
        this.logsheetData = logsheetData;
    }

    @Override
    public int getCount() {
        return logsheetData.size();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_logsheet, viewGroup, false);
        tvLogsheet = v.findViewById(R.id.tvLogsheet);
        tvLogsheet.setText(logsheetData.get(i).getLogsheetCode());
        return v;
    }
}
