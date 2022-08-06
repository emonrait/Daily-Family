package com.raihan.dailyfamily.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raihan.dailyfamily.R;

import java.util.ArrayList;

public class MyAdpterNew extends RecyclerView.Adapter<MyAdpterNew.BlogViewHolder> {
    private ArrayList<ListItem> contactList;
    private ArrayList<ListItem> contactListFiltered;
    // implements Filterable

    public MyAdpterNew(ArrayList<ListItem> contactList) {
        this.contactList = contactList;
        this.contactListFiltered = contactList;

    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_statement_listview, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {
        ListItem data = contactListFiltered.get(position);

        holder.tv_txnid.setText(data.getId());
        holder.tv_invoiceno.setText(data.getInvoiceno());
        holder.tv_date.setText(data.getDate());
        holder.tv_amount.setText(data.getAmount());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView tv_txnid, tv_invoiceno, tv_date, tv_amount;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_txnid = (TextView) itemView.findViewById(R.id.tv_txnid);
            tv_invoiceno = (TextView) itemView.findViewById(R.id.tv_invoiceno);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);

        }
    }

  /*  @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    ArrayList<ListItem> filteredList = new ArrayList<>();
                    for (ListItem row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getInvoiceno().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                            // || row.getAccountTitle().contains(charSequence)
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/
}
