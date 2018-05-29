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
  private int leftMenuId;//向右滑动显示左边菜单

    private View leftMenuView;
   
    private int rightMenuId;//向左滑动显示右边菜单

    private View rightMenuView;
   
    private int contentId;//中间自定义内容

    private View contentView;
   
   ```
 
 # 4、Gestures手势操作（手势放大缩小）
 
 # 5、自定义view
 a、自定义密码框设置、获取、清空等操作
  ```
  <com.skj.wheel.definedview.GridPasswordView
            android:layout_width="match_parent"
            android:layout_height="@dimen/x50"
            android:layout_margin="@dimen/x20" />
   
    <declare-styleable name="gridPasswordView">

        <attr name="gpvTextColor" format="color|reference" />
        <attr name="gpvTextSize" format="dimension" />

        <attr name="gpvLineColor" format="color" />
        <attr name="gpvGridColor" format="color" />
        <attr name="gpvLineWidth" format="dimension" />
        <attr name="gpvGridCornerRadius" format="integer" />
        <attr name="gpvPasswordLength" format="integer" />
        <attr name="gpvPasswordTransformation" format="string" />

        <attr name="gpvPasswordType" format="enum">
            <enum name="numberPassword" value="0" />
            <enum name="textPassword" value="1" />
            <enum name="textVisiblePassword" value="2" />
            <enum name="textWebPassword" value="3" />
        </attr>
    </declare-styleable>
   ```
  b、 LayoutView为自定义的view主要是在列表显示时数据为空、错误、loading展示的界面
   ```
     <com.skj.wheel.definedview.LayoutView
        android:id="@+id/layout_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/text_color"
        android:visibility="gone" />
        
 //        layoutView.setVisibility(View.VISIBLE);
//        layoutView.showEmpty(R.mipmap.ic_launcher_round, "空！");
//        layoutView.showError(R.mipmap.ic_launcher_round, "错误！");
//        layoutView.showLoading(R.mipmap.ic_launcher_round, "loading！");
   
   ```
   c、 MyETView为自定义的输入框自带清除按键并对一些特殊字符进行限制
  
    ```
     <com.skj.wheel.definedview.MyETView
            android:layout_width="match_parent"
            android:layout_height="@dimen/x50"
            android:layout_margin="@dimen/x20" />
     ```
  
   
  d、MyTBView自定义发送验证码按钮
  
    ```
     <com.skj.wheel.definedview.MyTBView
            android:layout_width="match_parent"
            android:layout_height="@dimen/x50"
            android:layout_margin="@dimen/x20" />
   
   
     ```
 e、MyRGView和MyRBView为模仿radiogroup和radiobutton自定义的view可实现单选自定义view功能
  
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:orientation="vertical">

    <com.skj.wheel.definedview.MyRGView
        android:id="@+id/rg"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/white"
        android:orientation="vertical">

        <com.skj.wheel.definedview.MyRBView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:rb_checked="true"
            app:rb_logo="@mipmap/ic_launcher"
            app:rb_radio="@drawable/check_box_btn"
            app:rb_sign="1"
            app:rb_title="微信" />

        <com.skj.wheel.definedview.MyRBView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:rb_checked="true"
            app:rb_logo="@mipmap/ic_launcher"
            app:rb_radio="@drawable/check_box_btn"
            app:rb_sign="1"
            app:rb_title="微信" />
      </com.skj.wheel.definedview.MyRGView>
    </LinearLayout>
    
    
    <!--  自定义radiobutton和radiobutton组-->
    <declare-styleable name="MyRBView">
        <attr name="rb_title" format="reference|string" />
        <attr name="rb_logo" format="reference|color" />
        <attr name="rb_sign" format="reference|integer" />
        <attr name="rb_checked" format="boolean" />
        <attr name="rb_radio" format="reference" />
    </declare-styleable>
    <declare-styleable name="MyRidioGroup">
        <attr name="checkedButton" format="integer" />
    </declare-styleable>
    
    
     rg.setOnCheckedChangeListener(new MyRGView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRGView group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((MyRBView) group.getChildAt(i)).setChangeImg(checkedId);
                }
                if (rb1.isChecked()) {
                 
                } else if (rb2.isChecked()) {
               
                }
            }
        });
    ```

   
  f、MyTGView自定义标签
    public void setTags(List<String> tagList) {}
   public void setOnTagClickListener(OnTagClickListener l) {}
   
  
        ```
   <?xml version="1.0" encoding="utf-8"?>
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.skj.wheel.definedview.MyTGView
            android:id="@+id/tg_text1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="@dimen/x15"
            app:atg_background="@drawable/corners_bgall_color"
            app:atg_borderColor="@color/transparent"
            app:atg_checkedBackgroundColor="@color/tag_bg_color1"
            app:atg_checkedTextColor="@color/main_color"
            app:atg_horizontalPadding="@dimen/x20"
            app:atg_horizontalSpacing="@dimen/x10"
            app:atg_isAppendMode="true"
            app:atg_textColor="@color/text_color"
            app:atg_textSize="@dimen/sp12"
            app:atg_verticalPadding="@dimen/x10"
            app:atg_verticalSpacing="@dimen/x10" />
    </RelativeLayout>

</RelativeLayout>
   <declare-styleable name="TagGroup">
        <!-- Whether the tag group is in append mode. -->
        <attr name="atg_isAppendMode" format="boolean" />
        <!-- If the tag group is in append mode, what the hint of input tag. -->
        <attr name="atg_inputHint" format="string" />

        <!-- The tag view outline border color. -->
        <attr name="atg_borderColor" format="color" />
        <!-- The tag view text color. -->
        <attr name="atg_textColor" format="color" />
        <!-- The tag view background color. -->
        <attr name="atg_backgroundColor" format="color" />

        <attr name="atg_background" format="reference" />
        <!-- The dash outline border color, when in append mode. -->
        <attr name="atg_dashBorderColor" format="color" />
        <!-- The input tag hint text color, when in append mode. -->
        <attr name="atg_inputHintColor" format="color" />
        <!-- The input tag type text color, when in append mode. -->
        <attr name="atg_inputTextColor" format="color" />

        <!-- The checked tag view outline border color. -->
        <attr name="atg_checkedBorderColor" format="color" />
        <!-- The checked text color. -->
        <attr name="atg_checkedTextColor" format="color" />
        <!-- The checked marker color. -->
        <attr name="atg_checkedMarkerColor" format="color" />
        <!-- The checked tag view background color. -->
        <attr name="atg_checkedBackgroundColor" format="color" />

        <!-- The tag view background color, when the tag view is being pressed. -->
        <attr name="atg_pressedBackgroundColor" format="color" />

        <!-- The tag view outline border stroke width. -->
        <attr name="atg_borderStrokeWidth" format="dimension" />
        <!-- The tag view text size. -->
        <attr name="atg_textSize" format="dimension" />

        <attr name="atg_horizontalSpacing" format="dimension" />
        <attr name="atg_verticalSpacing" format="dimension" />
        <attr name="atg_horizontalPadding" format="dimension" />
        <attr name="atg_verticalPadding" format="dimension" />
      </declare-styleable>
      <declare-styleable name="Themes">
        <attr name="tagGroupStyle" format="reference" />
        <attr name="editStyle" format="reference" />
      </declare-styleable>
           ```



   g、ShSwitchView自定义滑块按钮
   
       ```
      <com.skj.wheel.definedview.ShSwitchView
            android:layout_width="match_parent"
            android:layout_height="@dimen/x50"
            android:layout_margin="@dimen/x20" />
   
    <!-- 选择滑块 -->
    <declare-styleable name="ShSwitchView">
        <attr name="tintColor" format="reference|color" />
        <attr name="outerStrokeWidth" format="reference|dimension" />
        <attr name="shadowSpace" format="reference|dimension" />
    </declare-styleable>
        ```

   h、SideLetterBarView自定义字母栏列表默认显示完整，可自定义设置显示数量
   textOverly.setVisibility(View.GONE);
        sideLetterBarView.setOverlay(textOverly);
        选择到的字母放大显示
        
       ```
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">


        <com.skj.wheel.definedview.SideLetterBarView
            android:id="@+id/side_letter_bar"
            android:layout_width="20dp"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:background="@color/white"
            app:letter_text_color="@color/main_color"
            app:letter_text_selectcolor="@color/text_color"
            app:letter_text_size="@dimen/sp13"
            tools:ignore="RtlHardcoded" />

        <TextView
            android:id="@+id/text_overly"
            android:layout_width="120dp"
            android:layout_height="120dp"
            android:layout_centerInParent="true"
            android:background="@drawable/cp_overlay_bg"
            android:gravity="center"
            android:textColor="@android:color/white"
            android:textSize="48sp"
            android:textStyle="bold"
            android:visibility="gone" />
    </RelativeLayout>
    </LinearLayout>
    <!-- 字母栏 -->
    <declare-styleable name="SideLetterBarView">
        <attr name="letter_text_color" format="color" />
        <attr name="letter_text_selectcolor" format="color" />
        <attr name="letter_text_size" format="dimension" />
    </declare-styleable>
    
      private TreeMap<String, Object> letterIndexes;
    String[] b = {"A", "B", "D", "E", "F", "H", "I", "K", "M", "N", "O", "P", "T", "U", "V", "W", "X", "Y", "Z"};

    private void initView() {

        letterIndexes = new TreeMap<>();
        for (int index = 0; index < b.length; index++) {
            letterIndexes.put(b[index], index);
        }
        sideLetterBarView.setLetterList(letterIndexes);
        textOverly.setVisibility(View.GONE);
        sideLetterBarView.setOverlay(textOverly);
        sideLetterBarView.setOnLetterChangedListener(new SideLetterBarView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
            }
        });
        }
        ```

i、滑轮选择效果（下方为常用三种类型，其他根据需要可自定义）
//三级省市区联动
 SeletorCityView.getInstance().showOptions(this, new SeletorCityView.CitysPickerCallBack() {
                    @Override
                    public void onCitysSelect(String province, String city, String district, String cityCode) {

                    }
                });
  //单列滑轮
 SelectorSingleView.alertBottomWheelOption(this, stringList1, new SelectorSingleView.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        
                    }
                });
//年月日联动（可自定义类型）
 SelectorDateView.alertTimerPicker(this, TimePickerView.Type.ALL, "yyyy-MM-dd", new SelectorDateView.TimerPickerCallBack() {
            @Override
            public void onTimeSelect(String date) {
                
            }
        });
j、文字滚动播放MyTSView
 <com.skj.wheel.definedview.MyTSView
        android:id="@+id/ts_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
     String[] s = new String[]{"ceshi", "cddjdjjd"};
        tsView.setmAdvertisements(s);
        
 # 6、自定义工具类
 a、ActivityListUtil:管理项目中所有的activity页面
 b、ClickUtil：对点击事件加延迟
 c、DisplayUtil：分辨率适配
 d、IntentUtil：跳转
 e、LogUtil：日志
 f、PerMaUtil:android6.0以后动态权限管理
 g、SPUtil：SharedPreferences工具
 h、TextUtil：自定义文本工具
 i、TimeUtil：时间工具
 f、ToastUtil：提示工具
 g、Glide:图片处理工具















