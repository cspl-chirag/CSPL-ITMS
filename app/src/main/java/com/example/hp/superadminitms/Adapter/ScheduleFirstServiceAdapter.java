package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.FirstScheduleService;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 06-Feb-20.
 */

public class ScheduleFirstServiceAdapter extends RecyclerView.Adapter<ScheduleFirstServiceAdapter.ViewHolder> {
    private final Context context;
    private final List<FirstScheduleService> scheduleServices;

    public ScheduleFirstServiceAdapter(Context context, List<FirstScheduleService> scheduleServices) {
        this.context = context;
        this.scheduleServices = scheduleServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.schedule_service_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(scheduleServices.get(position), position);
    }

    @Override
    public int getItemCount() {
        return scheduleServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvVehicleRegNo;
        private final TextView tvLogDate;
        private final TextView tvFirstServiceKM;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvVehicleRegNo = itemView.findViewById(R.id.tvVehicleRegNo);
            tvVehicleRegNo.setSelected(true);
            tvLogDate = itemView.findViewById(R.id.tvLogDate);
            tvFirstServiceKM = itemView.findViewById(R.id.tvFirstServiceKM);
        }

        public void setData(FirstScheduleService firstScheduleService, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvVehicleRegNo.setText(firstScheduleService.getVehicleName());
            try {
                tvLogDate.setText(Common.convertDateFormat(firstScheduleService.getLogDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvFirstServiceKM.setText(String.valueOf(firstScheduleService.getFirstServiceKm()));
        }
    }
}
