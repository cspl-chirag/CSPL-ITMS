package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.ReportBusWiseAverage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class ReportBuswiseAVGAdapter extends RecyclerView.Adapter<ReportBuswiseAVGAdapter.ViewHolder> {
    private final Context context;
    private List<ReportBusWiseAverage> reportData = new ArrayList<>();

    public ReportBuswiseAVGAdapter(Context context, List<ReportBusWiseAverage> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_avg_data, parent, false));
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
        private final TextView tvRunKm;
        private final TextView tvFuelQuantity;
        private final TextView tvAverage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvBusRegNo = itemView.findViewById(R.id.tvBusRegNo);
            tvRunKm = itemView.findViewById(R.id.tvRunKm);
            tvFuelQuantity = itemView.findViewById(R.id.tvFuelQuantity);
            tvAverage = itemView.findViewById(R.id.tvAverage);
        }

        public void setData(ReportBusWiseAverage reportBusWiseAverage, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvBusRegNo.setText(reportBusWiseAverage.getVehicleCode() + "-" + reportBusWiseAverage.getVehicleRegNo());
            tvRunKm.setText(String.valueOf(reportBusWiseAverage.getTotalRunKM()));
            tvFuelQuantity.setText(String.valueOf(reportBusWiseAverage.getTotalFuel()));
            tvAverage.setText(String.valueOf(reportBusWiseAverage.getAverage()));
            if (reportBusWiseAverage.getAverage() <= 5) {
                tvAverage.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLight));
                tvAverage.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else {
                tvAverage.setBackgroundColor(context.getResources().getColor(R.color.colorTransparent));
                tvAverage.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }

        }
    }
}
