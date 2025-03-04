package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Activity.Store_UpdateJobActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.JobCardDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 13-Mar-19.
 */

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.ViewHolder> {
    private final Context context;
    private final List<JobCardDatum> jobCardData;

    public JobCardAdapter(Context contextl, List<JobCardDatum> jobCardData) {
        this.context = contextl;
        this.jobCardData = jobCardData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JobCardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_4_fileds, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvBusRegNo.setText(jobCardData.get(position).getVehicleCode() + "-" + jobCardData.get(position).getVehicleRegNo());
//        holder.tvCity.setText(jobCardData.get(position).getCompanyCity());
        try {
            holder.tvJobDate.setText(Common.convertDateFormat(jobCardData.get(position).getJobDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvProblem.setText(jobCardData.get(position).getProblemDesc());
    }

    @Override
    public int getItemCount() {
        return jobCardData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvBusRegNo;
        private final TextView tvJobDate;
        private final TextView tvProblem;
        private final TextView tvSrNo;
        private Context c;
        private final SessionManager sessionManager;
        private final int userType;

        public ViewHolder(View itemView) {
            super(itemView);
            c = itemView.getContext();
            c = itemView.getContext();
            sessionManager = new SessionManager(c);
            userType = sessionManager.getUserType();
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvBusRegNo = itemView.findViewById(R.id.tvName);
            tvJobDate = itemView.findViewById(R.id.tvDate);
            tvProblem = itemView.findViewById(R.id.tvDescription);
            tvProblem.setSelected(true);
            tvBusRegNo.setSelected(true);

//            tvCity = itemView.findViewById(R.id.tvCity);
            if (userType == 1 || userType == 2 || userType == 7) {
//                if (userType == 1)
//                {
//                    tvCity.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    tvCity.setVisibility(View.GONE);
//                }
                itemView.setActivated(false);
                itemView.setEnabled(false);
                itemView.setClickable(false);
                itemView.setLongClickable(false);
            } else {
//                tvCity.setVisibility(View.GONE);
                itemView.setActivated(true);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.startActivity(new Intent(Store_UpdateJobActivity.newIntent(c, jobCardData.get(getAdapterPosition()))));
                }
            });
        }
    }
}
