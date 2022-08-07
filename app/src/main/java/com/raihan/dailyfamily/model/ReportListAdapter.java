package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
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
    String date = "";
    String oldtotal = "";
    String monthlydep = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_deposit_amount, tv_due_balance, tv_total_meal, tv_due_balance_label, tv_total_meal_label;
        Button btn_payment_details;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_deposit_amount = (TextView) itemView.findViewById(R.id.tv_deposit_amount);
            tv_due_balance = (TextView) itemView.findViewById(R.id.tv_due_balance);
            tv_total_meal = (TextView) itemView.findViewById(R.id.tv_total_meal);
            tv_due_balance_label = (TextView) itemView.findViewById(R.id.tv_due_balance_label);
            tv_total_meal_label = (TextView) itemView.findViewById(R.id.tv_total_meal_label);
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

        martextshow();
        Query queryt = FirebaseDatabase.getInstance().getReference().child("Transaction").orderByChild("email").equalTo(data.getEmail().trim());

        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0.0;
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        assert map != null;
                        Object amount = map.get("amount");
                        Log.d("value-->", String.valueOf(amount));

                        if (map.isEmpty()) {
                            holder.tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount("0"));
                            holder.tv_due_balance.setText(ValidationUtil.commaSeparateAmount("0"));
                            holder.tv_total_meal.setText(ValidationUtil.commaSeparateMonth("0"));
                        } else {
                            double pvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(amount)));
                            total += pvalue;
                            holder.tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                            try {
                                //String inputString = "";
                                String currentTotal = oldtotal;
                                String monthlyDeposit = monthlydep;
                                String inputString = date;
                                SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                                String pattern = "yyyy-MM-dd";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                String date = simpleDateFormat.format(new Date());
                                String reformattedStr = simpleDateFormat.format(fromUser.parse(inputString));
                                Calendar actDate = new GregorianCalendar();
                                Calendar curDate = new GregorianCalendar();
                                Date thedate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reformattedStr);
                                actDate.setTime(thedate);

                                Date thedate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
                                curDate.setTime(thedate1);
                                //breakdown

                                int firstMonth = actDate.get(Calendar.MONTH);
                                int lastMonth = curDate.get(Calendar.MONTH);

                                int firstYear = actDate.get(Calendar.YEAR);
                                int lastYear = curDate.get(Calendar.YEAR);
                                int totalYear = lastYear - firstYear;

                                int totalMonths = (totalYear * 12) - (firstMonth) + lastMonth + 1;
                                double oldotal = Double.parseDouble(ValidationUtil.replacecomma(currentTotal));
                                double currentMonthlyDepo = Double.parseDouble(ValidationUtil.replacecomma(monthlyDeposit));
                                double totalamt = oldotal + (totalMonths * currentMonthlyDepo);
                                double dueamt = total - totalamt;

                                holder.tv_total_meal.setText(ValidationUtil.commaSeparateMonth(String.valueOf((dueamt / currentMonthlyDepo))));

                                if (dueamt > 0) {
                                    holder.tv_due_balance.setText(ValidationUtil.commaSeparateAmount(String.valueOf(dueamt)));
                                    holder.tv_due_balance_label.setText("Extra Balance: ");
                                    holder.tv_total_meal_label.setText("Extra Months: ");
                                } else if (dueamt == 0) {
                                    holder.tv_due_balance.setText("0" + ValidationUtil.commaSeparateAmount(String.valueOf(dueamt)));
                                    holder.tv_total_meal_label.setText("Due Months: ");
                                } else {
                                    holder.tv_due_balance.setText(ValidationUtil.commaSeparateAmount(String.valueOf(dueamt)));
                                    holder.tv_total_meal_label.setText("Due Months: ");
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                } else {
                    holder.tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount("0"));
                    holder.tv_due_balance.setText(ValidationUtil.commaSeparateAmount("0"));
                    holder.tv_total_meal.setText(ValidationUtil.commaSeparateMonth("0"));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
                // throw databaseError.toException(); // don't ignore errors
                holder.tv_deposit_amount.setText(ValidationUtil.commaSeparateAmount("0"));
                holder.tv_due_balance.setText(ValidationUtil.commaSeparateAmount("0"));
                holder.tv_total_meal.setText(ValidationUtil.commaSeparateMonth("0"));
            }
        });


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

    private void martextshow() {
        FirebaseDatabase.getInstance()
                .getReference().child("Message")
                .orderByChild("message2")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            date = "" + ds.child("date").getValue();
                            oldtotal = "" + ds.child("oldtotal").getValue();
                            monthlydep = "" + ds.child("monthlydep").getValue();

                            //DialogCustom.showErrorMessage((Activity) context, date + oldtotal + monthlydep);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());

                    }
                });
    }

}