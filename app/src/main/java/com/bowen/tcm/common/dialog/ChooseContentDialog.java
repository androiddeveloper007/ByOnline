package com.bowen.tcm.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bowen.commonlib.base.BaseDialog;
import com.bowen.commonlib.base.BaseQuickAdapter;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.dialog.adapter.ChooseContentAdapter;
import com.bowen.tcm.mine.activity.SelfDefinitionActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AwenZeng on 2016/12/13.
 */

public class ChooseContentDialog extends BaseDialog {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTitleTv)
    TextView mTitleTv;
    private ArrayList<String> list;
    private String titleStr;
    private String selectStr = "";
    private ChooseContentAdapter mAdapter;

    public ChooseContentDialog(Context context) {
        super(context, R.style.dialog_transparent_style);
        list = new ArrayList<>();
    }
    public ChooseContentDialog(Context context, String choosePositionStr) {
        super(context, R.style.dialog_transparent_style);
        list = new ArrayList<>();
        selectStr = choosePositionStr;
    }

    public ChooseContentDialog(Context context, int themeResId) {
        super(context, themeResId);
        list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_content);
        ButterKnife.bind(this);
        getWindow().getAttributes().width = ScreenSizeUtil.getScreenWidth();
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialogAnim);  //添加动画
        mAdapter = new ChooseContentAdapter(mContext);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setChoosePosStr(selectStr);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String temp = list.get(position);
                if (temp.contains("自定义")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(RouterActivityUtil.FROM_TAG,SelfDefinitionActivity.TYPE_FROM_DISEASE_NAME);
                    RouterActivityUtil.startActivityResult((Activity) mContext, SelfDefinitionActivity.class,SelfDefinitionActivity.REQUEST_CODE,bundle);
                }else{
                    mListener.onDataCallBack(temp);
                }
                dismiss();
            }
        });
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(selectStr)||!selectStr.equals("请选择疾病名称")&&list.get(i).equals("自定义")){
                mAdapter.setChoosePos(i);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        if(EmptyUtils.isNotEmpty(titleStr)){
            mTitleTv.setText(titleStr);
        }
        setCanceledOnTouchOutside(true);
    }

    public void setList(ArrayList<String> mList) {
        this.list = mList;
    }

    public void setTitleStr(String mTitleStr) {
        this.titleStr = mTitleStr;
    }

    public void setSelectStr(String selectStr){
        this.selectStr = selectStr;
    }

}
