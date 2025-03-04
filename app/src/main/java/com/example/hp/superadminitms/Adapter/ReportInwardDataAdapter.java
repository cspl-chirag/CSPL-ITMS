package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.StockInwardReportDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class ReportInwardDataAdapter extends RecyclerView.Adapter<ReportInwardDataAdapter.ViewHolder> {
    private final Context context;
    private final List<StockInwardReportDatum> reportData;

    public ReportInwardDataAdapter(Context context, List<StockInwardReportDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_inward_data, parent, false);
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
        private final TextView tvInwardDate;
        private final TextView tvQuantity;
        private final TextView tvBillNo;
        private final TextView tvStoreName;
        private String inwardDate, BillNo, CheckedBy, CourierAgencyName, InwardNo, ManufacuterName, PurchasedBy, Quantity, Remark, StoreName, Rate;


        public ViewHolder(View itemView) {
            super(itemView);
            tvBillNo = itemView.findViewById(R.id.tvBillNo);
            tvInwardDate = itemView.findViewById(R.id.tvInwardDate);
            tvPartNameCode = itemView.findViewById(R.id.tvPartNameCode);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
        }

        public void setReportData(StockInwardReportDatum stockInwardReportDatum) {

            inwardDate = stockInwardReportDatum.getInwardDTM();
            BillNo = stockInwardReportDatum.getBillNo();
            CheckedBy = stockInwardReportDatum.getCheckedBy();
            CourierAgencyName = stockInwardReportDatum.getCourierAgencyName();
            InwardNo = String.valueOf(stockInwardReportDatum.getInwardNo());
            ManufacuterName = stockInwardReportDatum.getManufacturerName();
            PurchasedBy = stockInwardReportDatum.getPurchasedBy();
            Quantity = String.valueOf(stockInwardReportDatum.getQuantity());
            Remark = stockInwardReportDatum.getRemark();
            StoreName = stockInwardReportDatum.getStoreName();
            tvStoreName.setSelected(true);
            Rate = String.valueOf(stockInwardReportDatum.getRate());
            if (BillNo != null) {
                tvBillNo.setText(BillNo);
            }
            try {
                tvInwardDate.setText(Common.convertDateFormat(inwardDate, "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (stockInwardReportDatum.getPartCode() == null) {
                tvPartNameCode.setText(stockInwardReportDatum.getPartName());
            } else {
                tvPartNameCode.setText(stockInwardReportDatum.getPartName() + " - " + stockInwardReportDatum.getPartCode());
            }
            if (Quantity != null) {
                tvQuantity.setText(Quantity);
            }
            if (StoreName != null) {
                tvStoreName.setText(StoreName);
            }
        }
    }
}
