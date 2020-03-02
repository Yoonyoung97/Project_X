package com.example.hairnawa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hairnawa.CommentData;
import com.example.hairnawa.R;

import java.util.ArrayList;
import java.util.Vector;

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater Inflater = null;
    private Vector<CommentData> Comment;

    public CommentAdapter(Context context, Vector<CommentData> data) {
        mContext = context;
        Comment = data;
        Inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return Comment.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CommentData getItem(int position) {
        return Comment.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (Inflater == null)
            {
                Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = Inflater.inflate(R.layout.comment, parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.Name);
        TextView context = (TextView)convertView.findViewById(R.id.context);
        TextView score = (TextView)convertView.findViewById(R.id.score);
        name.setText(Comment.get(position).getName());
        context.setText(Comment.get(position).getContext());
        score.setText(Comment.get(position).getScore());
        return convertView;
    }

}