package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.ShiftDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDROID-PC on 20/11/2018.
 */

public class ShiftDataOPAdapter extends BaseAdapter {

    Context context;
    List<ShiftDatum> shiftList = new ArrayList<>();
    private TextView tvFuelType;


    public ShiftDataOPAdapter(Context context, List<ShiftDatum> shiftList) {
        this.context = context;
        this.shiftList = shiftList;
    }


    @Override
    public int getCount() {
        return shiftList.size();
    }

    @Override
    public Object getItem(int i) {
        return shiftList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_shift, viewGroup, false);
        tvFuelType = v.findViewById(R.id.tvShift);
        tvFuelType.setText(shiftList.get(i).getShiftName());
        return v;
    }

//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        LinearLayout lv = (LinearLayout) super.getDropDownView(position,convertView,parent);
//        lv.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_background));
//        return lv;
//    }
}
