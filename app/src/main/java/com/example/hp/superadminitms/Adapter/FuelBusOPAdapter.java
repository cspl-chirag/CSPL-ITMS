package com.example.hp.superadminitms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Model.FuelBusDatum;

import java.util.List;

/**
 * Created by ANDROID-PC on 04/01/2019.
 */

public class FuelBusOPAdapter extends RecyclerView.Adapter<FuelBusOPAdapter.FuelBusHolder> {

    public static List<FuelBusDatum> fuelBusData;
    public static int selected_item = 0;
    OnItemClickListener mItemClickListener;
    private final Context context;

    public FuelBusOPAdapter(List<FuelBusDatum> fuelBusData, Context context) {
        FuelBusOPAdapter.fuelBusData = fuelBusData;
        this.context = context;
    }

    @Override
    public FuelBusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FuelBusHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bus_fuel, parent, false));
    }

    @Override
    public void onBindViewHolder(FuelBusHolder holder, int position) {
        holder.tvBus.setText(fuelBusData.get(position).getVehicleName());
        holder.tvQuantity.setText(fuelBusData.get(position).getQuantity() + " Ltr.");
        holder.tvRate.setText(fuelBusData.get(position).getRate() + " " + context.getString(R.string.rs));
        holder.tvAmount.setText(fuelBusData.get(position).getAmount() + " " + context.getString(R.string.rs));
        holder.tvMeterReading.setText(String.valueOf(fuelBusData.get(position).getMeterReading()));
    }

    @Override
    public int getItemCount() {
        return fuelBusData.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class FuelBusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvBus;
        private final TextView tvQuantity;
        private final TextView tvRate;
        private final TextView tvAmount;
        private final TextView tvMeterReading;

        public FuelBusHolder(View itemView) {
            super(itemView);
            tvBus = itemView.findViewById(R.id.tvRegistrationNo);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvMeterReading = itemView.findViewById(R.id.tvMeterReading);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }

    }

}
