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
import com.example.hp.superadminitms.Model.AccidentReportBusWise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class ReportAccidentDataAdapter extends RecyclerView.Adapter<ReportAccidentDataAdapter.ViewHolder> {
    private final Context context;
    private List<AccidentReportBusWise> data = new ArrayList<>();

    public ReportAccidentDataAdapter(Context context, List<AccidentReportBusWise> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_5_fileds, parent, false));
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
        private final TextView tvDriver;
        private final TextView tvLocation;
        private final TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDriver = itemView.findViewById(R.id.tvName1);
            tvLocation = itemView.findViewById(R.id.tvName2);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDriver.setSelected(true);
            tvLocation.setSelected(true);
            tvDescription.setSelected(true);
        }

        public void setData(AccidentReportBusWise accidentReportBusWise, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            try {
                tvDate.setText(Common.convertDateFormat(accidentReportBusWise.getAccidentDate(), "yyyy-MM-dd", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvDriver.setText(accidentReportBusWise.getDriverName());
            tvLocation.setText(accidentReportBusWise.getLocation());
            tvDescription.setText(accidentReportBusWise.getDescription());
        }
    }
}
