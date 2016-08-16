package com.zhouyou.blurred;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhouyou.blurred.adapter.ListViewAdapter;

/**
 * 作者：ZhouYou
 * 日期：2016/8/16.
 */
public class BlurEffectActivity extends Activity {

    private BlurView blurView;
    private ListView listView;

    private ListViewAdapter adapter;

    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = getResources().getDisplayMetrics();
        setContentView(R.layout.activity_blur_effect);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        blurView = (BlurView) findViewById(R.id.blur_view);

        View header = LayoutInflater.from(this).inflate(R.layout.view_header, null);
        TextView tvText = (TextView) header.findViewById(R.id.tv_text);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels);
        tvText.setLayoutParams(tvParams);

        listView = (ListView) findViewById(R.id.list_view);
        listView.addHeaderView(header, null, false);
        adapter = new ListViewAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new OnScrollBlurListener(blurView));
    }

    private static class OnScrollBlurListener implements AbsListView.OnScrollListener {
        private int currentFirstVisibleItem = 0;
        private SparseArray recordSp = new SparseArray(0);

        private BlurView blurView;

        public OnScrollBlurListener(BlurView blurView) {
            this.blurView = blurView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            currentFirstVisibleItem = firstVisibleItem;
            View childView = view.getChildAt(0);
            if (null != childView) {
                ItemRecord itemRecord = (ItemRecord) recordSp.get(firstVisibleItem);
                if (null == itemRecord) {
                    itemRecord = new ItemRecord();
                }
                itemRecord.height = childView.getHeight();
                itemRecord.top = childView.getTop();
                recordSp.append(firstVisibleItem, itemRecord);
                int h = getScrollY(); //滚动距离
                if (h > 1000) {
                    blurView.setBlurredLevel(100);
                } else {
                    blurView.setBlurredLevel(Math.abs(h) / 10);
                }
            }
        }

        private int getScrollY() {
            int height = 0;
            for (int i = 0; i < currentFirstVisibleItem; i++) {
                ItemRecord itemRecord = (ItemRecord) recordSp.get(i);
                height += itemRecord.height;
            }
            ItemRecord itemRecord = (ItemRecord) recordSp.get(currentFirstVisibleItem);
            if (null == itemRecord) itemRecord = new ItemRecord();
            return height - itemRecord.top;
        }

        class ItemRecord {
            int height = 0;
            int top = 0;
        }
    }
}
