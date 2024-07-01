package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.raihan.dailyfamily.activity.MealApprovalActivity;

import java.util.ArrayList;


public class MealApprovalAdapter extends RecyclerView.Adapter<MealApprovalAdapter.MyViewHolder>
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
        TextView tv_name, tv_breakfast, tv_launch, tv_dinner, tv_email, tv_date, tv_status;
        Button btn_approved;
        Button btn_declined;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_breakfast = (TextView) itemView.findViewById(R.id.tv_breakfast);
            tv_launch = (TextView) itemView.findViewById(R.id.tv_launch);
            tv_dinner = (TextView) itemView.findViewById(R.id.tv_dinner);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            btn_approved = (Button) itemView.findViewById(R.id.btn_approved);
            btn_declined = (Button) itemView.findViewById(R.id.btn_declined);


          /*  view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public MealApprovalAdapter(Context context, ArrayList<Meal> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_daily_meal_approval, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Meal data = contactList.get(position);


        holder.tv_name.setText(data.getId());
        holder.tv_email.setText(data.getEmail());
        holder.tv_breakfast.setText(String.valueOf(data.getBreakfast()));
        holder.tv_launch.setText(String.valueOf(data.getLaunch()));
        holder.tv_dinner.setText(String.valueOf(data.getDinner()));
        holder.tv_date.setText(String.valueOf(data.getDate()));
        holder.tv_status.setText(String.valueOf(data.getFlag()));


        holder.btn_declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateMealApproval(data.getEmail(), "R", data.getBreakfast(), data.getLaunch(), data.getDinner(), data.getDate(), data.getId());
            }
        });

        holder.btn_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMealApproval(data.getEmail(), "Y", data.getBreakfast(), data.getLaunch(), data.getDinner(), data.getDate(), data.getId());
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

    private void updateMealApproval(String email, String flag, String breakfast, String launch, String dinner, String date, String id) {

        Query queryt = FirebaseDatabase.getInstance().getReference("Meal").orderByChild("id").equalTo(id);

        queryt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                    edtData.getRef().child("flag").setValue(flag);
                    edtData.getRef().child("email").setValue(email);
                    edtData.getRef().child("breakfast").setValue(breakfast);
                    edtData.getRef().child("launch").setValue(launch);
                    edtData.getRef().child("dinner").setValue(dinner);
                    edtData.getRef().child("date").setValue(date);

                }
                Intent intent = new Intent(context, MealApprovalActivity.class);
                DialogCustom.doClearActivity(intent, (Activity) context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}