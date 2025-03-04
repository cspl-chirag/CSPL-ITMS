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
import com.example.hp.superadminitms.Model.StaffWiseKitAssignReport;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class HistoryAssignKitDataAdapter extends RecyclerView.Adapter<HistoryAssignKitDataAdapter.ViewHolder> {
    private final Context context;
    private List<StaffWiseKitAssignReport> reportData = new ArrayList<>();

    public HistoryAssignKitDataAdapter(Context context, List<StaffWiseKitAssignReport> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_5_fileds, parent, false));
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
        private final TextView tvStaffMemberName;
        private final TextView tvDesignation;
        private final TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStaffMemberName = itemView.findViewById(R.id.tvName1);
            tvDesignation = itemView.findViewById(R.id.tvName2);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStaffMemberName.setSelected(true);
            tvDesignation.setSelected(true);
            tvDescription.setSelected(true);
        }

        public void setData(StaffWiseKitAssignReport accidentReportDateWise, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            try {
                tvDate.setText(Common.convertDateFormat(accidentReportDateWise.getAssignDate(), "yyyy-MM-dd'T'hh:mm:sss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvStaffMemberName.setText(accidentReportDateWise.getStaffMemberName());
            tvDesignation.setText(accidentReportDateWise.getDesignationName());
            tvDescription.setText(accidentReportDateWise.getAssignKitDetail());

        }
    }
}
