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
import com.example.hp.superadminitms.Model.RTOExpDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 30-May-19.
 */

public class RtoExpAdapter extends RecyclerView.Adapter<RtoExpAdapter.ViewHolder> {
    private final Context context;
    private final List<RTOExpDatum> rtoExpDatumList;

    public RtoExpAdapter(Context context, List<RTOExpDatum> rtoExpDatumList) {
        this.context = context;
        this.rtoExpDatumList = rtoExpDatumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.licence_exp_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(String.valueOf(rtoExpDatumList.get(position).getVehicle()));
        try {
            holder.tvEndDate.setText(Common.convertDateFormat(rtoExpDatumList.get(position).getRegiValidUpto(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rtoExpDatumList.size();
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
