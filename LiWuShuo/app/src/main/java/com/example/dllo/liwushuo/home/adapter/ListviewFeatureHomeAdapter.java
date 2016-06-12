package com.example.dllo.liwushuo.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.liwushuo.R;
import com.example.dllo.liwushuo.home.bean.ListviewBean;
import com.example.dllo.liwushuo.register.RegisterActivity;
import com.example.dllo.liwushuo.tool.App;
import com.example.dllo.liwushuo.tool.NetTool;
import com.example.dllo.liwushuo.tool.RoundRectTool;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by dllo on 16/5/24.
 */
public class ListviewFeatureHomeAdapter extends BaseAdapter {
    private ListviewBean listviewBean;
    private RoundRectTool roundRectTool = new RoundRectTool(20);
    public Context context;


    public ListviewFeatureHomeAdapter(Context context) {
        this.context = context;
    }

    public ListviewFeatureHomeAdapter() {
    }

    public void setListviewBean(ListviewBean listviewBean) {
        this.listviewBean = listviewBean;
        notifyDataSetChanged();
    }

    //为了下拉刷新增加的方法
    public void addItemBean(List<ListviewBean.DataBean.ItemsBean> itemsBeans) {
        listviewBean.getData().getItems().addAll(itemsBeans);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listviewBean == null ? 0 : listviewBean.getData().getItems().size();
    }

    @Override
    public Object getItem(int position) {
        return listviewBean == null ? null : listviewBean.getData().getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Myholder myholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.context).inflate(R.layout.item_home_featured_listview, parent, false);
            myholder = new Myholder(convertView);
            convertView.setTag(myholder);
        } else {
            myholder = (Myholder) convertView.getTag();
        }
        myholder.setPos(position);
        //设置数据
        myholder.itemHomeFeatureListviewTitleTv.setText(listviewBean.getData().getItems().get(position).getTitle());

        String data = NetTool.formatData("yyyy-MM-dd EE", listviewBean.getData().getItems().get(position).getCreated_at());

        myholder.itemHomeFeatureDataTv.setText(data);

        // 设置listview中相同的只有一个日期显示
        String dataTest = "";
        if (position > 0 && position <= listviewBean.getData().getItems().size()) {
            dataTest = NetTool.formatData("yyyy-MM-dd EE", listviewBean.getData().getItems().get(position - 1).getCreated_at());
        }

        if (data.equals(dataTest)) {
            myholder.itemHomeFeatureDataLayout.setVisibility(View.GONE);
        } else {
            myholder.itemHomeFeatureDataLayout.setVisibility(View.VISIBLE);
        }//设置好了


        myholder.itemHomeFeatureListviewLikeCb.setText(String.valueOf(listviewBean.getData().getItems().get(position).getLikes_count()));
        myholder.itemHomeFeatureListviewLikeCb.setChecked(listviewBean.getData().getItems().get(position).isLiked());
        Picasso.with(App.context).load(listviewBean.getData().getItems().get(position).getCover_image_url()).placeholder(R.mipmap.ig_logo_text).
                transform(roundRectTool).fit().into(myholder.itemHomeFeatureListviewImg);


        //TODO:设置new图片可见不可见
//        final Myholder finalMyholder = myholder;
//        myholder.itemHomeFeatureListviewRlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listviewBean.getData().getItems().get(position).setStatus(1);
//                finalMyholder.itemHomeFeatureFreshImg.setVisibility(View.GONE);
//            }
//        });
//
//        if (listviewBean.getData().getItems().get(position).getStatus() != 1) {
//            myholder.itemHomeFeatureFreshImg.setVisibility(View.VISIBLE);
//        }else {
//            myholder.itemHomeFeatureFreshImg.setVisibility(View.GONE);
//        }

        //checkbox加监听, 解决复用问题
//        myholder.itemHomeFeatureListviewLikeCb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        return convertView;
    }


    class Myholder {
        private int pos;
        private final ImageView itemHomeFeatureListviewImg;
        private final TextView itemHomeFeatureDataTv;
        private final TextView itemHomeFeatureListviewTitleTv;
        private final LinearLayout itemHomeFeatureDataLayout;
        private final ImageView itemHomeFeatureFreshImg;
        private final RelativeLayout itemHomeFeatureListviewRlayout;
        private CheckBox itemHomeFeatureListviewLikeCb;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public Myholder(View view) {
            itemHomeFeatureListviewImg = (ImageView) view.findViewById(R.id.item_home_feature_listview_img);
            itemHomeFeatureDataTv = (TextView) view.findViewById(R.id.item_home_feature_data_tv);
            itemHomeFeatureListviewTitleTv = (TextView) view.findViewById(R.id.item_home_feature_listview_title_tv);
            itemHomeFeatureDataLayout = (LinearLayout) view.findViewById(R.id.item_home_feature_data_layout);
            itemHomeFeatureFreshImg = (ImageView) view.findViewById(R.id.item_home_feature_fresh_img);
            itemHomeFeatureListviewRlayout = (RelativeLayout) view.findViewById(R.id.item_home_feature_listview_rlayout);
            itemHomeFeatureListviewLikeCb = (CheckBox) view.findViewById(R.id.item_home_feature_listview_like_cb);
            itemHomeFeatureListviewLikeCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobUser bmobUser = BmobUser.getCurrentUser(context);
                    if (bmobUser == null) {
                        Intent intent = new Intent(context, RegisterActivity.class);
                        context.startActivity(intent);
                        itemHomeFeatureListviewLikeCb.setChecked(false);
                    } else {
                        CheckBox checkBox = (CheckBox) v;
                        listviewBean.getData().getItems().get(pos).setLiked(checkBox.isChecked());
                    }
                }
            });
        }
    }
}
