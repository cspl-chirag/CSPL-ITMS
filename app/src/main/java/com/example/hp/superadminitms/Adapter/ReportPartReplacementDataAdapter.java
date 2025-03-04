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
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class ReportPartReplacementDataAdapter extends RecyclerView.Adapter<ReportPartReplacementDataAdapter.ViewHolder> {
    private final Context context;
    private final List<StockPartReplacementReportDatum> reportData;

    public ReportPartReplacementDataAdapter(Context context, List<StockPartReplacementReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_partreplacement_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setReportData(reportData.get(position));
    }

    @Override
    public int getItemCount() {
        return reportData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvVehicle;
        private final TextView tvProblem;
        private final TextView tvOutwardDate;
        private final TextView tvPartNameCode;
        private String SolvedDate;
        private String VehicleType, Route, JobDateTime, ProblemDescription, SolvedBy, SolvedDateTime, PersonName, Quantity, Description, OutwardDate, PartNameCode;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOutwardDate = itemView.findViewById(R.id.tvOutwardDate);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvPartNameCode = itemView.findViewById(R.id.tvPartNameCode);
            tvProblem = itemView.findViewById(R.id.tvProblem);
            tvProblem.setSelected(true);
        }

        public void setReportData(StockPartReplacementReportDatum partReplacedReportDatum) {

            OutwardDate = partReplacedReportDatum.getPartReplaceDTM();
            VehicleType = partReplacedReportDatum.getVehicleCode() + " - " + partReplacedReportDatum.getVehicleRegNo();
            Route = partReplacedReportDatum.getRouteNo() + " - " + partReplacedReportDatum.getRouteName();
            String JobDate = null, JobTime = null;
            try {
                JobDate = Common.convertDateFormat(partReplacedReportDatum.getJobDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JobDateTime = JobDate;
            ProblemDescription = partReplacedReportDatum.getProblemDesc();
            tvProblem.setText(ProblemDescription);
            SolvedBy = partReplacedReportDatum.getSolvedBy();
            if (partReplacedReportDatum.getSolvedTime() != null) {
                try {
                    SolvedDate = Common.convertDateFormat(partReplacedReportDatum.getSolvedDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SolvedDateTime = SolvedDate + " " + partReplacedReportDatum.getSolvedTime();
            } else {
                try {
                    SolvedDate = Common.convertDateFormat(partReplacedReportDatum.getSolvedDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SolvedDateTime = SolvedDate;
            }
            PersonName = partReplacedReportDatum.getMechanicName();
            Quantity = String.valueOf(partReplacedReportDatum.getQuantity());
            Description = partReplacedReportDatum.getProblemDesc();

            if (OutwardDate != null) {
                try {
                    tvOutwardDate.setText(Common.convertDateFormat(partReplacedReportDatum.getPartReplaceDTM(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (VehicleType != null) {
                tvVehicle.setText(VehicleType);
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
