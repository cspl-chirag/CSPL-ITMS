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
import com.example.hp.superadminitms.Model.AccidentReportDateWise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class HistoryAccidentDataAdapter extends RecyclerView.Adapter<HistoryAccidentDataAdapter.ViewHolder> {
    private final Context context;
    private List<AccidentReportDateWise> reportData = new ArrayList<>();

    public HistoryAccidentDataAdapter(Context context, List<AccidentReportDateWise> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_accident_datewise, parent, false));
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
        private final TextView tvDate;
        private final TextView tvDriver;
        private final TextView tvLocation;
        private final TextView tvDescription;
        private final TextView tvBusNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvBusNo = itemView.findViewById(R.id.tvBusNo);
            tvDriver.setSelected(true);
            tvLocation.setSelected(true);
            tvDescription.setSelected(true);
        }

        public void setData(AccidentReportDateWise accidentReportDateWise, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvBusNo.setText(accidentReportDateWise.getVehicleCode());
            try {
                tvDate.setText(Common.convertDateFormat(accidentReportDateWise.getAccidentDate(), "yyyy-MM-dd", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvDriver.setText(accidentReportDateWise.getDriverName());
            tvLocation.setText(accidentReportDateWise.getLocation());
            tvDescription.setText(accidentReportDateWise.getDescription());
        }
    }
}
