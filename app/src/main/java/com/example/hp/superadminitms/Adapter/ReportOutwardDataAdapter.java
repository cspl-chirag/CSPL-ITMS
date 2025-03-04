package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.StockOutwardReportDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class ReportOutwardDataAdapter extends RecyclerView.Adapter<ReportOutwardDataAdapter.ViewHolder> {
    private final Context context;
    private final List<StockOutwardReportDatum> reportData;

    public ReportOutwardDataAdapter(Context context, List<StockOutwardReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_outward_data, parent, false);
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
        private final TextView tvPartNameCode;
        private final TextView tvVehicle;
        private final TextView tvGivenBy;
        private final TextView tvQuantity;
        private final TextView tvOutwardDate;
        private String outwardDate, VehicleType, GivenBy, Quantity;


        public ViewHolder(View itemView) {
            super(itemView);
            tvOutwardDate = itemView.findViewById(R.id.tvOutwardDate);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvPartNameCode = itemView.findViewById(R.id.tvPartNameCode);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvGivenBy = itemView.findViewById(R.id.tvGivenBy);
            tvGivenBy.setSelected(true);
        }

        public void setReportData(StockOutwardReportDatum stockOutwardReportDatum) {

            outwardDate = stockOutwardReportDatum.getOutwardDTM();
            if (stockOutwardReportDatum.getVehicleCode() == null && stockOutwardReportDatum.getVehicleRegNo() == null) {
                VehicleType = "-";
            } else {
                VehicleType = stockOutwardReportDatum.getVehicleCode() + " - " + stockOutwardReportDatum.getVehicleRegNo();
            }
            GivenBy = stockOutwardReportDatum.getGivenBy();
            Quantity = String.valueOf(stockOutwardReportDatum.getQuantity());

            if (outwardDate != null) {
                try {
                    tvOutwardDate.setText(Common.convertDateFormat(stockOutwardReportDatum.getOutwardDTM(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (VehicleType != null) {
                tvVehicle.setText(VehicleType);
            }
            if (stockOutwardReportDatum.getPartCode() == null) {
                tvPartNameCode.setText(stockOutwardReportDatum.getPartName());
            } else {
                tvPartNameCode.setText(stockOutwardReportDatum.getPartName() + " - " + stockOutwardReportDatum.getPartCode());
            }
            if (GivenBy != null) {
                tvGivenBy.setText(GivenBy);
            }
            if (Quantity != null) {
                tvQuantity.setText(Quantity);
            }
        }
    }
}
