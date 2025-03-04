package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.InsuranceExpDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 30-May-19.
 */

public class InsuranceExpAdapter extends RecyclerView.Adapter<InsuranceExpAdapter.ViewHolder> {
    private final Context context;
    private final List<InsuranceExpDatum> insuranceExpDatumList;

    public InsuranceExpAdapter(Context context, List<InsuranceExpDatum> insuranceExpDatumList) {
        this.context = context;
        this.insuranceExpDatumList = insuranceExpDatumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.licence_exp_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(String.valueOf(insuranceExpDatumList.get(position).getVehicle()));
        try {
            holder.tvEndDate.setText(Common.convertDateFormat(insuranceExpDatumList.get(position).getInsuranceRenDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return insuranceExpDatumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvEndDate;
        private final TextView tvSrNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
        }
    }
}
