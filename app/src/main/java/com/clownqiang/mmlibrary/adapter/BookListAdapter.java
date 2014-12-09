package com.clownqiang.mmlibrary.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clownqiang.mmlibrary.MyApp;
import com.clownqiang.mmlibrary.R;
import com.clownqiang.mmlibrary.activity.BookDetailActivity;
import com.clownqiang.mmlibrary.model.BookMessage;
import com.clownqiang.mmlibrary.util.CustomeUtil;
import com.clownqiang.mmlibrary.util.UserData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.ParseException;
import java.util.List;

/**
 * Created by clownqiang on 14/12/5.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookHolder> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.loading_failed)
            .showImageForEmptyUri(R.drawable.loading_failed)
            .showStubImage(R.drawable.loading)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    List<BookMessage> bookMessageList;
    Activity activity;

    String leftTime;

    public BookListAdapter(List<BookMessage> list, Activity activity) {
        this.bookMessageList = list;
        this.activity = activity;
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclelist_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookHolder holder, int position) {
        final BookMessage bookMessage = bookMessageList.get(position);
        if (!bookMessage.getTitle().isEmpty()) {
            try {
                if (CustomeUtil.getLeftDay(bookMessage.getReturn_date()) < UserData.getEarlyDay()) {
                    holder.leftTime.setTextColor(MyApp.getContext().getResources().getColor(android.R.color.holo_red_dark));
                }
                leftTime = String.valueOf(CustomeUtil.getLeftDay(bookMessage.getReturn_date()));
                holder.leftTime.setText("" + leftTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.bookName.setText(bookMessage.getTitle());
            holder.autherName.setText(bookMessage.getAuthor());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BookDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("book_message", bookMessage);
                    intent.putExtras(bundle);
                    if (Build.VERSION.SDK_INT == 21) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                                Pair.create((View) holder.bookPage, "bookPage")
                        );
                        activity.startActivity(intent, options.toBundle());
                    }else {
                        activity.startActivity(intent);
                    }
                }
            });
            imageLoader.displayImage(bookMessage.getImg_url(),holder.bookPage,options);
        }
    }

    @Override
    public int getItemCount() {
        return bookMessageList.size();
    }

    class BookHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView autherName;
        TextView leftTime;
        CardView cardView;
        ImageView bookPage;

        public BookHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.bookName);
            autherName = (TextView) itemView.findViewById(R.id.autherName);
            leftTime = (TextView) itemView.findViewById(R.id.leftTime);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            bookPage = (ImageView) itemView.findViewById(R.id.bookPage);
        }
    }

}
