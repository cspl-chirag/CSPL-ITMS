package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.LoggedOutBusDatum;

import java.util.List;


/**
 * Created by ANDROID-PC on 04/01/2019.
 */

public class LoggedOutBusDataOPAdapter extends RecyclerView.Adapter<LoggedOutBusDataOPAdapter.LoggedOutBusHolder> {

    public static List<LoggedOutBusDatum> loggedOutBusData;
    public static int selected_item = 0;
    OnItemClickListener mItemClickListener;
    private final Context context;

    public LoggedOutBusDataOPAdapter(List<LoggedOutBusDatum> loggedOutBusData, Context context) {
        LoggedOutBusDataOPAdapter.loggedOutBusData = loggedOutBusData;
        this.context = context;
    }

    @Override
    public LoggedOutBusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoggedOutBusHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_logout, parent, false));
    }

    @Override
    public void onBindViewHolder(LoggedOutBusHolder holder, int position) {
        holder.tvSrvNo.setText(String.valueOf(position + 1));
        holder.tvBus.setText(loggedOutBusData.get(position).getVehicleCode() + '-' + loggedOutBusData.get(position).getVehicleRegNo());
        holder.tvRoute.setText(String.valueOf(loggedOutBusData.get(position).getRouteNo()));
//        holder.tvLogsheet.setText(String.valueOf(loggedOutBusData.get(position).getLogsheetCode()));
        holder.tvDriver.setText(String.valueOf(loggedOutBusData.get(position).getDriverName()));
        holder.tvDriver.setSelected(true);
        holder.tvClosingKm.setText(String.valueOf(loggedOutBusData.get(position).getLogOutKm()));
//        try {
//            holder.tvLogouttime.setText(convertDateFormat(loggedOutBusData.get(position).getLogOutTime(),"HH:mm:ss","hh:mm a"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return loggedOutBusData.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class LoggedOutBusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvBus;
        private final TextView tvSrvNo;
        private final TextView tvRoute;
        //        private TextView tvLogsheet;
        private final TextView tvDriver;
        private final TextView tvClosingKm;

        public LoggedOutBusHolder(View itemView) {
            super(itemView);
            tvSrvNo = itemView.findViewById(R.id.tvSrNo);
            tvBus = itemView.findViewById(R.id.tvRegistrationNo);
            tvRoute = itemView.findViewById(R.id.tvRoute);
//            tvLogsheet = itemView.findViewById(R.id.tvLogsheet);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvClosingKm = itemView.findViewById(R.id.tvClosingKm);
//            tvLogouttime = itemView.findViewById(R.id.tvLogoutTime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }

}
