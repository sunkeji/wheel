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
     


