package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.StockPartReplacementReportDatum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class PartReplacementHistoryAdapter extends RecyclerView.Adapter<PartReplacementHistoryAdapter.ViewHolder> {
    private final Context context;
    private List<StockPartReplacementReportDatum> reportData = new ArrayList<>();

    public PartReplacementHistoryAdapter(Context context, List<StockPartReplacementReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_partreplacement_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setReportData(reportData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reportData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSrNo;
        private final TextView tvPartNameCode;
        private final TextView tvDriver;
        private final TextView tvReplacedDate;
        private final TextView tvOdometerKm;
        private final TextView tvProblem;
        private String SolvedDate;
        private String ProblemDescription, ReplacedDate, PartNameCode;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvOdometerKm = itemView.findViewById(R.id.tvOdometerKm);
            tvReplacedDate = itemView.findViewById(R.id.tvReplacedDate);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDriver.setSelected(true);
            tvPartNameCode = itemView.findViewById(R.id.tvPartNameCode);
            tvPartNameCode.setSelected(true);
            tvProblem = itemView.findViewById(R.id.tvProblem);
            tvProblem.setSelected(true);
        }

        public void setReportData(StockPartReplacementReportDatum partReplacedReportDatum, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            tvOdometerKm.setText(String.valueOf(partReplacedReportDatum.getMeterReading()));
            tvDriver.setText(partReplacedReportDatum.getDriverName());
            ReplacedDate = partReplacedReportDatum.getPartReplaceDTM();
            ProblemDescription = partReplacedReportDatum.getProblemDesc();
            tvProblem.setText(ProblemDescription);
            if (ReplacedDate != null) {
                try {
                    tvReplacedDate.setText(Common.convertDateFormat(partReplacedReportDatum.getPartReplaceDTM(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (ProblemDescription != null) {
                tvProblem.setText(ProblemDescription);
            }

            if (partReplacedReportDatum.getPartCode() == null) {
                tvPartNameCode.setText(partReplacedReportDatum.getPartName());
            } else {
                tvPartNameCode.setText(partReplacedReportDatum.getPartName() + " \n( " + partReplacedReportDatum.getPartCode() + " )");
            }
        }
    }
}
