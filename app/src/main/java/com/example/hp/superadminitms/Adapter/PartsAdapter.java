package com.example.hp.superadminitms.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.PartsDatum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-2 on 07-Jan-20.
 */

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder> implements Filterable {
    public Context context;
    public List<PartsDatum> partsData;
    OnItemClickListener onItemClickListener;
    private List<PartsDatum> mFilteredList;


    public PartsAdapter(Context context, List<PartsDatum> partsData) {
        this.context = context;
        this.partsData = partsData;
        this.onItemClickListener = onItemClickListener;
        mFilteredList = partsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.part_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mFilteredList.get(position));
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
                    mFilteredList = partsData;
                } else {
                    List<PartsDatum> filteredList = new ArrayList<>();

                    for (PartsDatum wholeListPartDatum : partsData) {
                        if (wholeListPartDatum.getPartName().toUpperCase().startsWith(charString) ||
                                wholeListPartDatum.getPartName().toLowerCase().startsWith(charString)) {
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
                mFilteredList = (List<PartsDatum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void SetOnItemClickListener(final PartsAdapter.OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvPartName, tvQuantity, tvPartCode;
        public ImageView ivQuantityAdd, ivPartQuantityMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(R.id.tvPartName);
            tvQuantity = itemView.findViewById(R.id.tvPartQuantity);
            tvPartCode = itemView.findViewById(R.id.tvPartCode);
            ivQuantityAdd = itemView.findViewById(R.id.ivPartQuantityAdd);
            ivPartQuantityMinus = itemView.findViewById(R.id.ivPartQuantityMinus);
            ivQuantityAdd.setOnClickListener(this);
            ivPartQuantityMinus.setOnClickListener(this);
        }

        public void setData(PartsDatum wholeListPartDatum) {
            tvPartName.setText(wholeListPartDatum.getPartName());
            if (wholeListPartDatum.getNewQuantity() == null) {
                tvQuantity.setText("0");
            } else {
                tvQuantity.setText(String.valueOf(wholeListPartDatum.getNewQuantity()));
            }
            if (wholeListPartDatum.getPartCode() == null) {
                tvPartCode.setText("");
            } else {
                tvPartCode.setText(wholeListPartDatum.getPartCode());
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
//                int pos = mFilteredList.indexOf(mFilteredList.get(getAdapterPosition()));
//                onItemClickListener.onItemClick(view,getLayoutPosition());
//                onItemClickListener.onItemClick(view,mFilteredList.indexOf(mFilteredList.get(getAdapterPosition())));
            }
        }
    }
}
