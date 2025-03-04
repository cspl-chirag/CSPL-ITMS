package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.BusLogInLogOutReportDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/8/2020.
 */

public class ReportBusLoginLogoutAdapter extends RecyclerView.Adapter<ReportBusLoginLogoutAdapter.ViewHolder> {
    private final Context context;
    private List<BusLogInLogOutReportDatum> reportData = new ArrayList<>();

    public ReportBusLoginLogoutAdapter(Context context, List<BusLogInLogOutReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bus_login_logout_report_list, parent, false);
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
        private final TextView tvOpeningKm;
        private final TextView tvClosingKm;
        private final TextView tvRunKm;
        private final TextView tvRouteNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvBusRegNo = itemView.findViewById(R.id.tvBusRegNo);
            tvBusRegNo.setSelected(true);
            tvOpeningKm = itemView.findViewById(R.id.tvOpeningKm);
            tvClosingKm = itemView.findViewById(R.id.tvClosingKm);
            tvRunKm = itemView.findViewById(R.id.tvRunKm);
            tvRouteNo = itemView.findViewById(R.id.tvRouteNo);
        }

        public void setData(BusLogInLogOutReportDatum reportDatum, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvRouteNo.setText(String.valueOf(reportDatum.getRouteNo()));
            tvRunKm.setText(String.valueOf(reportDatum.getRunKm()));
            if (reportDatum.getRunKm() >= 200) {
                tvRunKm.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLight));
//                tvRunKm.setTextColor(context.getResources().getColor(R.color.colorAccentText));
                tvRunKm.setTextColor(context.getResources().getColor(R.color.colorAccent));

            } else {
                tvRunKm.setBackgroundColor(context.getResources().getColor(R.color.colorTransparent));
                tvRunKm.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            tvClosingKm.setText(String.valueOf(reportDatum.getClosingKm()));
            tvOpeningKm.setText(String.valueOf(reportDatum.getOpeningKm()));
            tvBusRegNo.setText(reportDatum.getVehicleCode() + "-" + reportDatum.getVehicleRegNo());
        }
    }
}
