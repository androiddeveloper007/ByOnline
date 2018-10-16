package com.bowen.tcm.healthcare.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.RouterActivityUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.gallery.utils.ScreenUtils;
import com.bowen.tcm.R;
import com.bowen.tcm.common.base.BaseActivity;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.event.ColumnSortEvent;
import com.bowen.tcm.common.model.DragItemTouchHelper;
import com.bowen.tcm.common.widget.GridOffsetsItemDecoration;
import com.bowen.tcm.healthcare.adapter.ColumnsSortAdapter;
import com.bowen.tcm.healthcare.contract.ColumnsSortContract;
import com.bowen.tcm.healthcare.presenter.ColumnsSortPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/8.
 */

public class ColumnsSortActivity extends BaseActivity implements DragItemTouchHelper.DragItemTouchBack,ColumnsSortContract.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ColumnsSortAdapter mAdapter;
    private List<Column> interestList;
    private ColumnsSortPresenter presenter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemStatusBar(R.color.color_00);
        setContentView(R.layout.activity_columns_sort);
        ButterKnife.bind(this);

        setTitle("栏目排序");
        getTitleBar().getRightTextButton().setVisibility(View.VISIBLE);
        getTitleBar().getRightTextButton().setText("完成");

        init();

    }

    private void init(){
        presenter = new ColumnsSortPresenter(this, this);
        interestList = (List<Column>) RouterActivityUtil.getSerializable(this);
        for(Column column:interestList){//删除推荐栏目
            if(column.getColumnId().equals("recommend")){
                interestList.remove(column);
                break;
            }
        }
        StringBuilder buffer = new StringBuilder();
        boolean flag=false;
        for(Column item : interestList){
            if(!flag){
                buffer.append(item.getColumnId());
                flag = true;
            }else{
                buffer.append(","+item.getColumnId());
            }
        }
        /**
         * 下面是内容展示RecyclerView，
         * 这里主要为了使ScrollView整体滑动以及item能够在ScrollView下实现match_parent
         * 因此并未绑定拖拽事件
         */
        linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }//返回false禁止RecyclerView竖直滚动
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if(EmptyUtils.isNotEmpty(interestList)) {
            mAdapter = new ColumnsSortAdapter(this);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);//, GridLayoutManager.VERTICAL, false
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(mAdapter);

            GridOffsetsItemDecoration offsetsItemDecoration = new GridOffsetsItemDecoration(
                    GridOffsetsItemDecoration.GRID_OFFSETS_VERTICAL);
            offsetsItemDecoration.setVerticalItemOffsets(ScreenUtils.dip2px(this, 1));
            offsetsItemDecoration.setHorizontalItemOffsets(ScreenUtils.dip2px(this, 1));
            mRecyclerView.addItemDecoration(offsetsItemDecoration);

            ItemTouchHelper.Callback callback = new DragItemTouchHelper(this);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);

            mAdapter.setNewData(interestList);

        } else {
            ToastUtil.getInstance().showToastDialog("栏目列表数据为空");
        }
    }

    @Override
    public void onRightButtonPressed() {
        super.onRightButtonPressed();
        presenter.sortRequest(interestList);
    }

    @Override
    public void onDragItemTouch(int oldPosition, int newPosition) {
        interestList.add(newPosition, interestList.remove(oldPosition));
        mAdapter.notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onSortSuccess() {
        EventBus.getDefault().post(new ColumnSortEvent());
        finish();
    }
}
