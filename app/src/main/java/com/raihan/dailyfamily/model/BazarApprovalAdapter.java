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
import com.raihan.dailyfamily.activity.BazarApproval;
import com.raihan.dailyfamily.activity.DashboardActivity;

import java.util.ArrayList;
import java.util.Map;


public class BazarApprovalAdapter extends RecyclerView.Adapter<BazarApprovalAdapter.MyViewHolder>
        implements Filterable {
    public static Object ContactsAdapterListener;
    private Context context;
    private ArrayList<Bazaar> contactList = null;
    private ArrayList<Bazaar> contactListFiltered;
    private OnItemClickListener listener;
    String date = "";
    String oldtotal = "";
    String monthlydep = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_email, tv_date, tv_total_amount, tv_product_details;
        Button btn_approved;
        Button btn_declined;

        public MyViewHolder(View view) {
            super(view);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_total_amount = (TextView) itemView.findViewById(R.id.tv_total_amount);
            tv_product_details = (TextView) itemView.findViewById(R.id.tv_product_details);
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


    public BazarApprovalAdapter(Context context, ArrayList<Bazaar> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_daily_bazar_approval, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Bazaar data = contactList.get(position);


        holder.tv_email.setText(data.getEmail());
        holder.tv_date.setText(data.getDate());
        holder.tv_total_amount.setText(ValidationUtil.commaSeparateAmount(data.getAmount()));
        holder.tv_product_details.setText(data.getProductDetails());


        holder.btn_declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateMealApproval(data.getEmail(), "R", data.getAmount(), data.getDate(), data.getId(), data.getProductDetails());
            }
        });

        holder.btn_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMealApproval(data.getEmail(), "Y", data.getAmount(), data.getDate(), data.getId(), data.getProductDetails());
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
                    ArrayList<Bazaar> filteredList = new ArrayList<>();
                    for (Bazaar row : contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getEmail().toLowerCase().contains(charString.toLowerCase())
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
                contactList = (ArrayList<Bazaar>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onContactSelected(Bazaar contact);
    }

    private void updateMealApproval(String email, String flag, String amount, String date, String id, String productDetails) {

        Query queryt = FirebaseDatabase.getInstance().getReference("Bazaar").orderByChild("id").equalTo(id);

        queryt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                    edtData.getRef().child("flag").setValue(flag);
                    edtData.getRef().child("email").setValue(email);
                    edtData.getRef().child("date").setValue(date);
                    edtData.getRef().child("amount").setValue(amount);
                    edtData.getRef().child("productDetails").setValue(productDetails);
                    edtData.getRef().child("id").setValue(id);

                }
                Intent intent = new Intent(context, BazarApproval.class);
                DialogCustom.doClearActivity(intent, (Activity) context);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}