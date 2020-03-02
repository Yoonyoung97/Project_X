package com.example.hairnawa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Vector;

public class Frag5Adapter extends BaseExpandableListAdapter
{

    private static final int PARENT_ROW = R.layout.customer_parent;
    private static final int CHILD_ROW = R.layout.customer_child;
    private Context context;
    private Vector<customerParent> data;
    private LayoutInflater inflater = null;
    public Frag5Adapter(Context context, Vector<customerParent> data) {
        this.context = context;
        this.data = data;
        this.inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(PARENT_ROW, parent, false);
        }
        TextView nameTV = convertView.findViewById(R.id.userName);
        TextView priceTV = convertView.findViewById(R.id.price);
        TextView visitsTV = convertView.findViewById(R.id.visits);

        String name = data.get(groupPosition).getName();
        Integer price = data.get(groupPosition).getTotal_price();
        Integer vitis = data.get(groupPosition).getVisits();

        nameTV.setText(name);
        priceTV.setText(price.toString());
        visitsTV.setText(vitis.toString());
        return convertView;
    }
//20200212 1  2  2  4  1  2
//01234567 9 10 11 12 13  14
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(CHILD_ROW, parent, false);
        }
        TextView lastDate = convertView.findViewById(R.id.lastdate);
        TextView lastProcedure = convertView.findViewById(R.id.procedure);
        String date = data.get(groupPosition).getChild().get(childPosition).getLast_date().substring(0,4) + "-"
                + data.get(groupPosition).getChild().get(childPosition).getLast_date().substring(4,6)+ "-" + data.get(groupPosition).getChild().get(childPosition).getLast_date().substring(6,8);
        String procedure = data.get(groupPosition).getChild().get(childPosition).getLast_procedure();
        lastDate.setText(date);
        lastProcedure.setText(procedure);

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getChild().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
