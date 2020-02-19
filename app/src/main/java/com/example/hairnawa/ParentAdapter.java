package com.example.hairnawa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class ParentAdapter extends BaseExpandableListAdapter {

    private static final int PARENT_ROW = R.layout.parent;
    private static final int CHILD_ROW = R.layout.child;
    private Context context;
    private Vector<ParentData> data;
    private LayoutInflater inflater = null;
    private ImageView iv_arrow;

    public ParentAdapter(Context context, Vector<ParentData> data){
        this.data = data;
        this.context = context;
        this.inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getGroupView(int groupPosition,
                             boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(PARENT_ROW, parent, false);
        }

        TextView header = convertView.findViewById(R.id.list1);
        String date = data.get(groupPosition).getDate().substring(8,10) + ":" + data.get(groupPosition).getDate().substring(10,12);
        String reservation = date + " : " + data.get(groupPosition).getName() + " 고객님 " + data.get(groupPosition).getService();
        header.setText(reservation);
        iv_arrow = convertView.findViewById(R.id.iv_arrow);
        if(isExpanded){
            iv_arrow.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            iv_arrow.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(CHILD_ROW, parent, false);
        }

        TextView content = convertView.findViewById(R.id.list_more);
        String reservationInformation
                = "폰 번호: " + data.get(groupPosition).child.get(childPosition).getPhoneNumber()
                + "\n가격: " + data.get(groupPosition).child.get(childPosition).getPrice() + "원";
        content.setText(reservationInformation);

        return convertView;
    }
    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).child.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).child.get(childPosition);
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
