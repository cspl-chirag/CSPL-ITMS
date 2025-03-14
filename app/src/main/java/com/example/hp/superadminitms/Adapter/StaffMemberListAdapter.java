package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Model.StaffDatum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by INFOTEK on 2/1/2020.
 */

public class StaffMemberListAdapter extends RecyclerView.Adapter<StaffMemberListAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private List<StaffDatum> staffData = new ArrayList<>();
    private List<StaffDatum> mFilteredList = new ArrayList<>();

    public StaffMemberListAdapter(Context context, List<StaffDatum> staffData) {
        this.context = context;
        this.staffData = staffData;
        this.mFilteredList = staffData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_member_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mFilteredList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toUpperCase();
                if (charString.isEmpty()) {
                    mFilteredList = staffData;
                } else {
                    List<StaffDatum> filteredList = new ArrayList<>();

                    for (StaffDatum staffDatum : staffData) {
                        if (staffDatum.getFirstName().toUpperCase().contains(charString) ||
                                staffDatum.getFirstName().toLowerCase().contains(charString) ||
                                staffDatum.getMiddleName().toUpperCase().contains(charString) ||
                                staffDatum.getMiddleName().toLowerCase().contains(charString) ||
                                staffDatum.getLastName().toUpperCase().contains(charString) ||
                                staffDatum.getLastName().toLowerCase().contains(charString) ||
                                staffDatum.getStaffCode().toLowerCase().contains(charString) ||
                                staffDatum.getStaffCode().toUpperCase().contains(charString)) {
                            filteredList.add(staffDatum);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                mFilteredList = (List<StaffDatum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStaffName;
        private TextView tvStaffCode;
        private TextView tvAddress;
        private final TextView tvMobileNo;
        private final TextView tvDesignation;
        private final TextView tvJoiningDate;
        private final TextView tvSrNo;

        public ViewHolder(View itemView) {
            super(itemView);
//            tvStaffCode = itemView.findViewById(R.id.tvStaffCode);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvStaffName = itemView.findViewById(R.id.tvStaffMemberName);
            tvStaffName.setSelected(true);
//            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvMobileNo = itemView.findViewById(R.id.tvMobileNo);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvDesignation.setSelected(true);
            tvJoiningDate = itemView.findViewById(R.id.tvJoiningDate);
        }

        public void setData(StaffDatum staffDatum, int position) {
            tvSrNo.setText(String.valueOf(position + 1));
            if (staffDatum.getStaffCode() != null) {
                tvStaffName.setText(staffDatum.getFirstName() + " " + staffDatum.getMiddleName() + " " + staffDatum.getLastName() + " - " + staffDatum.getStaffCode());
            } else {
                tvStaffName.setText(staffDatum.getFirstName() + " " + staffDatum.getMiddleName() + " " + staffDatum.getLastName());
            }
//            tvStaffCode.setText(staffDatum.getStaffCode());
            try {
                tvJoiningDate.setText(Common.convertDateFormat(staffDatum.getJoiningDate(), "yyyy-dd-MM'T'hh:mm:ss", "yyyy-dd-MM"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            tvAddress.setText(staffDatum.getPermanentAddress1());
            tvDesignation.setText(staffDatum.getDesignation());

            if (staffDatum.getMobileNo1().isEmpty() && staffDatum.getMobileNo2().isEmpty()) {
                tvMobileNo.setText(" ");
            } else if (!staffDatum.getMobileNo1().isEmpty() && !staffDatum.getMobileNo2().isEmpty()) {
                tvMobileNo.setText(staffDatum.getMobileNo1() + " \n" + staffDatum.getMobileNo2() + " ");
            }
        }
    }
}
