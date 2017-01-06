package com.rydohg.assignmenttracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class OptionsAdapter extends ArrayAdapter<OptionListItem> {
    private Context context;

    public OptionsAdapter(Context context, int resourceId,
                              List<OptionListItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView titleTextView;
        TextView selectedOption;
        ImageView label;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        OptionListItem optionList = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_option, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.option_name);
            holder.selectedOption = (TextView) convertView.findViewById(R.id.selectedOption);
            holder.label = (ImageView) convertView.findViewById(R.id.label);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(optionList.getTitle());
        if (optionList.getTitle().equals("Due Date")){
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            holder.selectedOption.setText(formatter.format(System.currentTimeMillis()));
        }
        Drawable drawable = parent.getResources().getDrawable(optionList.getDrawableId());
        holder.label.setImageDrawable(drawable);

        return convertView;
    }
}
