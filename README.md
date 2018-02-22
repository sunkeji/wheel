# wheel
android 开发UI 一些常用的控件，我在android应用开发的过程中遇到的常用的自定义控件和必用的工具类

1、按字母排序功能
2、自定义密码
3、滑轮菜单
4、相册选择图片
5、手势放大缩小
6、自定义标签
7、列表下拉刷新上拉加载更多

# 导入

  compile 'com.wheel:tools:1.1.0'

# 1、相册图片多选功能
   此功能为应用的常用的类似朋友圈功能中选择相册图片的多选功能，包括图片多选、预览、删除。
   此功能为封装好的调用 
   ```
  IntentUtil.startActivity(this, AlbumAllActivity.class); 
   
   ```
  下一步就是一系列操作了，等选择图片成功以后 所有的图片都封装在
 ```
public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>(); // 选择的图片的临时列表
     
public class ImageItem implements Serializable {//图片的对象属性
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    private Bitmap bitmap;
    public boolean isSelected = false;
}

ImageItem imageItem=Bimp.tempSelectBitmap.get(i);//获取选择的图片 
Bimp.tempSelectBitmap.clear();//清除选择的图片 （上传成功后记得调用此方法清除缓存的图片）
    
   ```
 # 2、城市列表选择功能
   应用常用的城市列表选择功能，包括城市自动定位、热门城市、列表、精确搜索和自定义字母栏搜索。
  ```
  //   初始化 
     private CityListAdapter mCityAdapter;//包括城市自动定位、热门城市、列表
     private ResultListAdapter mResultAdapter;//精确搜索
     
     setContentView(R.layout.cp_activity_city_list);
     DBManager  dbManager = new DBManager(this);
     dbManager.copyDBFile();
     List<City> mAllCities  = dbManager.getAllCities();
   
   ```
 
   ```
   private void initView() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
//                back(name);
            }

            @Override
            public void onLocateClick() {
//                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                mCityAdapter.updateLocateState(LocateState.SUCCESS, "重庆市");
            }
        });
        mCityAdapter.updateLocateState(LocateState.SUCCESS, "重庆市");
        mResultAdapter = new ResultListAdapter(this, null);


        listviewAllCity.setAdapter(mCityAdapter);
        sideLetterBar.setOverlay(textOverly);
        listviewSearchResult.setAdapter(mResultAdapter);
        sideLetterBar.setOnLetterChangedListener(new SideLetterBarView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                listviewAllCity.setSelection(position);
            }
        });
        listviewSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                back(mResultAdapter.getItem(position).getName());
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    ivSearchClear.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    listviewSearchResult.setVisibility(View.GONE);
                } else {
                    ivSearchClear.setVisibility(View.VISIBLE);
                    listviewSearchResult.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });
    }
   
   ```
     
 # 3、RecyclerView列表
            
  RecyclerView为Android在listview基础上优化的很强大的列表控件，在android应用开发中必不可少的控件，故此为了使用方便优化封装一下以便使用，可参考
  
        
  ```
  <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.skj.wheel.swiperecyclerview.MyRecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:layout_below="@+id/layout_view"
        app:orientation="0"
        app:type="1" />

    <com.skj.wheel.definedview.LayoutView
        android:id="@+id/layout_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/text_color"
        android:visibility="gone" />
</RelativeLayout>
   

  ```
  
  ```
   <!-- recyclerview的属性 -->
    <declare-styleable name="MyRecyclerView">
        <attr name="orientation" format="integer" />
        <attr name="type" format="integer" />
    </declare-styleable>
    private ItemDecoration itemDecoration;//分割线
    private int orientation = 1;//recyclerview 列表展现形式 垂直1或水平0
    private int type = 0;//recyclerview 列表展现形式 列表1或网格0
   
  ```
  MyRecyclerView 为集成 RecyclerView的封装的自定义view
  LayoutView为自定义的view主要是在列表显示时数据为空、错误、loading展示的界面（在下面会介绍）
 
  ```
  app:orientation="0"列表展现形式 垂直1或水平0
  app:type="1" 列表展现形式 列表1或网格0
  removeDivider（） //移除分割线 
   /**
     * 设置网格GridLayoutManager
     */
    private GridLayoutManager gridLayoutManager;

    public void setGridLayoutManager(Context context, int spaceCount) {
        gridLayoutManager = new GridLayoutManager(context, spaceCount);
        this.setLayoutManager(gridLayoutManager);
        itemDecoration = new GridItemDecoration(DisplayUtil.dip2px(10));
        this.addItemDecoration(itemDecoration);
    }

   /**
     * 线性自定义分割线
     *
     * @param space        分割线的宽度
     * @param mOrientation 垂直列表分割线0:top、3:bottom默认3;水平列表分割线1：left、2:right默认2
     */
    public void setLinearItemDecoration(int space, int mOrientation) {
        this.removeItemDecoration(itemDecoration);
        itemDecoration = new LinearItemDecoration(space, mOrientation);
        this.addItemDecoration(itemDecoration);
    }

    /**
     * 网格自定义分割线
     *
     * @param space 分割线的宽度
     */
    public void setGridItemDecoration(int space) {
        this.removeItemDecoration(itemDecoration);
        itemDecoration = new GridItemDecoration(space);
        this.addItemDecoration(itemDecoration);
    }

  
  ```
  有一种情况当列表为网格时且为不规则时配合
   /**
     * 设置不规则网格
     *
     * @param mList
     */
    public void setGridlayoutSpaceCount(final List<?> mList) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position, mList);
            }
        });
    }
    使用
 ```
 <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.skj.wheel.swiperecyclerview.MySwipeRLView
        android:id="@+id/swipe_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.skj.wheel.swiperecyclerview.MyRecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_alignParentLeft="true"
            android:layout_alignParentStart="true"
            android:layout_below="@+id/layout_view"
            app:orientation="0"
            app:type="0" />
    </com.skj.wheel.swiperecyclerview.MySwipeRLView>

    <com.skj.wheel.definedview.LayoutView
        android:id="@+id/layout_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/text_color"
        android:visibility="gone" />
</RelativeLayout>
  ```

MySwipeRLView为集成SwipeRefreshLayout的自定义控件，SwipeRefreshLayout为android自带的功能

MySwipeRLView配合 MyRecyclerView实现列表自带下拉刷新上拉加载更多

  ```
    swipeRefresh.setOnSwipeListener(new MySwipeRLView.OnSwipeListener() {//下拉刷新
            @Override
            public void onRefresh() {
               mList.clear();
                for (int i = 0; i < 10; i++) {
                    mList.add("卧室的多多多" + i);
                }
                adapter.updateList(mList);
            }
        });

        recyclerView.setOnBottomListener(new MyRecyclerView.OnBottomListener() {//上拉加载更多
            @Override
            public void onLoadMore() {
                  for (int i = 0; i < 10; i++) {
                    mList.add("卧室的多多多" + i);
                }
                adapter.updateList(mList);
            }
        });

    }
 ```


 ```
 <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <com.skj.wheel.swiperecyclerview.SwipeMenuLayout
        android:id="@+id/swipeMenuLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:contentId="@+id/content_view"
        app:leftMenuId="@+id/left_menu"
        app:rightMenuId="@+id/right_menu">

        <LinearLayout
            android:id="@+id/left_menu"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <TextView
                android:layout_width="80dp"
                android:layout_height="match_parent"
                android:background="@android:color/holo_blue_dark"
                android:gravity="center"
                android:text="LeftMenu"
                android:textColor="@android:color/white">

            </TextView>
        </LinearLayout>

        <RelativeLayout
            android:id="@+id/content_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <include layout="@layout/cp_view_hot_city" />

        </RelativeLayout>

        <LinearLayout
            android:id="@+id/right_menu"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <TextView
                android:layout_width="80dp"
                android:layout_height="match_parent"
                android:background="#D9DEE4"
                android:gravity="center"
                android:text="Top"
                android:textColor="@android:color/white" />

            <TextView
                android:id="@+id/tv_add"
                android:layout_width="80dp"
                android:layout_height="match_parent"
                android:background="#ECD50A"
                android:gravity="center"
                android:text="Add"
                android:textColor="@android:color/white"
                android:textSize="16sp" />

            <TextView
                android:id="@+id/tv_delete"
                android:layout_width="80dp"
                android:layout_height="match_parent"
                android:background="#FF4A57"
                android:gravity="center"
                android:text="Delete"
                android:textColor="@android:color/white"
                android:textSize="16sp" />

        </LinearLayout>

    </com.skj.wheel.swiperecyclerview.SwipeMenuLayout>

</LinearLayout>
  ```
SwipeMenuLayout为实现列表中一种滑动item时想弹出菜单时的功能
  ```
    /**
     * 向右滑动显示左边菜单
     */
    private int leftMenuId;

    private View leftMenuView;
    /**
     * 向左滑动显示右边菜单
     */
    private int rightMenuId;

    private View rightMenuView;
    /**
     * 中间自定义内容
     */
    private int contentId;

    private View contentView;

  
    ```































