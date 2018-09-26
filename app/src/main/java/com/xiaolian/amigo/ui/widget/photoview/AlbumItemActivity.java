package com.xiaolian.amigo.ui.widget.photoview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览器
 *
 */
public class AlbumItemActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = AlbumItemActivity.class.getSimpleName();
    /**
     * 图片地址传入类型<br/>
     * <b>List&#060;String(url)&#062;</b><br/>
     */
    public static final String EXTRA_TYPE_LIST = "List";
    /**
     * 图片地址传入类型<br/>
     * <b>String(url) []</b><br/>
     */
    public static final String EXTRA_TYPE_ARRAY = "Array";
    /**
     * 图片地址传入类型<br/>
     * <b>String(url)</b><br/>
     */
    public static final String EXTRA_TYPE_SINGLE = "Single";
    /**
     * 图片地址传入类型<br/>
     * <b>String("url,url,url")</b><br/>
     */
    public static final String EXTRA_TYPE_SEPARATOR = "Separator";
    /**
     * 当前位置
     */
    public static final String EXTRA_CURRENT = "current";


    private AlbumViewPager mViewPager;//显示大图
    private ImageView mBackView;
    private TextView mCountView;
    private View mHeaderBar, mBottomBar;
    private TextView mDelete;
    private Button mDeleteButton;
    public static final String INTENT_ACTION = "intent_action";
    public static final String INTENT_POSITION = "intent_position";
    public static final int ACTION_NORMAL = 1;
    public static final int ACTION_DELETEABLE = 2;
    private int action = ACTION_NORMAL;

    //0是网络图片无pop，1是本地图片有pop，2是本地图片没有pop
    public static int POPANDURL = 0;
    /**
     * 当前位置
     */
    private int mCurrent;
    /**
     * 图片地址
     */
    private List<String> mPaths = new ArrayList<String>();

    private List<String> deletePaths;
    private int position;

    /**
     * 跳转至大图
     * @param context
     * @param type
     * @param data
     */
    public static void start(Context context, String type, String data) {
        if (context == null || TextUtils.isEmpty(type) || TextUtils.isEmpty(data)) {
            return;
        }
        Intent intent = new Intent(context, AlbumItemActivity.class);
        intent.putExtra(type, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_albumitem);
        mViewPager = (AlbumViewPager) findViewById(R.id.albumviewpager);
        mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
        mCountView = (TextView) findViewById(R.id.header_bar_photo_count);
        mHeaderBar = findViewById(R.id.album_item_header_bar);
        mBottomBar = findViewById(R.id.album_item_bottom_bar);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mDelete = (TextView) findViewById(R.id.tv_delete);

        mBackView.setOnClickListener(this);
        mCountView.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mViewPager.setOnSingleTapListener(super::onBackPressed);

        setPath();
        if (action == ACTION_DELETEABLE) {
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.GONE);
        }
        deletePaths = new ArrayList<String>();
        deletePaths.addAll(mPaths);
        loadAlbum();
    }

    /**
     * 设置图片路径
     */
    public void setPath() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            return;
        }
        try {
            position = getIntent().getIntExtra(INTENT_POSITION, -1);
            action = getIntent().getIntExtra(INTENT_ACTION, ACTION_NORMAL);
            mPaths.clear();
            mCurrent = getIntent().getIntExtra(EXTRA_CURRENT, 0);
            String pathSingle = getIntent().getStringExtra(EXTRA_TYPE_SINGLE);
            if (pathSingle != null) {
                mPaths.add(Constant.IMAGE_PREFIX + pathSingle);
                return;
            }
            List<String> paths = getIntent().getStringArrayListExtra(EXTRA_TYPE_LIST);
            if (paths != null) {
                for (String image : paths) {
                    mPaths.add(Constant.IMAGE_PREFIX + image);
                }
                return;
            }
            String[] pathArray = getIntent().getStringArrayExtra(EXTRA_TYPE_ARRAY);
            if (pathArray != null) {
                for (int i = 0; i < pathArray.length; i++) {
                    mPaths.add(pathArray[i]);
                }
                return;
            }
            String pathSeparator = getIntent().getStringExtra(EXTRA_TYPE_SEPARATOR);
            if (pathSeparator != null) {
                String urls[] = pathSeparator.split(",");
                for (int i = 0; i < urls.length; i++) {
                    mPaths.add(urls[i]);
                }
                return;
            }
        } catch (Exception e) {

        }

    }


    /**
     * 加载图片
     *
     */
    public void loadAlbum() {
        if (mPaths == null || mPaths.size() == 0) {
            return;
        }
        mViewPager.setAdapter(mViewPager.new ViewPagerAdapter(mPaths));
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(mCurrent);
        mCountView.setText((mCurrent + 1) + "/" + mPaths.size());
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (mViewPager.getAdapter() != null) {
                String text = (position + 1) + "/" + mViewPager.getAdapter().getCount();
                mCountView.setText(text);
            } else {
                mCountView.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

	/*@Override
    public void onSingleTap() {
		if(mHeaderBar.getVisibility()==View.VISIBLE){
			AlphaAnimation animation=new AlphaAnimation(1, 0);
			animation.setDuration(300);
			mHeaderBar.startAnimation(animation);
			mBottomBar.startAnimation(animation);
			mHeaderBar.setVisibility(View.GONE);
			mBottomBar.setVisibility(View.GONE);
		}else {
			AlphaAnimation animation=new AlphaAnimation(0, 1);
			animation.setDuration(300);
			mHeaderBar.startAnimation(animation);
			mBottomBar.startAnimation(animation);
			mHeaderBar.setVisibility(View.VISIBLE);
			mBottomBar.setVisibility(View.VISIBLE);
		}
	}*/

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        if (i == R.id.header_bar_photo_back) {
            onBackPressed();

		/*case R.id.delete:
            if(deletePaths.size()<=0)
				Toast.makeText(this, R.string.nothing_pic, Toast.LENGTH_SHORT).show();
			else{
				deletePaths.remove(mViewPager.getCurrentItem());
				String result = mViewPager.deleteCurrentPath();
				if (result != null)
					mCountView.setText(result);
				Toast.makeText(this,  R.string.delete_success, Toast.LENGTH_SHORT).show();
			}
			break;*/
        } else if (i == R.id.tv_delete) {
            Intent intent = new Intent();
            intent.putExtra(INTENT_POSITION, position);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        /*Intent intent = new Intent();
        intent.putStringArrayListExtra("deletePaths", (ArrayList<String>) deletePaths);
		setResult(10, intent);
		this.finish();*/
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.photo_activity_out);

    }
}
