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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;


public class MealReportDailyListAdapter extends RecyclerView.Adapter<MealReportDailyListAdapter.MyViewHolder>
        implements Filterable {
    public static Object ContactsAdapterListener;
    private Context context;
    private ArrayList<Meal> contactList = null;
    private ArrayList<Meal> contactListFiltered;
    private OnItemClickListener listener;
    String date = "";
    String oldtotal = "";
    String monthlydep = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_breakfast, tv_launch, tv_dinner;
        Button btn_payment_details;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_breakfast = (TextView) itemView.findViewById(R.id.tv_breakfast);
            tv_launch = (TextView) itemView.findViewById(R.id.tv_launch);
            tv_dinner = (TextView) itemView.findViewById(R.id.tv_dinner);
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


    public MealReportDailyListAdapter(Context context, ArrayList<Meal> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_report_daily_meal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Meal data = contactList.get(position);


        holder.tv_name.setText(data.getName());

        //  martextshow();
        Query queryt = FirebaseDatabase.getInstance().getReference().child("Meal").orderByChild("email").equalTo(data.getEmail());

        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double breakfasttotal = 0;
                double launchtotal = 0;
                double dinnertotal = 0;
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y") && map.get("date").equals(data.getDate())) {
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

                        holder.tv_breakfast.setText(String.valueOf(breakfasttotal));
                        holder.tv_launch.setText(String.valueOf(launchtotal));
                        holder.tv_dinner.setText(String.valueOf(dinnertotal));


                    }
                } else {
                    holder.tv_breakfast.setText(ValidationUtil.commaSeparateAmount("0"));
                    holder.tv_launch.setText(ValidationUtil.commaSeparateAmount("0"));
                    holder.tv_dinner.setText(ValidationUtil.commaSeparateMonth("0"));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
                // throw databaseError.toException(); // don't ignore errors
                holder.tv_breakfast.setText(ValidationUtil.commaSeparateAmount("0"));
                holder.tv_launch.setText(ValidationUtil.commaSeparateAmount("0"));
                holder.tv_dinner.setText(ValidationUtil.commaSeparateMonth("0"));
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
                    ArrayList<Meal> filteredList = new ArrayList<>();
                    for (Meal row : contactListFiltered) {

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
                contactList = (ArrayList<Meal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onContactSelected(Meal contact);
    }

}