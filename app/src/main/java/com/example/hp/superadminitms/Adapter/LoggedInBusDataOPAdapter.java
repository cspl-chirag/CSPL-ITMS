package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.LoggedInBusDatum;

import java.util.List;

/**
 * Created by ANDROID-PC on 04/01/2019.
 */

public class LoggedInBusDataOPAdapter extends RecyclerView.Adapter<LoggedInBusDataOPAdapter.LoggedInBusHolder> {

    public static List<LoggedInBusDatum> loggedInBusData;
    public static int selected_item = 0;
    OnItemClickListener mItemClickListener;
    private final Context context;

    public LoggedInBusDataOPAdapter(List<LoggedInBusDatum> loggedInBusData, Context context) {
        LoggedInBusDataOPAdapter.loggedInBusData = loggedInBusData;
        this.context = context;
    }

    @Override
    public LoggedInBusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoggedInBusHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_login, parent, false));
    }

    @Override
    public void onBindViewHolder(LoggedInBusHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvBus.setText(loggedInBusData.get(position).getVehicleCode() + '-' + loggedInBusData.get(position).getVehicleRegNo());
        holder.tvRoute.setText(String.valueOf(loggedInBusData.get(position).getRouteNo()));
//        holder.tvLogsheet.setText(String.valueOf(loggedInBusData.get(position).getLogsheetCode()));
        holder.tvDriver.setText(String.valueOf(loggedInBusData.get(position).getDriverName()));
        holder.tvDriver.setSelected(true);
        holder.tvLoginTime.setText(String.valueOf(loggedInBusData.get(position).getLoginTime()));
//        try {
//            holder.tvLogintime.setText(convertDateFormat(loggedInBusData.get(position).getLoginTime(),"HH:mm:ss","hh:mm a"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return loggedInBusData.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class LoggedInBusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvBus;
        private final TextView tvSrNo;
        private final TextView tvRoute;
        //        private TextView tvLogsheet;
        private final TextView tvDriver;
        private final TextView tvLoginTime;
//        private TextView tvLogintime;

        public LoggedInBusHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvBus = itemView.findViewById(R.id.tvRegistrationNo);
            tvRoute = itemView.findViewById(R.id.tvRoute);
//            tvLogsheet = itemView.findViewById(R.id.tvLogsheet);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvLoginTime = itemView.findViewById(R.id.tvLoginTime);
//            tvLogintime = itemView.findViewById(R.id.tvLoginTime);
            tvBus.setSelected(true);
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
