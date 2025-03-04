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
import com.example.hp.superadminitms.Model.ReportBusWiseMaintenanceHistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class MaintananceHistoryAdapter extends RecyclerView.Adapter<MaintananceHistoryAdapter.ViewHolder> {
    private final Context context;
    private List<ReportBusWiseMaintenanceHistory> reportData = new ArrayList<>();

    public MaintananceHistoryAdapter(Context context, List<ReportBusWiseMaintenanceHistory> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_maintanance_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setReportData(reportData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reportData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo, tvOdometerKm, tvJobDate, tvDriver, tvProblem, tvSolution;
        private String ProblemDescription, JobDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvOdometerKm = itemView.findViewById(R.id.tvOdometerKm);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDriver.setSelected(true);
            tvProblem = itemView.findViewById(R.id.tvProblem);
            tvProblem.setSelected(true);
            tvSolution = itemView.findViewById(R.id.tvSolution);
            tvSolution.setSelected(true);
        }

        public void setReportData(ReportBusWiseMaintenanceHistory datum, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvOdometerKm.setText(String.valueOf(datum.getMeterReading()));
            tvDriver.setText(datum.getDriverName());
            JobDate = datum.getJobDate();
            ProblemDescription = datum.getProblemDesc();
            tvProblem.setText(ProblemDescription);
            tvSolution.setText(datum.getSolvedDesc());
            if (JobDate != null) {
                try {
                    tvJobDate.setText(Common.convertDateFormat(datum.getJobDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (ProblemDescription != null) {
                tvProblem.setText(ProblemDescription);
            }
        }
    }
}
