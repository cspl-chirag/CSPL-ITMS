package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.RouteWiseBusReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class RouteWiseBusAdapter extends RecyclerView.Adapter<RouteWiseBusAdapter.ViewHolder> {
    private final Context context;
    private List<String> routeNumber = new ArrayList<>();
    private List<RouteWiseBusReport> routeWiseBusData = new ArrayList<>();
    private RouteWiseBusDataAdapter routeWiseBusDataAdapter;

    public RouteWiseBusAdapter(Context context, List<String> routeNoNameData, List<RouteWiseBusReport> routeWiseBusData) {
        this.context = context;
        this.routeNumber = routeNoNameData;
        this.routeWiseBusData = routeWiseBusData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routewise_reports_routewithrecyler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(routeNumber.get(position));
    }

    @Override
    public int getItemCount() {
        return routeNumber.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRouteNoName;
        private final RecyclerView rvBusLoginData;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRouteNoName = itemView.findViewById(R.id.tvRouteNoName);
            rvBusLoginData = itemView.findViewById(R.id.rvBusLoginData);
            tvRouteNoName.setSelected(true);
        }

        public void setData(String routeNumber) {
            List<RouteWiseBusReport> reportDataList = new ArrayList<>();
            for (RouteWiseBusReport routeWiseBusReport : routeWiseBusData) {
                if (routeWiseBusReport.getRouteNo().equals(routeNumber)) {
                    reportDataList.add(routeWiseBusReport);
                }
            }
            tvRouteNoName.setText(reportDataList.get(0).getRouteNo() + " - " + reportDataList.get(0).getRouteName());
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            if (routeWiseBusData.size() > 0) {
                routeWiseBusDataAdapter = new RouteWiseBusDataAdapter(context, reportDataList);
                rvBusLoginData.setLayoutManager(new GridLayoutManager(context, 1));
                rvBusLoginData.setAdapter(routeWiseBusDataAdapter);
                routeWiseBusDataAdapter.notifyDataSetChanged();
            }

        }
    }
}
