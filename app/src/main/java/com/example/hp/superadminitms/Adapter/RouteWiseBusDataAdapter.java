package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.RouteWiseBusReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

class RouteWiseBusDataAdapter extends RecyclerView.Adapter<RouteWiseBusDataAdapter.ViewHolder> {
    private final Context context;
    private List<RouteWiseBusReport> reportData = new ArrayList<>();

    public RouteWiseBusDataAdapter(Context context, List<RouteWiseBusReport> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routewise_bus_report_data, parent, false);
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
        private final TextView tvVehicleRegNo;
        private final TextView tvLoginTime;
        private final TextView tvOpeningKm;
        private final TextView tvDriver;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvVehicleRegNo = itemView.findViewById(R.id.tvVehicleRegNo);
            tvLoginTime = itemView.findViewById(R.id.tvLoginTime);
            tvOpeningKm = itemView.findViewById(R.id.tvOpeningKm);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvVehicleRegNo.setSelected(true);
            tvDriver.setSelected(true);
        }

        public void setData(RouteWiseBusReport routeWiseBusReport, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvVehicleRegNo.setText(routeWiseBusReport.getVehicleCode() + "-" + routeWiseBusReport.getVehicleRegNo());
            tvLoginTime.setText(String.valueOf(routeWiseBusReport.getLoginTime()));
            tvOpeningKm.setText(String.valueOf(routeWiseBusReport.getOpeningKm()));
            tvDriver.setText(String.valueOf(routeWiseBusReport.getDriverName()));

        }
    }

}
