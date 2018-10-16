package com.bowen.tcm.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ImageLoaderUtil;
import com.bowen.commonlib.widget.CircleImageView;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.network.BannerInfo;

import java.util.ArrayList;

/**
 * Created by AwenZeng on 2017/11/22.
 */


public class TopBannerAdapter extends RecyclerView.Adapter<TopBannerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BannerInfo> bannerList;

    public interface  OnBannerItemClickListener{
      void  onItemClick(BannerInfo bannerInfo);
    }

    private OnBannerItemClickListener onBannerItemClickListener;

    public TopBannerAdapter(Context context) {
        this.context = context;
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public TopBannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_top_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopBannerAdapter.ViewHolder holder, int position) {
        final int pos = position % bannerList.size();
       BannerInfo bannerInfo = bannerList.get(pos);
       ImageLoaderUtil.getInstance().show(context,bannerInfo.getFileLink(),holder.bannerImg,R.drawable.top_news_default_bg);
       if(EmptyUtils.isNotEmpty(bannerInfo.getBannerTitle())){
           holder.bannerTitleTv.setVisibility(View.VISIBLE);
           holder.bannerTitleTv.setText(bannerInfo.getBannerTitle());
       }else{
           holder.bannerTitleTv.setVisibility(View.GONE);
       }
       holder.bannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(bannerList.get(pos));
                }

            }
        });
    }

    public ArrayList<BannerInfo> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerInfo> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView bannerImg;
        TextView bannerTitleTv;

        ViewHolder(View itemView) {
            super(itemView);
            bannerImg = (CircleImageView) itemView.findViewById(R.id.mBannerImg);
            bannerTitleTv = (TextView) itemView.findViewById(R.id.mBannerTitileTv);
        }
    }

}
