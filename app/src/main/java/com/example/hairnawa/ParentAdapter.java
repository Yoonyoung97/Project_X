package com.example.hairnawa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class ParentAdapter extends BaseExpandableListAdapter {

    private static final int PARENT_ROW = R.layout.parent;
    private static final int CHILD_ROW = R.layout.child;
    private Context context;
    private Vector<ParentData> data;
    private LayoutInflater inflater = null;
    private TextView tv_time, tv_customerName, tv_surgery;
    private ImageView iv_arrow;

    public ParentAdapter(Context context, Vector<ParentData> data){
        this.data = data;
        this.context = context;
        try{
            this.inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View getGroupView(int groupPosition,
                             boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(PARENT_ROW, parent, false);
        }

        tv_time = convertView.findViewById(R.id.tv_time);
        tv_customerName = convertView.findViewById(R.id.tv_customerName);
        tv_surgery = convertView.findViewById(R.id.tv_surgery);

        String date = data.get(groupPosition).getDate().substring(8,10) + ":" + data.get(groupPosition).getDate().substring(10,12); //09:00
        tv_time.setText(date);
        tv_customerName.setText(data.get(groupPosition).getName()); //김리나 (고객님)
        String surgery = data.get(groupPosition).getService();
        int len = surgery.length();
        surgery = surgery.substring(1, len - 1); // [] 제거
        surgery = surgery.replaceAll(",", "/"); //커트 / 파마 / 염색
        tv_surgery.setText(surgery);
        iv_arrow = convertView.findViewById(R.id.iv_arrow);
        if(isExpanded){
            iv_arrow.setImageResource(R.drawable.ic_expand_less_purple_24dp);
            convertView.findViewById(R.id.layoutBorder).setBackgroundResource(R.drawable.selected_border);
        } else {
            iv_arrow.setImageResource(R.drawable.ic_expand_more_purple_24dp);
            convertView.findViewById(R.id.layoutBorder).setBackgroundResource(R.drawable.nonseleted_border);
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

        Button call = convertView.findViewById(R.id.call);
        call.setTag(data.get(groupPosition).child.get(childPosition).getPhoneNumber());
        call.setOnClickListener(callListener);

        Button message = convertView.findViewById(R.id.message);
        message.setTag(data.get(groupPosition).child.get(childPosition).getPhoneNumber());
        message.setOnClickListener(messageListener);

        return convertView;
    }

    View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + v.getTag())); //전화 걸기
            v.getContext().startActivity(intent);
        }
    };

    View.OnClickListener messageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + v.getTag())); //메시지 보내기
            //intent.putExtra("sms_body", "보낼 내용"); //보낼 메시지 내용
            v.getContext().startActivity(intent);
        }
    };

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
