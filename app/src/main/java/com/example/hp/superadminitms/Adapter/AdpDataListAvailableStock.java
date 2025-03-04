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
import com.example.hp.superadminitms.Model.WholeListPartDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 27-Jan-20.
 */

public class AdpDataListAvailableStock extends RecyclerView.Adapter<AdpDataListAvailableStock.ViewHolder> implements Filterable {
    private final Context context;
    private final List<WholeListPartDatum> reportData;
    private List<WholeListPartDatum> mFilteredList;


    public AdpDataListAvailableStock(Context context, List<WholeListPartDatum> reportData) {
        this.context = context;
        this.reportData = reportData;
        mFilteredList = reportData;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toUpperCase();
                if (charString.isEmpty()) {
                    mFilteredList = reportData;
                } else {
                    List<WholeListPartDatum> filteredList = new ArrayList<>();

                    for (WholeListPartDatum wholeListPartDatum : reportData) {
                        if (wholeListPartDatum.getPartName().toUpperCase().contains(charString) ||
                                wholeListPartDatum.getPartName().toLowerCase().contains(charString)) {
                            filteredList.add(wholeListPartDatum);
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
                mFilteredList = (List<WholeListPartDatum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_availablestock_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setReportData(mFilteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvQuantity;
        private final TextView tvPartCode;
        private final TextView tvPartName;
        private String Quantity, PartCode, PartName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(R.id.tvPartName);
            tvPartCode = itemView.findViewById(R.id.tvPartCode);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void setReportData(WholeListPartDatum wholeListPartDatum) {
            PartName = wholeListPartDatum.getPartName();
            if (wholeListPartDatum.getPartCode() != null) {
                PartCode = wholeListPartDatum.getPartCode();
            } else {
                PartCode = " - ";
            }
            Quantity = String.valueOf(wholeListPartDatum.getQuantity());
            tvQuantity.setText(Quantity);
            tvPartCode.setText(PartCode);
            tvPartName.setText(PartName);
        }
    }
}
