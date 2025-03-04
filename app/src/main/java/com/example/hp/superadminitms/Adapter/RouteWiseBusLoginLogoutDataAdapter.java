package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.RouteWiseBusLogInLogOutReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

class RouteWiseBusLoginLogoutDataAdapter extends RecyclerView.Adapter<RouteWiseBusLoginLogoutDataAdapter.ViewHolder> {
    private final Context context;
    private List<RouteWiseBusLogInLogOutReport> reportDataList = new ArrayList<>();

    public RouteWiseBusLoginLogoutDataAdapter(Context context, List<RouteWiseBusLogInLogOutReport> reportDataList) {
        this.context = context;
        this.reportDataList = reportDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.route_wise_bus_loginlogout_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(reportDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reportDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvVehicleRegNo;
        private final TextView tvRunKm;
        private final TextView tvOpeningKm;
        private final TextView tvClosingKm;
        private final TextView tvTripDone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvVehicleRegNo = itemView.findViewById(R.id.tvVehicleRegNo);
            tvVehicleRegNo.setSelected(true);
            tvRunKm = itemView.findViewById(R.id.tvRunKm);
            tvOpeningKm = itemView.findViewById(R.id.tvOpeningKm);
            tvClosingKm = itemView.findViewById(R.id.tvClosingKm);
            tvTripDone = itemView.findViewById(R.id.tvTripDone);
        }

        public void setData(RouteWiseBusLogInLogOutReport reportdata, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvVehicleRegNo.setText(reportdata.getVehicleCode() + "-" + reportdata.getVehicleRegNo());
            tvRunKm.setText(String.valueOf(reportdata.getRunKm()));
            tvOpeningKm.setText(String.valueOf(reportdata.getOpeningKm()));
            tvClosingKm.setText(String.valueOf(reportdata.getClosingKm()));
            tvTripDone.setText(String.valueOf(reportdata.getActualTrip()));
        }
    }
}
