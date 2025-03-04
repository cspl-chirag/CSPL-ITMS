package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.DashboardRouteCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/17/2020.
 */

public class AdpDataListRouteInfoDshbrd extends RecyclerView.Adapter<AdpDataListRouteInfoDshbrd.ViewHolder> {
    private final Context context;
    private List<DashboardRouteCount> routeData = new ArrayList<>();

    public AdpDataListRouteInfoDshbrd(Context context, List<DashboardRouteCount> routeData) {
        this.context = context;
        this.routeData = routeData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_routeinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(routeData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return routeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvYesterdayBusCount;
        private final TextView tvTodayBusCount;
        private final TextView tvRouteName;
        private final TextView tvRouteNo;
        private final TextView tvSrNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvYesterdayBusCount = itemView.findViewById(R.id.tvYesterdayBusCount);
            tvTodayBusCount = itemView.findViewById(R.id.tvTodayBusCount);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvRouteName.setSelected(true);
            tvRouteNo = itemView.findViewById(R.id.tvRouteNo);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
        }

        public void setData(DashboardRouteCount countResponses, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvYesterdayBusCount.setText(String.valueOf(countResponses.getYesterdayOnRoad()));
            tvTodayBusCount.setText(String.valueOf(countResponses.getTodayOnRoad()));
            tvRouteNo.setText(String.valueOf(countResponses.getRouteNo()));
            tvRouteName.setText(String.valueOf(countResponses.getRouteName()));
        }
    }
}
