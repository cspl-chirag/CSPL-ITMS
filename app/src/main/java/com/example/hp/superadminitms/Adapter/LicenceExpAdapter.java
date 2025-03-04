package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.LiceneceExpDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 30-May-19.
 */

public class LicenceExpAdapter extends RecyclerView.Adapter<LicenceExpAdapter.ViewHolder> {
    private final Context context;
    private final List<LiceneceExpDatum> liceneceExpDatumList;

    public LicenceExpAdapter(Context context, List<LiceneceExpDatum> liceneceExpDatumList) {
        this.context = context;
        this.liceneceExpDatumList = liceneceExpDatumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.licence_exp_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(liceneceExpDatumList.get(position).getName());
        try {
            holder.tvEndDate.setText(Common.convertDateFormat(liceneceExpDatumList.get(position).getLicenceExpiryDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        holder.tvProblemDate.setText(String.valueOf(Common.convertDateFormat(breakdownData.get(position).getProblemDate(),"yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy")));
    }

    @Override
    public int getItemCount() {
        return liceneceExpDatumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvEndDate;
        private final TextView tvSrNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
        }
    }
}
