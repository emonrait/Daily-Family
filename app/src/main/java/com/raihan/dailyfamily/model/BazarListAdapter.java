package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raihan.dailyfamily.R;

import java.util.ArrayList;


public class BazarListAdapter extends RecyclerView.Adapter<BazarListAdapter.MyViewHolder>
        implements Filterable {
    public static Object ContactsAdapterListener;
    private Context context;
    private ArrayList<Bazaar> contactList = null;
    private ArrayList<Bazaar> contactListFiltered;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_amount, tv_product_details;
        Button btn_payment_details;

        public MyViewHolder(View view) {
            super(view);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_product_details = (TextView) itemView.findViewById(R.id.tv_product_details);
            btn_payment_details = (Button) itemView.findViewById(R.id.btn_payment_details);

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }


    public BazarListAdapter(Context context, ArrayList<Bazaar> contactList, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_report_daily_bazaar, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Bazaar data = contactList.get(position);


        holder.tv_date.setText(data.getDate());
        holder.tv_amount.setText(ValidationUtil.commaSeparateAmount(data.getAmount()));
        holder.tv_product_details.setText(data.getProductDetails());
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
                    ArrayList<Bazaar> filteredList = new ArrayList<>();
                    for (Bazaar row : contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId().toLowerCase().contains(charString.toLowerCase())
                                || row.getDate().contains(charSequence)) {
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
}