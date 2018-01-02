package com.skj.wheel.album.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.skj.wheel.album.utils.Bimp;
import com.skj.wheel.album.utils.CacheActivityUtil;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.album.utils.IntentUtil;
import com.skj.wheel.album.utils.PublicWay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class ShowAllPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private AlbumGridViewAdapter gridImageAdapter;
    public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

    private TextView back, other, title, tv;
    private TextView preview, okNum;
    private LinearLayout okLayout;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(this);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_album);

        back = (TextView) findViewById(R.id.back);
        other = (TextView) findViewById(R.id.other);
        preview = (TextView) findViewById(R.id.preview);
        okNum = (TextView) findViewById(R.id.ok_buttonnum);
        okLayout = (LinearLayout) findViewById(R.id.ok_buttonlayout);
        title = (TextView) findViewById(R.id.title);
        tv = (TextView) findViewById(R.id.myText);
        gridView = (GridView) findViewById(R.id.myGrid);
        this.intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        if (!folderName.equals("")) {
            if (folderName.length() > 8) {
                folderName = folderName.substring(0, 9) + "...";
            }
            title.setText(folderName);
        }

        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, Bimp.tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        gridView.setEmptyView(tv);
        okNum.setText(Bimp.tempSelectBitmap.size() + "");
        other.setVisibility(View.VISIBLE);
        other.setText("取消");

        other.setOnClickListener(this);
        back.setOnClickListener(this);
        preview.setOnClickListener(this);
        okLayout.setOnClickListener(this);
        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button button) {
                if (Bimp.tempSelectBitmap.size() >= PublicWay.num && isChecked) {
                    button.setVisibility(View.GONE);
                    toggleButton.setChecked(false);
                    Toast.makeText(ShowAllPhotoActivity.this, "超出可选图片张数", 2000);
                    return;
                }

                if (isChecked) {
                    button.setVisibility(View.VISIBLE);
                    Bimp.tempSelectBitmap.add(dataList.get(position));
                    okNum.setText(Bimp.tempSelectBitmap.size() + "");
                } else {
                    button.setVisibility(View.GONE);
                    Bimp.tempSelectBitmap.remove(dataList.get(position));
                    okNum.setText(Bimp.tempSelectBitmap.size() + "");
                }
                okNum.setText(Bimp.tempSelectBitmap.size() + "");
            }
        });

    }

    private void initData() {
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
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
            CacheActivityUtil.finishOtherActivity(ShowAllPhotoActivity.class);
            finish();
        } else if (i == R.id.preview) {
            if (Bimp.tempSelectBitmap.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("position", "1");
                IntentUtil.startActivity(this, ViewGalleryActivity.class, map);
            } else {
                Toast.makeText(this, "您未选择图片！", 2000);
            }
        } else if (i == R.id.ok_buttonlayout) {
            CacheActivityUtil.finishOtherActivity(ShowAllPhotoActivity.class);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        okNum.setText(Bimp.tempSelectBitmap.size() + "");
    }
}
