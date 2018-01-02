package com.skj.wheel.album.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.skj.wheel.R;
import com.skj.wheel.album.adapter.AlbumGridViewAdapter;
import com.skj.wheel.album.utils.AlbumHelper;
import com.skj.wheel.album.utils.Bimp;
import com.skj.wheel.album.utils.CacheActivityUtil;
import com.skj.wheel.album.utils.ImageBucket;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.album.utils.IntentUtil;
import com.skj.wheel.album.utils.PublicWay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {
    // 显示手机里的所有图片的列表控件
    private GridView gridView;
    // 当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    // gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    private TextView back, other, title;
    private TextView preview, okNum;
    private LinearLayout okLayout;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(this);
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_album);
        back = (TextView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        other = (TextView) findViewById(R.id.other);
        preview = (TextView) findViewById(R.id.preview);
        okNum = (TextView) findViewById(R.id.ok_buttonnum);
        okLayout = (LinearLayout) findViewById(R.id.ok_buttonlayout);

        gridView = (GridView) findViewById(R.id.myGrid);

        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);

        okNum.setText(Bimp.tempSelectBitmap.size() + "");
        other.setVisibility(View.VISIBLE);
        other.setText("相册");
        title.setText("全部图片");
        other.setOnClickListener(this);
        back.setOnClickListener(this);
        preview.setOnClickListener(this);
        okLayout.setOnClickListener(this);
    }

    private void initData() {
        loadData();
        // 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);

        okNum.setText(Bimp.tempSelectBitmap.size() + "");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();
        } else if (i == R.id.other) {
            IntentUtil.startActivity(this, ImageFileActivity.class, null);
        } else if (i == R.id.preview) {
            if (Bimp.tempSelectBitmap.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("position", "1");
                IntentUtil.startActivity(this, ViewGalleryActivity.class, map);
            } else {
                Toast.makeText(this, "您未选择图片！", Toast.LENGTH_SHORT);
            }
        } else if (i == R.id.ok_buttonlayout) {
            finish();
        }
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                contentList = helper.getImagesBucketList(false);
                dataList = new ArrayList<ImageItem>();
                for (int i = 0; i < contentList.size(); i++) {
                    dataList.addAll(contentList.get(i).imageList);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        gridImageAdapter = new AlbumGridViewAdapter(AlbumActivity.this, dataList,
                                Bimp.tempSelectBitmap);
                        gridView.setAdapter(gridImageAdapter);
                        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked,
                                                    Button chooseBt) {
                                if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
                                    toggleButton.setChecked(false);
                                    chooseBt.setVisibility(View.GONE);
                                    if (!removeOneData(dataList.get(position))) {
                                        Toast.makeText(AlbumActivity.this, R.string.only_choose_num, Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                                if (isChecked) {
                                    chooseBt.setVisibility(View.VISIBLE);
                                    Bimp.tempSelectBitmap.add(dataList.get(position));
                                    okNum.setText(Bimp.tempSelectBitmap.size() + "");
                                } else {
                                    Bimp.tempSelectBitmap.remove(dataList.get(position));
                                    chooseBt.setVisibility(View.GONE);
                                    okNum.setText(Bimp.tempSelectBitmap.size() + "");
                                }
                                okNum.setText(Bimp.tempSelectBitmap.size() + "");
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private boolean removeOneData(ImageItem imageItem) {
        if (Bimp.tempSelectBitmap.contains(imageItem)) {
            Bimp.tempSelectBitmap.remove(imageItem);
            okNum.setText(Bimp.tempSelectBitmap.size() + "");
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        okNum.setText(Bimp.tempSelectBitmap.size() + "");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
