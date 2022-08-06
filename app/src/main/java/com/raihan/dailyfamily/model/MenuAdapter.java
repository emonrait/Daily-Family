package com.raihan.dailyfamily.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuModel> {

    // View lookup cache

    private  class ViewHolder {

        ImageView menu_icon;
        TextView menu_name;
        TextView menu_soft_code ;


    }



    public MenuAdapter(Context context, ArrayList<MenuModel> welcomeMenuModels) {

        super(context, R.layout.row_fundtransfer_menu, welcomeMenuModels);

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        MenuModel model = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.row_fundtransfer_menu, parent, false);

            viewHolder.menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
            viewHolder.menu_name = (TextView) convertView.findViewById(R.id.menu_name);
            viewHolder.menu_soft_code = (TextView) convertView.findViewById(R.id.menu_soft_code);


            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        // Populate the data into the template view using the data object

        viewHolder.menu_icon.setImageResource(model.getIcon());


        viewHolder.menu_name.setText(model.getMenuName());


        viewHolder.menu_soft_code.setText(model.getSoftcode());



        return convertView;

    }

}