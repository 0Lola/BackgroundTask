package com.example.zxa01.backgroundtask.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zxa01.backgroundtask.R;
import com.example.zxa01.backgroundtask.entity.Book;

import java.util.ArrayList;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private ArrayList<Book> mData;
    private Context mContext;

    public SimpleAdapter(Context context, ArrayList<Book> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SimpleAdapter.ViewHolder holder, int position) {
        holder.bindTo(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleText;
        private TextView mAuthorText;
        private TextView mPrintType;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.title);
            mAuthorText = itemView.findViewById(R.id.author);
            mPrintType = itemView.findViewById(R.id.printType);
        }

        void bindTo(Book e) {
            mTitleText.setText(e.getTitle());
            mAuthorText.setText(e.getAuthor());
            mPrintType.setText(e.getPrintType());
        }
    }
}
