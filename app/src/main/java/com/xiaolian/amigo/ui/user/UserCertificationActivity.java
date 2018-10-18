package com.xiaolian.amigo.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nanchen.compresshelper.CompressHelper;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.network.model.user.UserGradeInfoRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.ActionSheetDialog;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.FileUtils;
import com.xiaolian.amigo.util.GildeUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.PictureUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.xiaolian.amigo.ui.user.EditUserInfoActivity.KEY_BACK_DATA;
import static com.xiaolian.amigo.ui.user.EditUserInfoActivity.KEY_DATA;
import static com.xiaolian.amigo.ui.user.EditUserInfoActivity.KEY_TYPE;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_BACK_IMAGE;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_CLASS;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_DEPARTMENT;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_FRONT_IMAGE;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_GRADE;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_PROFESSION;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_STUDENT_ID;
import static com.xiaolian.amigo.ui.user.UserCertificationStatusActivity.KEY_STUENDT_IAMGES;
import static com.xiaolian.amigo.util.Constant.CLASS;
import static com.xiaolian.amigo.util.Constant.DEPARTMENT;
import static com.xiaolian.amigo.util.Constant.PROFESSION;
import static com.xiaolian.amigo.util.Constant.STUDENT_ID;
import static com.xiaolian.amigo.util.Constant.USER_INFO_ACTIVITY_SRC;

public class UserCertificationActivity extends BaseActivity implements IUserCertificationView {

    private static final int REQUEST_IMAGE = 0x3302;

    private static final int REQUEST_BACK_CARD_IMAGE = 0x2201;

    private static final int REQUEST_FRONT_CARD_IMAGE = 0x2202;

    private static final int REQUEST_EDIT_USERINFO = 0x2200;
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
    @BindView(R.id.certify)
    Button certify;
    @BindView(R.id.sv_main_container)
    ScrollView svMainContainer;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rlToolbar;

    private User user;


    private UserActivityComponent mActivityComponent;


    private ImageAddAdapter imageAddAdapter;
    List<ImageAddAdapter.ImageItem> addImages = new ArrayList<>();

    List<String> images = new ArrayList<>();


    private int screenWidth;
    private int imageWidth;


    private String ivFrontPath;  // 正面照图片地址

    private String ivBackPath;  // 背面照图片地址

    private String ivFrontUrl;  // 正面照片Url

    private String ivBackUrl; //  背面照片Url


    private File ivBackFile ;

    private File ivFrontFile ;

    private List<File> imageFile = new ArrayList<>();
    private boolean isNeedRefresh = false;

    private ActionSheetDialog actionSheetDialog; // 年级选择器底部弹窗

    private List<Integer> gradeList = new ArrayList<>();

    private Integer grade = 0;


    private int rlToolBarHeight ;


    private String department ;

    private String profession ;

    private String gradestr ;

    private String classstr ;

    private String studentIdstr ;

    private List<String> studentImageBase64 ;

    private String frontImageBase64 ;

    private String backImageBase64 ;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_certification);
        ButterKnife.bind(this);
        setUp();
        IOverScrollDecor iOverScrollDecor = OverScrollDecoratorHelper.setUpOverScroll(svMainContainer);
        iOverScrollDecor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {

            }
        });
        svMainContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > rlToolBarHeight){
                    setTitleVisiable(View.VISIBLE);
                }else{
                    setTitleVisiable(View.GONE);
                }
            }
        });
        initView();
    }



    private void setTitleVisiable(int visiable){
        tvTitle.setVisibility(visiable);
        viewLine.setVisibility(visiable);
    }

    protected void initView() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        initImageAdd();
        initChooseGrade();
        user = presenter.getUserInfo();
        if (user != null) referStatus(user);
        presenter.getGradeInfo();
        rlToolbar.post(() -> {
            rlToolBarHeight = rlToolbar.getHeight();
        });

    }

    @OnClick({R.id.rel_edit_grade, R.id.rel_edit_department, R.id.rel_edit_profession
            , R.id.rel_edit_class, R.id.rel_edit_studentId})
    public void editUserInfo(View view) {
        switch (view.getId()) {
            case R.id.rel_edit_department:
                editUserInfo(DEPARTMENT);
                break;
            case R.id.rel_edit_profession:
                editUserInfo(PROFESSION);
                break;
            case R.id.rel_edit_class:
                editUserInfo(CLASS);
                break;
            case R.id.rel_edit_studentId:
                editUserInfo(STUDENT_ID);
                break;
            default:
                break;
        }

    }


    public void editUserInfo(int type) {
        String data = "";
        switch (type) {
            case DEPARTMENT:
                data = tvDepartment.getText().toString().trim();
                break;
            case PROFESSION:
                data = tvProfession.getText().toString().trim();
                break;
            case CLASS:
                data = tvClass.getText().toString().trim();
                break;
            case STUDENT_ID:
                data = tvStudentId.getText().toString().trim();
                break;
        }
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_DATA, data);
        startActivityForResult(intent, REQUEST_EDIT_USERINFO);
    }


    private void initChooseGrade() {
        if (actionSheetDialog == null) {
            actionSheetDialog = new ActionSheetDialog(this)
                    .builder()
                    .setTitle("请选择年级");

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (actionSheetDialog != null && actionSheetDialog.getDialog() != null)
            actionSheetDialog.getDialog().dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedRefresh) {
            tvDormitory.setText(presenter.getDormitory());
            tvDepartment.setText(department);
            tvProfession.setText(profession);
            tvGrade.setText(gradestr);
            tvClass.setText(classstr);
            tvStudentId.setText(studentIdstr);

            isNeedRefresh = false ;
        }
        toggleBtnStatus();
    }

    private void referStatus(User user) {

        tvDepartment.setText(user.getDepartment());
        tvProfession.setText(user.getProfession());
        tvGrade.setText(user.getGrade());
        tvClass.setText(user.getCalsses());
        tvStudentId.setText(user.getStudentId());
        tvDormitory.setText(user.getDormitory());
    }

    @OnClick(R.id.rel_edit_grade)
    public void choseGraed() {
        if (actionSheetDialog != null && actionSheetDialog.getDialog() != null) {
            if (gradeList != null && gradeList.size() == 0) presenter.getGradeInfo();
            actionSheetDialog.show();
        }
    }


    @OnClick(R.id.rel_edit_dormitory)
    public void setRelEditDormitory() {
        isNeedRefresh = true;
        Intent intent;
        intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, USER_INFO_ACTIVITY_SRC);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        if (actionSheetDialog != null && actionSheetDialog.getDialog() != null) {
            actionSheetDialog.getDialog().dismiss();
        }
        actionSheetDialog = null;
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
                                presenter.uploadImage(UserCertificationActivity.this, imagePath, position, OssFileType.CERTIFICAITON);

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
    public void addImage(String url, int position, String localPath) {
        Log.d(TAG, url);
        if (this.images.size() > position) {
            this.images.remove(position);
            this.images.add(position, url);
        } else {
            this.images.add(url);
        }

        if (this.urls.size() > position) {
            this.urls.remove(position);
            this.urls.add(position, localPath);
        } else {
            this.urls.add(localPath);
        }
        refreshAddImage();
        toggleBtnStatus();
    }

    @Override
    public void setCardImage(ImageView iv, String filePath, String objectKey) {
        if (iv == null) return;
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GildeUtils.setPathImage(this, iv, filePath);
        if (iv.getId() == R.id.iv_back_card) {
            ivBackPath = filePath;
            ivBackUrl = objectKey;

        }
        if (iv.getId() == R.id.iv_front_card) {
            ivFrontPath = filePath;
            ivFrontUrl = objectKey;
        }
        toggleBtnStatus();
    }

    @OnTextChanged({R.id.tv_department, R.id.tv_profession, R.id.tv_grade, R.id.tv_class,
            R.id.tv_studentId, R.id.tv_dormitory})
    void onTextChange() {
        toggleBtnStatus();
    }

    boolean allValidated = false;

    public void toggleBtnStatus() {
        allValidated = (!TextUtils.isEmpty(tvDepartment.getText()) && !TextUtils.isEmpty(tvProfession.getText()) &&
                !TextUtils.isEmpty(tvGrade.getText()) && !TextUtils.isEmpty(tvClass.getText()) && !TextUtils.isEmpty(tvStudentId.getText())
                && !TextUtils.isEmpty(tvDormitory.getText())
                && !TextUtils.isEmpty(ivBackPath) && !TextUtils.isEmpty(ivFrontPath) && images.size() > 0);
        certify.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    File ivCompressFileBackFile  ,ivCompressFileFrontFile ;

    List<File> compressImageFile ;

    @OnClick(R.id.certify)
    public void certify() {
        Integer stuNum = 0;
        try {
            grade = Integer.parseInt(tvGrade.getText().toString());
            stuNum = Integer.parseInt(tvStudentId.getText().toString());
        } catch (Exception e) {
            Log.wtf(TAG, e.getMessage());
        }
        ivBackFile = getFile(ivBackPath);
        ivFrontFile = getFile(ivFrontPath);

        ivCompressFileBackFile = CompressHelper.getDefault(this).compressToFile(ivBackFile);

        ivCompressFileFrontFile = CompressHelper.getDefault(this).compressToFile(ivFrontFile);

        if (imageFile == null ) imageFile = new ArrayList<>();

        if (compressImageFile == null) compressImageFile = new ArrayList<>();

        if (imageFile.size() > 0) imageFile.clear();

        if (compressImageFile.size() > 0) compressImageFile.clear();
        for (String imagePath : images){
            imageFile.add(getFile(imagePath));
            compressImageFile.add(CompressHelper.getDefault(this).compressToFile(getFile(imagePath)));
        }


        presenter.certify(tvClass.getText().toString(), tvDepartment.getText().toString(),
                grade, ivCompressFileBackFile, ivCompressFileFrontFile, tvProfession.getText().toString(), stuNum, compressImageFile);
    }

    private File getFile(String filePath){
        File file  = new File(filePath);
        if (file.exists())
            return file ;
            else return null ;

    }

    @Override
    public void setGradeInfo(UserGradeInfoRespDTO dto) {
        if (gradeList.size() > 0) gradeList.clear();
        gradeList.addAll(dto.getGradeList());
        setGradeList(gradeList);
    }

    @Override
    public void certifySuccess() {
        finish();
    }

    private void setGradeList(List<Integer> gradeList) {
        if (actionSheetDialog == null) return;
        if (gradeList != null && gradeList.size() > 0) {
            for (Integer grad : gradeList) {
                actionSheetDialog.addSheetItem(grad + "", ActionSheetDialog.SheetItemColor.Orange, i -> {
                    tvGrade.setText(grad + "");
                });
            }

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




    @OnClick({R.id.iv_first, R.id.iv_second, R.id.iv_third, R.id.front_card_rl, R.id.back_card_rl})
    void chooseImage(View view) {
        Log.d(TAG, "click");
        switch (view.getId()) {
            case R.id.iv_first: {
                getImage2(imagePath -> {
                    ivSecond.setVisibility(View.VISIBLE);
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 0, OssFileType.CERTIFICAITON);
                });
                break;
            }
            case R.id.iv_second: {

                getImage2(imagePath -> {

                    ivThird.setVisibility(View.VISIBLE);
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 1, OssFileType.CERTIFICAITON);
                });

                break;
            }
            case R.id.iv_third: {

                getImage2(imagePath -> {
                    presenter.uploadImage(UserCertificationActivity.this,
                            imagePath, 2, OssFileType.CERTIFICAITON);
                });
                break;
            }
            case R.id.front_card_rl:
                if (ivFrontCard.getDrawable() != null) {
                    Intent intent = new Intent(UserCertificationActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, 1);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, ivFrontPath);
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_FRONT_CARD_IMAGE);
                } else {
                    getImage2(imagePath -> {
                        presenter.uploadImage(this, imagePath, OssFileType.CERTIFICAITON, ivFrontCard);

                    });
                }
                break;

            case R.id.back_card_rl:
                if (ivBackCard.getDrawable() != null) {
                    Intent intent = new Intent(UserCertificationActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, 1);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, ivBackPath);
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_BACK_CARD_IMAGE);
                } else {
                    getImage2(imagePath -> {
                        presenter.uploadImage(this, imagePath, OssFileType.CERTIFICAITON, ivBackCard);
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
            ivBackPath = "";
            ivBackUrl = "";
        }

        if (requestCode == REQUEST_EDIT_USERINFO && resultCode == RESULT_OK) {
            int type = data.getIntExtra(KEY_TYPE, -1);
            String content = data.getStringExtra(KEY_BACK_DATA);
            onActivityResultForSetUserInfo(type, content);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    private void onActivityResultForSetUserInfo(int type, String data) {
        switch (type) {
            case DEPARTMENT:
                tvDepartment.setText(data);
                break;
            case PROFESSION:
                tvProfession.setText(data);
                break;
            case CLASS:
                tvClass.setText(data);
                break;
            case STUDENT_ID:
                tvStudentId.setText(data);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setUp() {
        if (getIntent() != null){
            department = getIntent().getStringExtra(KEY_DEPARTMENT);
            profession = getIntent().getStringExtra(KEY_PROFESSION);
            gradestr = getIntent().getStringExtra(KEY_GRADE);
            classstr = getIntent().getStringExtra(KEY_CLASS);
            studentIdstr = getIntent().getStringExtra(KEY_STUDENT_ID);
            studentImageBase64 = getIntent().getStringArrayListExtra(KEY_STUENDT_IAMGES);
            frontImageBase64 = getIntent().getStringExtra(KEY_FRONT_IMAGE);
            backImageBase64 = getIntent().getStringExtra(KEY_BACK_IMAGE);
            isNeedRefresh = true ;
        }
    }
}
