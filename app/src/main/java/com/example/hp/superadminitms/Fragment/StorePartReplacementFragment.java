package com.example.hp.superadminitms.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MenuItemCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Adapter.PartsAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.PartsDatum;
import com.example.hp.superadminitms.Network.InsertPartReplaceRequest;
import com.example.hp.superadminitms.Network.InsertPartReplaceResponse;
import com.example.hp.superadminitms.Network.PartListResponse;
import com.example.hp.superadminitms.Network.WholePartListRequest;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StorePartReplacementFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String BUNDLE_VEHICLE_REG_NO = "vehicle_reg_no";
    private static final String BUNDLE_JOB_ID = "job_id";
    private static final String BUNDLE_VEHICLE_ID = "vehicle_id";
    private View mView;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private EditText etMechanicName, etPartReplaceRemark;
    private TextView btnSubmitParts;
    private CheckBox cbIsPartReplacing;
    private LinearLayout llCheckBox;
    private RecyclerView rvParts;
    private LinearLayout llPartReplacementSection;
    private String UserName;
    private int UserID, Company_ID;
    private String Selected_Date, Selected_Time, PartReplacedDateTime;
    private Date d;
    private boolean isConnected;
    private int Job_ID, Vehicle_ID;
    private List<PartsDatum> partsData = new ArrayList<>();
    private final List<PartsDatum> partInsertionList = new ArrayList<>();
    private PartsAdapter partsAdapter;
    private int quantity;
    private final List<InsertPartReplaceRequest> partInsertionMainList = new ArrayList<InsertPartReplaceRequest>();
    private SearchView searchView;
    private TextView tvVehicle;
    private String vehicle_reg_No;
    private long mLastClickTime = 0;


    public StorePartReplacementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_store_part_replacement, container, false);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        initializeControls(mView);
        return mView;
    }

    @SuppressLint("ResourceType")
    private void initializeControls(View view) {
        pg = new ProgressDialog(getContext(), R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        sessionManager = new SessionManager(getContext());
        etMechanicName = view.findViewById(R.id.etPersonName);
        etPartReplaceRemark = view.findViewById(R.id.etPartReplaceRemark);
        btnSubmitParts = view.findViewById(R.id.btnSubmitParts);
        Company_ID = sessionManager.getKeyCompanyId();
        UserID = sessionManager.getKeyUserId();
        UserName = sessionManager.getKeyUserName();
        llPartReplacementSection = view.findViewById(R.id.llPartReplacementSection);
        rvParts = view.findViewById(R.id.rvParts);
        cbIsPartReplacing = view.findViewById(R.id.cbIsPartReplacing);
        llCheckBox = view.findViewById(R.id.llCheckBox);

        if (cbIsPartReplacing.isChecked()) {
            partReplacementPortionVisible();
        } else {
            partReplacementPortionNotVisible();
        }
        cbIsPartReplacing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int[][] states = {{android.R.attr.state_checked}, {}};
                final int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
                CompoundButtonCompat.setButtonTintList(cbIsPartReplacing, new ColorStateList(states, colors));
                if (compoundButton.isChecked()) {
                    llPartReplacementSection.setVisibility(View.VISIBLE);
                    getPartList();
                } else {
                    llPartReplacementSection.setVisibility(View.GONE);
                    partInsertionList.clear();
                    //                    closeOptionsMenu();
                }
            }
        });
        btnSubmitParts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                diableDoubleClick();
                hideSoftKeyBoard();
                searchView.clearFocus();
                if (partInsertionList.isEmpty()) {
                    Common.showSnackError(mView.findViewById(R.id.llRootLayout), "Parts Not Selected...!");
                } else {
                    partReplacement();
                }
            }
        });

        tvVehicle = view.findViewById(R.id.tvBusRegNo);
        Bundle b = getArguments();
        vehicle_reg_No = b.getString(BUNDLE_VEHICLE_REG_NO);
        Job_ID = b.getInt(BUNDLE_JOB_ID);
        Vehicle_ID = b.getInt(BUNDLE_VEHICLE_ID);
        tvVehicle.setText(vehicle_reg_No);
    }

    @SuppressLint("LongLogTag")
    private void partReplacement() {
        Log.e("Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
        if (IsValid(etMechanicName.getText().toString())) {

            try {
                PartReplacedDateTime = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd") + " " + Common.getCurrentTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("PartReplacedDateTime", PartReplacedDateTime);

            String PersonName = etMechanicName.getText().toString();
            String OperatorName = UserName;
            String PartReplaceRemark;
            if (!etPartReplaceRemark.getText().toString().isEmpty()) {
                PartReplaceRemark = etPartReplaceRemark.getText().toString();
            } else {
                PartReplaceRemark = null;
            }
            insertPartReplacementList(partInsertionList, PartReplacedDateTime, PersonName, OperatorName, PartReplaceRemark);
        } else {
            if (!IsValid(etMechanicName.getText().toString())) {
                etMechanicName.requestFocus();
                etMechanicName.setError("Enter Personname...!");
            }
        }
    }

    private void partReplacementPortionNotVisible() {
        if (!partInsertionMainList.isEmpty()) {
            partInsertionMainList.clear();
        }
        llPartReplacementSection.setVisibility(View.GONE);
    }

    private void partReplacementPortionVisible() {
        if (!partInsertionMainList.isEmpty()) {
            partInsertionMainList.clear();
        }
        llPartReplacementSection.setVisibility(View.VISIBLE);
        getPartList();
    }

    private void insertPartReplacementList(List<PartsDatum> partInsertionList, String partReplacedDateTime, String personName, String operatorName, String partReplaceRemark) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(mView.findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (!partInsertionList.isEmpty()) {
                for (int i = 0; i < partInsertionList.size(); i++) {
                    partInsertionMainList.add(new InsertPartReplaceRequest(Job_ID, UserID, Company_ID, partReplacedDateTime, partInsertionList.get(i).getPartID(), Vehicle_ID, partInsertionList.get(i).getNewQuantity(), personName, operatorName, partReplaceRemark));
                }
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.setMessage("Please Wait...");
            pg.show();

            Call<InsertPartReplaceResponse> call = apiInterface.insertPartReplace(partInsertionMainList);
            call.enqueue(new Callback<InsertPartReplaceResponse>() {
                @Override
                public void onResponse(Call<InsertPartReplaceResponse> call, Response<InsertPartReplaceResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("RESPONSE : ", response.body().getMessage());
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);
                                cbIsPartReplacing.setChecked(false);
                                partReplacementPortionNotVisible();
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                                clearControls();
                            } else {
                                hideDialog();
                                Log.e("ERROR : ", response.body().getMessage());
                            }
                        } else {
                            hideDialog();
                            Log.e("ERROR 1 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        hideDialog();
                        Log.e("EXCEPTION : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<InsertPartReplaceResponse> call, Throwable t) {
                    hideDialog();
                    Log.e("EXCEPTION : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getContext(), R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            partReplacement();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }

    }

    private boolean IsValid(String s) {
        return s != null && s.length() > 2 && s.length() <= 200;
    }

    private void clearControls() {
        etPartReplaceRemark.getText().clear();
        etMechanicName.getText().clear();
    }

    private void hideDialog() {
        if (pg.isShowing()) {
            pg.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(mView.findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Enter Partname to be seach");
        searchView.setPadding(0, 5, 0, 5);
        searchView.setBackground(getResources().getDrawable(R.drawable.bg_green_rounded_corner_8dp));
        searchView.setFocusableInTouchMode(true);
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (partsData.size() > 0) {
//                    partsAdapter.getFilter().filter(newText);
//                    return true;
//                }
//                return true;
                for (int position = 0; position < partsData.size(); position++) {

                    if (partsData.get(position).getPartCode() != null) {
                        if (partsData.get(position).getPartCode().toUpperCase().contains(newText.toUpperCase()) || partsData.get(position).getPartCode().toLowerCase().contains(newText.toLowerCase())) {
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        }
                    } else {
                        if (partsData.get(position).getPartName().toUpperCase().startsWith(newText.toUpperCase()) || partsData.get(position).getPartName().toLowerCase().startsWith(newText.toLowerCase())) {
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        } else if (partsData.get(position).getPartName().toUpperCase().contains(newText.toLowerCase()) || partsData.get(position).getPartName().toLowerCase().contains(newText.toLowerCase())) {
//                        rvParts.scrollToPosition(position);
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        }
                    }
                    if (partsData.get(position).getPartName().toUpperCase().startsWith(newText.toUpperCase()) || partsData.get(position).getPartName().toLowerCase().startsWith(newText.toLowerCase())) {
                        rvParts.smoothScrollToPosition(position + 2);
                        break;
                    } else if (partsData.get(position).getPartName().toUpperCase().contains(newText.toLowerCase()) || partsData.get(position).getPartName().toLowerCase().contains(newText.toLowerCase())) {
//                        rvParts.scrollToPosition(position);
                        rvParts.smoothScrollToPosition(position + 2);
                        break;
                    }
                }
                return false;
            }
        });

    }

    private void getPartList() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(mView.findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Parts...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<PartListResponse> call = apiInterface.getPartList(new WholePartListRequest(Company_ID));
            call.enqueue(new Callback<PartListResponse>() {
                @Override
                public void onResponse(Call<PartListResponse> call, Response<PartListResponse> response) {
                    try {
                        pg.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                                partsData = response.body().getPartsData();
                                rvParts.setLayoutManager(new GridLayoutManager(getContext(), 1));
                                partsAdapter = new PartsAdapter(getContext(), partsData);
                                partsAdapter.notifyDataSetChanged();
                                rvParts.setAdapter(partsAdapter);
                                rvParts.setNestedScrollingEnabled(true);
                                rvParts.setVerticalScrollBarEnabled(true);
                                partsAdapter.SetOnItemClickListener(new PartsAdapter.OnItemClickListener() {
                                    public ImageView ivPartMinus;
                                    public ImageView ivPartAdd;
                                    public TextView tvQuantity;

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onItemClick(View view, int pos) {

                                        PartsDatum part = partsData.get(pos);
                                        RecyclerView.ViewHolder holder = rvParts.findViewHolderForAdapterPosition(pos);

                                        if (holder != null) {
                                            ivPartAdd = holder.itemView.findViewById(R.id.ivPartQuantityAdd);
                                            ivPartMinus = holder.itemView.findViewById(R.id.ivPartQuantityMinus);
                                            tvQuantity = holder.itemView.findViewById(R.id.tvPartQuantity);
                                        }

                                        if (part.getNewQuantity() == null) {
                                            quantity = 0;
                                        } else {
                                            quantity = part.getNewQuantity();
                                        }

                                        /*switch (view.getId()) {
                                            case R.id.ivPartQuantityAdd:
                                                if (quantity < part.getQuantity()) {
                                                    quantity = quantity + 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                                    if (partInsertionList.isEmpty()) {
                                                        partInsertionList.add(part);
                                                    } else {
                                                        if (partInsertionList.contains(part)) {
                                                            for (PartsDatum p : partInsertionList) {
                                                                if (part.getPartID() == p.getPartID()) {
                                                                    p.setNewQuantity(quantity);
                                                                    tvQuantity.setText(String.valueOf(quantity));
                                                                }
                                                            }
                                                        } else {
                                                            partInsertionList.add(part);
                                                            break;
                                                        }
                                                    }
                                                }
                                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
//                                                Toast.makeText(getContext(),"Pos"+pos,Toast.LENGTH_LONG).show();
                                                break;
                                            case R.id.ivPartQuantityMinus:

                                                if (!partInsertionList.isEmpty()) {
                                                    if (partInsertionList.contains(part)) {
                                                        for (int i = 0; i < partInsertionList.size(); i++) {
                                                            if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                                if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                                    quantity = 0;
                                                                    part.setNewQuantity(quantity);
                                                                    partInsertionList.remove(i);
                                                                    tvQuantity.setText("0");
                                                                    break;
                                                                } else {
                                                                    if (quantity > 0) {
                                                                        quantity = quantity - 1;
                                                                        part.setNewQuantity(quantity);
                                                                        tvQuantity.setText(String.valueOf(quantity));
                                                                        partInsertionList.get(i).setNewQuantity(quantity);
                                                                    }
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
//                                                Toast.makeText(getContext(),"Pos"+pos,Toast.LENGTH_LONG).show();
                                                break;
                                        }*/

                                        if (view.getId() == R.id.ivPartQuantityAdd) {
                                            if (quantity < part.getQuantity()) {
                                                quantity = quantity + 1;
                                                part.setNewQuantity(quantity);
                                                tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                                if (partInsertionList.isEmpty()) {
                                                    partInsertionList.add(part);
                                                } else {
                                                    if (partInsertionList.contains(part)) {
                                                        for (PartsDatum p : partInsertionList) {
                                                            if (part.getPartID() == p.getPartID()) {
                                                                p.setNewQuantity(quantity);
                                                                tvQuantity.setText(String.valueOf(quantity));
                                                            }
                                                        }
                                                    } else {
                                                        partInsertionList.add(part);
                                                    }
                                                }
                                            }
                                            Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                            // Toast.makeText(getContext(),"Pos"+pos,Toast.LENGTH_LONG).show();
                                        } else if (view.getId() == R.id.ivPartQuantityMinus) {
                                            if (!partInsertionList.isEmpty()) {
                                                if (partInsertionList.contains(part)) {
                                                    for (int i = 0; i < partInsertionList.size(); i++) {
                                                        if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                            if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                                quantity = 0;
                                                                part.setNewQuantity(quantity);
                                                                partInsertionList.remove(i);
                                                                tvQuantity.setText("0");
                                                                break;
                                                            } else {
                                                                if (quantity > 0) {
                                                                    quantity = quantity - 1;
                                                                    part.setNewQuantity(quantity);
                                                                    tvQuantity.setText(String.valueOf(quantity));
                                                                    partInsertionList.get(i).setNewQuantity(quantity);
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                            // Toast.makeText(getContext(),"Pos"+pos,Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                            } else {
                                Log.e("PART LIST ERROR 1 : ", response.body().getMessage());
                                pg.dismiss();
                            }
                        } else {
                            Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                            pg.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e("PART LIST EXCEPTION : ", e.getMessage());
                        pg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<PartListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("PART LIST FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getContext(), R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getPartList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void hideSoftKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void diableDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
            hideSoftKeyBoard();
            getActivity().invalidateOptionsMenu();
        }
    }
}
