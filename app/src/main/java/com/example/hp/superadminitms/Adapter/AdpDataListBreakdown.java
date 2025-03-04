package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Activity.StoreUpdateBreakdownActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BreakdownDatum;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Android-2 on 07-Feb-19.
 */

public class AdpDataListBreakdown extends RecyclerView.Adapter<AdpDataListBreakdown.ViewHolder> {
    private final Context context;
    private final List<BreakdownDatum> breakdownData;

    public AdpDataListBreakdown(Context context, List<BreakdownDatum> breakdownData) {
        this.context = context;
        this.breakdownData = breakdownData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.st_breakdown_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSrNo.setText(String.valueOf(position + 1));
        holder.tvLocation.setText(breakdownData.get(position).getCompanyCity());
        holder.tvBusRegNo.setText(breakdownData.get(position).getVehicleCode() + "-" + breakdownData.get(position).getVehicleRegNo());
//        holder.tvCity.setText(breakdownData.get(position).getCompanyCity());
        try {
            holder.tvProblemDate.setText(Common.convertDateFormat(breakdownData.get(position).getProblemDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //
// try {
//
//            //holder.tvProblemDate.setText(Common.convertDateFormat(String.valueOf(breakdownData.get(position).getProblemDate()),"dd/MM/yyyy HH:mm:ss","dd-MM-yyyy"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.tvProblrm.setText(breakdownData.get(position).getProblem());
    }

    @Override
    public int getItemCount() {
        return breakdownData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvBusRegNo;
        private final TextView tvProblemDate;
        private final TextView tvProblrm;
        private final TextView tvLocation;
        private final TextView tvSrNo;
        private final Context c;
        private final SessionManager sessionManager;
        private final int userType;

        public ViewHolder(View itemView) {
            super(itemView);
            c = itemView.getContext();
            sessionManager = new SessionManager(c);
            userType = sessionManager.getUserType();
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvBusRegNo = itemView.findViewById(R.id.tvBusRegNo);
            tvProblemDate = itemView.findViewById(R.id.tvProblemDate);
            tvProblrm = itemView.findViewById(R.id.tvProblem);
            tvProblrm.setSelected(true);
            tvBusRegNo.setSelected(true);
//            tvCity = itemView.findViewById(R.id.tvCity);
            if (userType == 1 || userType == 2) {
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
                    c.startActivity(new Intent(StoreUpdateBreakdownActivity.newIntent(c, breakdownData.get(getAdapterPosition()))));
                }
            });
        }
    }
}
