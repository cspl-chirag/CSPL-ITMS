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
import com.example.hp.superadminitms.Model.StockOutwardDatum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOTEK on 2/18/2020.
 */

public class StockOutwardDataAdapter extends RecyclerView.Adapter<StockOutwardDataAdapter.ViewHolder> {

    OnItemClickListener itemClickListener;
    private final Context context;
    private List<Integer> batchId = new ArrayList<>();
    private List<StockOutwardDatum> stockOutwardData = new ArrayList<>();

    public StockOutwardDataAdapter(Context context, List<Integer> batchId, List<StockOutwardDatum> stockOutwardData) {
        this.context = context;
        this.batchId = batchId;
        this.stockOutwardData = stockOutwardData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.stock_outward_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(batchId.get(position), position);
    }

    @Override
    public int getItemCount() {
        return batchId.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvSrNo;
        private final TextView tvRemark;
        private final TextView tvGivenBy;
        private final TextView tvOutwardDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvOutwardDate = itemView.findViewById(R.id.tvOutwardDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            tvGivenBy = itemView.findViewById(R.id.tvGivenBy);
            itemView.setOnClickListener(this);
        }

        public void setData(Integer batchId, int position) {
            tvSrNo.setText(String.valueOf(position + 1));

            List<StockOutwardDatum> outwardDataList = new ArrayList<>();
            for (StockOutwardDatum datum : stockOutwardData) {
                if (datum.getOutwardBatchID().equals(batchId)) {
                    outwardDataList.add(datum);
                }
            }
            try {
                tvOutwardDate.setText(Common.convertDateFormat(outwardDataList.get(0).getOutwardDTM(), "yyyy-MM-dd'T'HH:mm:sss", "dd-MM-yyyy"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvRemark.setText(String.valueOf(outwardDataList.get(0).getRemark()));
            tvGivenBy.setText(outwardDataList.get(0).getGivenBy());
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
