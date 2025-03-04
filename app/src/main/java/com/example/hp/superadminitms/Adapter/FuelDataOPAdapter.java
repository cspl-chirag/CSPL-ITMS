package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.FuelTypeDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDROID-PC on 20/11/2018.
 */

public class FuelDataOPAdapter extends BaseAdapter {

    Context context;
    List<FuelTypeDatum> fuelList = new ArrayList<>();
    private TextView tvFuelType;


    public FuelDataOPAdapter(Context context, List<FuelTypeDatum> fuelList) {
        this.context = context;
        this.fuelList = fuelList;
    }


    @Override
    public int getCount() {
        return fuelList.size();
    }

    @Override
    public Object getItem(int i) {
        return fuelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_fuel, viewGroup, false);
        tvFuelType = v.findViewById(R.id.tvFuelType);
        tvFuelType.setText(fuelList.get(i).getFuelType());
        return v;
    }

//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        LinearLayout lv = (LinearLayout) super.getDropDownView(position,convertView,parent);
//        lv.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_background));
//        return lv;
//    }
}
