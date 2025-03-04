package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.DepotDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDROID-PC on 20/10/2018.
 */

public class DepotDataOPAdapter extends BaseAdapter {

    Context context;
    List<DepotDatum> depotData = new ArrayList<>();
    private TextView tvDepot;

    public DepotDataOPAdapter(Context context, List<DepotDatum> depotData) {
        this.context = context;
        this.depotData = depotData;
    }

    @Override
    public int getCount() {
        return depotData.size();
    }

    @Override
    public Object getItem(int i) {
        return depotData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_depot, viewGroup, false);
        tvDepot = v.findViewById(R.id.tvDepot);
        tvDepot.setText(depotData.get(i).getDeptName());
        return v;
    }
}
