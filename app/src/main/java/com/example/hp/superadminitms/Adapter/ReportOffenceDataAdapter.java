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
import com.example.hp.superadminitms.Model.StaffWiseOffenceReport;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class ReportOffenceDataAdapter extends RecyclerView.Adapter<ReportOffenceDataAdapter.ViewHolder> {
    private final Context context;
    private List<StaffWiseOffenceReport> reportList = new ArrayList<>();

    public ReportOffenceDataAdapter(Context context, List<StaffWiseOffenceReport> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_offence_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position, reportList.get(position));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvOffenceDate;
        private final TextView tvAmount;
        private final TextView tvPenaltyBy;
        private final TextView tvOffence;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvOffenceDate = itemView.findViewById(R.id.tvOffenceDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPenaltyBy = itemView.findViewById(R.id.tvPenaltyBy);
            tvOffence = itemView.findViewById(R.id.tvOffence);
            tvOffence.setSelected(true);
            tvPenaltyBy.setSelected(true);
            tvAmount.setSelected(true);
        }

        public void setData(int position, StaffWiseOffenceReport staffWiseOffenceReport) {
            tvSrNo.setText(String.valueOf(position + 1));
            try {
                tvOffenceDate.setText(Common.convertDateFormat(staffWiseOffenceReport.getOffenceDate(), "yyyy-MM-dd'T'hh:mm:ss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvAmount.setText(staffWiseOffenceReport.getOffenceAmount().toString());
            tvPenaltyBy.setText(staffWiseOffenceReport.getPenaltyBy());
            tvOffence.setText(staffWiseOffenceReport.getOffence());
        }
    }
}
