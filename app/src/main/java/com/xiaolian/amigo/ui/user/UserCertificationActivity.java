package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.GildeUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCertificationActivity extends BaseActivity implements IUserCertificationView {

    private static final int REQUEST_IMAGE = 0x3302;

    private static final int REQUEST_BACK_CARD_IMAGE= 0x2201 ;

    private static final int REQUEST_FRONT_CARD_IMAGE = 0x2202 ;
    private static final int IMAGE_COUNT = 3;
    @Inject
    IUserCertificationPresenter<IUserCertificationView> presenter;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_profession)
    TextView tvProfession;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_studentId)
    TextView tvStudentId;
    @BindView(R.id.tv_dormitory)
    TextView tvDormitory;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.rel_edit_department)
    RelativeLayout relEditDepartment;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.rel_edit_profession)
    RelativeLayout relEditProfession;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.rel_edit_grade)
    RelativeLayout relEditGrade;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.rel_edit_class)
    RelativeLayout relEditClass;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.rel_edit_studentId)
    RelativeLayout relEditStudentId;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.rel_edit_dormitory)
    RelativeLayout relEditDormitory;
    @BindView(R.id.submit_card_txt)
    TextView submitCardTxt;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_third)
    ImageView ivThird;
    @BindView(R.id.student_card_ll)
    LinearLayout studentCardLl;
    @BindView(R.id.card_txt)
    TextView cardTxt;
    @BindView(R.id.iv_front_card)
    ImageView ivFrontCard;
    @BindView(R.id.tv_front_card)
    TextView tvFrontCard;
    @BindView(R.id.front_card_rl)
    RelativeLayout frontCardRl;
    @BindView(R.id.iv_back_card)
    ImageView ivBackCard;
    @BindView(R.id.tv_back_card)
    TextView tvBackCard;
    @BindView(R.id.back_card_rl)
    RelativeLayout backCardRl;

    private User user;


    private UserActivityComponent mActivityComponent;


    private ImageAddAdapter imageAddAdapter;
    List<ImageAddAdapter.ImageItem> addImages = new ArrayList<>();

    List<String> images = new ArrayList<>();


    private int screenWidth;
    private int imageWidth;


    private String ivFrontPath ;  // 正面照图片地址

    private String ivBackPath ;  // 背面照图片地址

    private String ivFrontUrl ;  // 正面照片Url

    private String ivBackUrl ; //  背面照片Url


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_certification);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        initImageAdd();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) referStatus(user);
    }

    private void referStatus(User user) {

        tvDepartment.setText(user.getDepartment());
        tvProfession.setText(user.getProfession());
        tvGrade.setText(user.getGrade());
        tvClass.setText(user.getCalsses());
        tvStudentId.setText(user.getStudentId());
        tvDormitory.setText(user.getDormitory());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    private void initImageAdd() {
        screenWidth = ScreenUtils.getScreenWidth(this);
        imageWidth = (screenWidth - ScreenUtils.dpToPxInt(this, 62)) / 3;
        addImages.add(new ImageAddAdapter.ImageItem());
        imageAddAdapter = new ImageAddAdapter(this, R.layout.item_image_add, addImages);
        imageAddAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (images.isEmpty() || (images.size() < IMAGE_COUNT && position == images.size())) {
                    getImage2(imagePath -> {
                                presenter.uploadImage(UserCertificationActivity.this, imagePath, position, OssFileType.FOUND);

                            }
                    );
                } else {
                    Intent intent = new Intent(UserCertificationActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, position);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, images.get(position));
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        imageAddAdapter.setViewWidth(imageWidth);
        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImage.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rvImage.setAdapter(imageAddAdapter);
    }


    List<String> urls = new ArrayList<>();
    @Override
    public void addImage(String url, int position , String localPath) {
        Log.d(TAG ,url);
        if (this.images.size() > position) {
            this.images.remove(position);
            this.images.add(position, url);
        } else {
            this.images.add(url);
        }

        if (this.urls.size() > position){
            this.urls.remove(position);
            this.urls.add(position, localPath);
        } else {
            this.urls.add(localPath);
        }
        refreshAddImage();
    }

    @Override
    public void setCardImage(ImageView iv, String filePath, String objectKey) {
        if (iv == null) return ;
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GildeUtils.setPathImage(this ,iv ,filePath);
        if (iv.getId() == R.id.iv_back_card){
            ivBackPath = filePath;
            ivBackUrl = objectKey ;

        }
        if (iv.getId() == R.id.iv_front_card){
            ivFrontPath = filePath ;
            ivFrontUrl = objectKey ;
        }
    }


    private void referImageView(){
        if (ivBackCard.getDrawable() != null){

        }
    }

    private void refreshAddImage() {
        addImages.clear();
        for (String image : images) {
            addImages.add(new ImageAddAdapter.ImageItem(image));
        }
        if (images.size() < IMAGE_COUNT) {
            addImages.add(new ImageAddAdapter.ImageItem());
        }
        imageAddAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.iv_first, R.id.iv_second, R.id.iv_third , R.id.front_card_rl ,R.id.back_card_rl})
    void chooseImage(View view) {
        Log.d(TAG ,"click");
        switch (view.getId()) {
            case R.id.iv_first: {
                getImage2(imagePath -> {
                    ivSecond.setVisibility(View.VISIBLE);
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 0, OssFileType.FOUND);
                });
                break;
            }
            case R.id.iv_second: {

                getImage2(imagePath -> {

                    ivThird.setVisibility(View.VISIBLE);
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 1, OssFileType.FOUND);
                });

                break;
            }
            case R.id.iv_third: {

                getImage2(imagePath -> {
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 2, OssFileType.FOUND);
                });
                break;
            }
            case R.id.front_card_rl:
                if (ivFrontCard.getDrawable() != null){
                    Intent intent = new Intent(UserCertificationActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, 1);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, ivFrontPath);
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_FRONT_CARD_IMAGE);
                }else{
                getImage2(imagePath -> {
                    presenter.uploadImage(this ,imagePath  , OssFileType.FOUND , ivFrontCard);

                });}
                break;

            case R.id.back_card_rl:
                if (ivBackCard.getDrawable() !=null){
                    Intent intent = new Intent(UserCertificationActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, 1);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, ivBackPath);
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_BACK_CARD_IMAGE);
                }else {
                    getImage2(imagePath -> {
                        presenter.uploadImage(this, imagePath, OssFileType.FOUND, ivBackCard);
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            int position = data.getIntExtra(AlbumItemActivity.INTENT_POSITION, -1);
            if (position != -1) {
                images.remove(position);
                urls.remove(position);
                refreshAddImage();
            }
        }

        if (requestCode == REQUEST_FRONT_CARD_IMAGE && resultCode == RESULT_OK) {
            ivFrontCard.setImageDrawable(null);
            ivFrontPath = "";
            ivFrontUrl = "";
        }

        if (requestCode == REQUEST_BACK_CARD_IMAGE && resultCode == RESULT_OK) {
            ivBackCard.setImageDrawable(null);
            ivBackPath = "" ;
            ivBackUrl = "" ;
        }
    }
    @Override
    protected void setUp() {

    }
}
