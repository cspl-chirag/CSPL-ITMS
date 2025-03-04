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

public class ReportAssignKitDataAdapter extends RecyclerView.Adapter<ReportAssignKitDataAdapter.ViewHolder> {
    private final Context context;
    private List<StaffWiseKitAssignReport> data = new ArrayList<>();

    public ReportAssignKitDataAdapter(Context context, List<StaffWiseKitAssignReport> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_4_fileds, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvDate;
        private final TextView tvDesignation;
        private final TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDesignation = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDesignation.setSelected(true);
            tvDescription.setSelected(true);
        }

        public void setData(StaffWiseKitAssignReport report, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            try {
                tvDate.setText(Common.convertDateFormat(report.getAssignDate(), "yyyy-MM-dd'T'hh:mm:sss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvDesignation.setText(report.getDesignationName());
            tvDescription.setText(report.getAssignKitDetail());
        }
    }
}
