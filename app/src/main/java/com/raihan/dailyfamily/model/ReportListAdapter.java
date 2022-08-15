package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raihan.dailyfamily.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.activity.TotalBalanceActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;


public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder>
        implements Filterable {
    public static Object ContactsAdapterListener;
    private Context context;
    private ArrayList<Report> contactList = null;
    private ArrayList<Report> contactListFiltered;
    private OnItemClickListener listener;
    double totalMeal = 0.0;
    double perMealAmount = 0.0;
    double alltotalMeal = 0.0;
    double totalCostAmount = 0.0;
    double alltotalCostAmount = 0.0;
    double availableBalance = 0.0;
    double totalDepositAmount = 0.0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_deposit_amount, tv_due_balance, tv_total_meal, tv_due_balance_label, tv_total_meal_label;
        Button btn_payment_details;
        TextView tv_total_shoping_cost;
        TextView tv_total_breakfast;
        TextView tv_total_launch_meal;
        TextView tv_total_diner_meal;
        TextView tv_per_meal_cost;
        TextView tv_total_meal_cost;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_deposit_amount = (TextView) itemView.findViewById(R.id.tv_deposit_amount);
            tv_due_balance = (TextView) itemView.findViewById(R.id.tv_due_balance);
            tv_total_meal = (TextView) itemView.findViewById(R.id.tv_total_meal);
            tv_due_balance_label = (TextView) itemView.findViewById(R.id.tv_due_balance_label);
            tv_total_meal_label = (TextView) itemView.findViewById(R.id.tv_total_meal_label);
            tv_total_shoping_cost = (TextView) itemView.findViewById(R.id.tv_total_shoping_cost);
            tv_total_breakfast = (TextView) itemView.findViewById(R.id.tv_total_breakfast);
            tv_total_launch_meal = (TextView) itemView.findViewById(R.id.tv_total_launch_meal);
            tv_total_diner_meal = (TextView) itemView.findViewById(R.id.tv_total_diner_meal);
            tv_per_meal_cost = (TextView) itemView.findViewById(R.id.tv_per_meal_cost);
            tv_total_meal_cost = (TextView) itemView.findViewById(R.id.tv_total_meal_cost);
            btn_payment_details = (Button) itemView.findViewById(R.id.btn_payment_details);

          /*  view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public ReportListAdapter(Context context, ArrayList<Report> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_report_listviewall, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Report data = contactList.get(position);

        holder.tv_name.setText(data.getName());
        holder.tv_per_meal_cost.setText(ValidationUtil.commaSeparateAmount(String.valueOf(data.getPerMealAmount())));
        alltotalCostAmount = data.getAlltotalCostAmount();
        perMealAmount = data.getPerMealAmount();
        alltotalMeal = data.getAlltotalMeal();
        // allTotalBazar();
        //allTotalMeal();
        totalAmount(data.getEmail(), holder.tv_deposit_amount);
        totalBazar(data.getEmail(), holder.tv_total_shoping_cost);
        totalMeal(data.getEmail(),
                holder.tv_total_meal,
                holder.tv_due_balance,
                holder.tv_due_balance_label,
                holder.tv_total_breakfast,
                holder.tv_total_launch_meal,
                holder.tv_total_diner_meal,
                holder.tv_deposit_amount,
                holder.tv_total_meal_cost);


        holder.btn_payment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(contactList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactList = contactListFiltered;
                } else {
                    ArrayList<Report> filteredList = new ArrayList<>();
                    for (Report row : contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);

                            // || row.getAccountTitle().contains(charSequence)
                        }
                    }

                    contactList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactList = (ArrayList<Report>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onContactSelected(Report contact);
    }

    private void totalMeal(String email,
                           TextView tv_total_meal,
                           TextView tv_due_balance,
                           TextView tv_due_balance_label,
                           TextView tv_total_breakfast,
                           TextView tv_total_launch_meal,
                           TextView tv_total_diner_meal,
                           TextView tv_deposit_amount,
                           TextView tv_total_meal_cost) {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            // loadingDialog.startDialoglog();
            Query query = FirebaseDatabase.getInstance().getReference().child("Meal").orderByChild("email").equalTo(email);

            query.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double breakfasttotal = 0;
                    double launchtotal = 0;
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y") && !map.isEmpty()) {
                            Object breakfast = map.get("breakfast");
                            Object launch = map.get("launch");
                            Object dinner = map.get("dinner");
                            double breakfastvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            double launchvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(launch)));
                            double dinnervalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(dinner)));
                            breakfasttotal += breakfastvalue;
                            launchtotal += launchvalue;
                            dinnertotal += dinnervalue;

                            totalMeal = (breakfasttotal * .5) + launchtotal + dinnertotal;
                            // perMealAmount = totalCostAmount / totalMeal;
                            double cost = perMealAmount * totalMeal;
                            double deposit = ValidationUtil.replacecommaDouble(tv_deposit_amount.getText().toString());
                            availableBalance = deposit - cost;


                            tv_total_meal.setText(String.valueOf(totalMeal));
                            tv_total_breakfast.setText(String.valueOf((breakfasttotal)));
                            tv_total_launch_meal.setText(String.valueOf((launchtotal)));
                            tv_total_diner_meal.setText(String.valueOf((dinnertotal)));


                            //tv_per_meal_cost.setText(ValidationUtil.commaSeparateAmount(String.valueOf(perMealAmount)));
                            tv_total_meal_cost.setText(ValidationUtil.commaSeparateAmount(String.valueOf(cost)));


                            if (deposit >= cost) {
                                tv_due_balance.setText(ValidationUtil.commaSeparateAmount(String.valueOf(availableBalance)));
                                tv_due_balance_label.setText("Available Balance");
                            } else {
                                tv_due_balance.setText(ValidationUtil.commaSeparateAmount(String.valueOf(availableBalance)));
                                tv_due_balance_label.setText("Due Balance");

                            }
                        } else {
                            totalDepositAmount = 0;
                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // loadingDialog.dismisstDialoglog();
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                }
            });
        }
    }

    private void totalBazar(String email, TextView tv_total_shoping_cost) {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            //loadingDialog.startDialoglog();
            Query query = FirebaseDatabase.getInstance().getReference().child("Bazaar").orderByChild("email").equalTo(email);

            query.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double bazartotal = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y") && !map.isEmpty()) {
                            Object amount = map.get("amount");
                            double bazarvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(amount)));
                            bazartotal += bazarvalue;
                            totalCostAmount = bazartotal;
                            tv_total_shoping_cost.setText(ValidationUtil.commaSeparateAmount(String.valueOf(totalCostAmount)));

                        } else {
                            tv_total_shoping_cost.setText(ValidationUtil.commaSeparateAmount(String.valueOf(0)));

                        }
                    }
                    //globalVariable.setTotalAmount(String.valueOf(total));

                    //total_cost_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(bazartotal)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // loadingDialog.dismisstDialoglog();
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                }
            });
        }
    }

    private void totalAmount(String email, TextView tv_deposit_amount) {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            //loadingDialog.startDialoglog();
            Query query = FirebaseDatabase.getInstance().getReference().child("Transaction").orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double total = 0.0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("amount--", ds.toString());
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (!map.isEmpty()) {
                            Object amount = map.get("amount");
                            double pvalue = Double.parseDouble(String.valueOf(amount));
                            total += pvalue;

                            totalDepositAmount = total;
                            tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount(String.valueOf(totalDepositAmount)));
                            Log.d("amountn--", String.valueOf(total));
                        } else {
                            tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount(String.valueOf(0)));
                            totalDepositAmount = 0;

                        }
                    }


                    // globalVariable.setTotalAmount(String.valueOf(total));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                    //loadingDialog.startDialoglog();
                }
            });
        }
    }

    private void allTotalMeal() {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            FirebaseDatabase.getInstance().getReference().child("Meal").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double breakfasttotal = 0;
                    double launchtotal = 0;
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y")) {
                            Object breakfast = map.get("breakfast");
                            Object launch = map.get("launch");
                            Object dinner = map.get("dinner");
                            double breakfastvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            double launchvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(launch)));
                            double dinnervalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(dinner)));
                            breakfasttotal += breakfastvalue;
                            launchtotal += launchvalue;
                            dinnertotal += dinnervalue;
                        }


                    }

                    alltotalMeal = (breakfasttotal * .5) + launchtotal + dinnertotal;
                    perMealAmount = alltotalCostAmount / alltotalMeal;

                    Log.d("alltotalMeal", String.valueOf(alltotalMeal));
                    Log.d("perMealAmount", String.valueOf(perMealAmount));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                }
            });
        }
    }

    private void allTotalBazar() {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            FirebaseDatabase.getInstance().getReference().child("Bazaar").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double bazartotal = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y")) {
                            Object breakfast = map.get("amount");

                            double bazarvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            bazartotal += bazarvalue;
                            alltotalCostAmount = bazartotal;

                            Log.d("alltotalCostAmount", String.valueOf(alltotalCostAmount));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                }
            });
        }
    }

    private void allTotalAmount() {
        if (!DialogCustom.isOnline((Activity) context)) {
            DialogCustom.showInternetConnectionMessage((Activity) context);
        } else {
            //loadingDialog.startDialoglog();
            FirebaseDatabase.getInstance().getReference().child("Transaction").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double total = 0.0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String lastbal = "" + ds.child("amount").getValue();
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();

                        Object amount = map.get("amount");
                        double pvalue = Double.parseDouble(String.valueOf(amount));
                        total += pvalue;


                    }

                    totalDepositAmount = total;

                    // globalVariable.setTotalAmount(String.valueOf(total));
                    // loadingDialog.dismisstDialoglog();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());
                    // loadingDialog.dismisstDialoglog();
                }
            });
        }
        // loadingDialog.dismisstDialoglog();

    }

}