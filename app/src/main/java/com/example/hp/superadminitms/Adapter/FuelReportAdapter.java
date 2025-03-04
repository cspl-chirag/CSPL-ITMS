package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.FuelReportDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/8/2020.
 */

public class FuelReportAdapter extends RecyclerView.Adapter<FuelReportAdapter.ViewHolder> {
    private final Context context;
    private List<FuelReportDatum> reportData = new ArrayList<>();

    public FuelReportAdapter(Context context, List<FuelReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fuel_report_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(reportData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reportData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvBusRegNo;
        private final TextView tvKm;
        private final TextView tvQuantity;
        private final TextView tvDriver;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvBusRegNo = itemView.findViewById(R.id.tvBusRegNo);
            tvBusRegNo.setSelected(true);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDriver.setSelected(true);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvKm = itemView.findViewById(R.id.tvKm);
        }

        public void setData(FuelReportDatum reportDatum, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvBusRegNo.setText(reportDatum.getVehicleCode() + "-" + reportDatum.getVehicleRegNo());
            tvDriver.setText(reportDatum.getDriverName());
            tvQuantity.setText(String.valueOf(reportDatum.getQuantity()));
            tvKm.setText(String.valueOf(reportDatum.getKilometre()));
        }
    }
}
