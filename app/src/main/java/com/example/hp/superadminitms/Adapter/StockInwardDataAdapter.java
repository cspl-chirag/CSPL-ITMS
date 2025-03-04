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
import com.example.hp.superadminitms.Model.StockInwardDatum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by INFOTEK on 2/18/2020.
 */

public class StockInwardDataAdapter extends RecyclerView.Adapter<StockInwardDataAdapter.ViewHolder> {

    OnItemClickListener itemClickListener;
    private final Context context;
    private List<String> billNo = new ArrayList<>();
    private List<StockInwardDatum> stockInwardData = new ArrayList<>();

    public StockInwardDataAdapter(Context context, List<String> billNo, List<StockInwardDatum> stockInwardData) {
        this.context = context;
        this.billNo = billNo;
        this.stockInwardData = stockInwardData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.stock_inward_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(billNo.get(position), position);
    }

    @Override
    public int getItemCount() {
        return billNo.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvSrNo;
        private final TextView tvInwardDate;
        private final TextView tvBillNo;
        private final TextView tvCourierLrNo;
        private final TextView tvBillAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvInwardDate = itemView.findViewById(R.id.tvInwardDate);
            tvBillNo = itemView.findViewById(R.id.tvBillNo);
            tvCourierLrNo = itemView.findViewById(R.id.tvCourierLrNo);
            tvBillAmount = itemView.findViewById(R.id.tvBillAmount);
            itemView.setOnClickListener(this);
        }

        public void setData(String billNo, int position) {
            tvSrNo.setText(String.valueOf(position + 1));

            List<StockInwardDatum> inwardDataList = new ArrayList<>();
            for (StockInwardDatum detailReportHcfListDatum : stockInwardData) {
                if (detailReportHcfListDatum.getBillNo().equals(billNo)) {
                    inwardDataList.add(detailReportHcfListDatum);
                }
            }

            tvBillNo.setText(inwardDataList.get(0).getBillNo());
            try {
                tvInwardDate.setText(Common.convertDateFormat(inwardDataList.get(0).getInwardDTM(), "yyyy-MM-dd'T'HH:mm:sss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvBillAmount.setText(String.valueOf(inwardDataList.get(0).getBillAmount()));
            tvCourierLrNo.setText(inwardDataList.get(0).getCourierLRNumber());
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
