package com.xiaolian.amigo.ui.user;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.user.UserCertifyInfoRespDTO;
import com.xiaolian.amigo.data.vo.CertificationStatusTypeEvent;
import com.xiaolian.amigo.data.vo.UserCertificationStatus;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.user.intf.IUserCerticifationStatusPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationStatusView;
import com.xiaolian.amigo.ui.widget.ErrorLayout;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.FileIOUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.xiaolian.amigo.util.Constant.CERTIFICATION_FAILURE;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_PASS;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_REVIEWING;
import static com.xiaolian.amigo.util.Constant.USER_CERTIFICATION_STATUS_ACTIVITY_SRC;

public class UserCertificationStatusActivity extends BaseActivity implements IUserCertificationStatusView {

    private static final String CERTIFICATION_FILURE = "（认证未通过）";

    private static final String CERTIFICATIONINT = "(审核中，请稍等...)";

    public static final String KEY_CERTIFICATION_TYPE = "KEY_CERTIFICATION_TYPE";


    public static final String KEY_DEPARTMENT = "KEY_DEPARTMENT";

    public static final String KEY_PROFESSION = "KEY_PROFESSION";

    public static final String KEY_GRADE = "KEY_GRADE";

    public static final String KEY_CLASS = "KEY_CLASS";

    public static final String KEY_STUDTENT_ID = "KEY_STUDTENT_ID";


    public static final String KEY_STUDENT_IMAGE = "KEY_STUDENT_IMAGE";

    public static final String KEY_FRONT_IMAGE = "KEY_FRONT_IMAGE";

    public static final String KEY_BACK_IMAGE = "KEY_BACK_IMAGE";


    @BindView(R.id.certification)
    Button certification;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.reason_ll)
    LinearLayout reasonLl;
    @BindView(R.id.reason_line)
    View reasonLine;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.change_dormitory)
    TextView changeDormitory;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.loading_rl)
    RelativeLayout loadingRl;
    @BindView(R.id.error_net_layout)
    ErrorLayout errorNetLayout;
    @BindView(R.id.tv_student_image)
    TextView tvStudentImage;
    @BindView(R.id.tv_card_image)
    TextView tvCardImage;


    private UserActivityComponent mActivityComponent;

    @Inject
    IUserCerticifationStatusPresenter<IUserCertificationStatusView> presenter;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_text)
    TextView tvToolbarText;
    @BindView(R.id.tv_toolbar_iv)
    ImageView tvToolbarIv;
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
    @BindView(R.id.student_card_id)
    RecyclerView studentCardId;
    @BindView(R.id.card_id)
    RecyclerView cardId;
    @BindView(R.id.main_content)
    LinearLayout mainContent;
    @BindView(R.id.sv_main_container)
    ScrollView svMainContainer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_line)
    View viewLine;

    private Unbinder unbinder;

    private int screenWidth;
    private int imageWidth;

    private ImageAddAdapter studentIdAdapter;
    List<ImageAddAdapter.ImageItem> studentIdImages = new ArrayList<>();


    private ImageAddAdapter cardIdAdapter;  // 身份证
    List<ImageAddAdapter.ImageItem> cardIdImages = new ArrayList<>();


    List<String> cardIdUrlImages = new ArrayList<>();

    List<String> studentUrlImages = new ArrayList<>();


    private int rlToolBarHeight;

    private ValueAnimator loadingAnimator;

    int[] loadingRes = new int[]{
            R.drawable.loading_one, R.drawable.loading_two,
            R.drawable.loading_three, R.drawable.loading_four
    };

    private UserCertifyInfoRespDTO dto;

    private String department;

    private String profession;

    private String grade;

    private String classstr;

    private String studentId;

    private String frontImage;

    private String backImage;

    private boolean isNeedRefer = false;

    private int type;

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(KEY_CERTIFICATION_TYPE, -1);
            department = getIntent().getStringExtra(KEY_DEPARTMENT);
            profession = getIntent().getStringExtra(KEY_PROFESSION);
            grade = getIntent().getStringExtra(KEY_GRADE);
            classstr = getIntent().getStringExtra(KEY_CLASS);
            studentId = getIntent().getStringExtra(KEY_STUDTENT_ID);
            if (cardIdUrlImages == null) cardIdUrlImages = new ArrayList<>();
            if (studentUrlImages == null) studentUrlImages = new ArrayList<>();
            if (cardIdUrlImages.size() > 0) cardIdUrlImages.clear();
            if (studentUrlImages.size() > 0) studentUrlImages.clear();
            studentUrlImages = getIntent().getStringArrayListExtra(KEY_STUDENT_IMAGE);
            backImage = getIntent().getStringExtra(KEY_BACK_IMAGE);
            frontImage = getIntent().getStringExtra(KEY_FRONT_IMAGE);
            cardIdUrlImages.add(frontImage);
            cardIdUrlImages.add(backImage);
            isNeedRefer = true;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_certification_status);
        unbinder = ButterKnife.bind(this);
        setTitleVisiable(View.GONE);
        svMainContainer.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > rlToolBarHeight) {
                setTitleVisiable(View.VISIBLE);
            } else {
                setTitleVisiable(View.GONE);
            }
        });
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        if (dto != null) writeImageToFile(dto);
                    } else {
                        showMessage("没有SD卡权限");
                    }
                });
        initJect();
        initView();
        initImageAdd();
        presenter.getDormitory();
        if (!isNeedRefer)
            presenter.getCertifyInfo();


        tvDormitory.setText(presenter.getDormInfo());

    }


    /**
     * 根据传过来的现不同页面
     */
    private void setStatus(int type) {
        if (type == CERTIFICATION_FAILURE) {
            showFail();
            showImage();
            tvToolbarText.setText(CERTIFICATION_FILURE);
        } else if (type == CERTIFICATION_REVIEWING) {
            showBarTxt();
            showImage();
            tvToolbarText.setText(CERTIFICATIONINT);
        } else if (type == CERTIFICATION_PASS) {
            hideImage();
            showBarIv();
        } else {

        }
    }

    /**
     * 显示照片
     */
    private void showImage() {
        tvCardImage.setVisibility(View.VISIBLE);
        tvStudentImage.setVisibility(View.VISIBLE);
        studentCardId.setVisibility(View.VISIBLE);
        cardId.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏照片
     */
    private void hideImage() {
        tvCardImage.setVisibility(View.GONE);
        tvStudentImage.setVisibility(View.GONE);
        studentCardId.setVisibility(View.GONE);
        cardId.setVisibility(View.GONE);

    }

    private void showFail() {
        tvToolbarText.setVisibility(View.VISIBLE);
        tvToolbarIv.setVisibility(View.GONE);
        certification.setVisibility(View.VISIBLE);
        changeDormitory.setVisibility(View.GONE);
        setMarginBottom(ScreenUtils.dpToPxInt(this, 91));
    }

    private void showBarTxt() {
        tvToolbarText.setVisibility(View.VISIBLE);
        tvToolbarIv.setVisibility(View.GONE);
        certification.setVisibility(View.GONE);
        changeDormitory.setVisibility(View.GONE);
        setMarginBottom(ScreenUtils.dpToPxInt(this, 20));
    }


    /**
     * 设置margin
     */
    private void setMarginBottom(int marginBottom) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) svMainContainer.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, marginBottom);
        svMainContainer.setLayoutParams(layoutParams);
    }

    private void showBarIv() {
        tvToolbarIv.setVisibility(View.VISIBLE);
        tvToolbarText.setVisibility(View.GONE);
        certification.setVisibility(View.GONE);
        setMarginBottom(ScreenUtils.dpToPxInt(this, 20));
        changeDormitory.setVisibility(View.VISIBLE);
    }

    private void initView() {
        presenter.onAttach(this);
        rlToolbar.post(() -> {
            rlToolBarHeight = rlToolbar.getHeight();
        });
        setErrorNetListener();
        initLoadingAnim();
    }


    @OnClick(R.id.certification)
    public void startCertification() {

        Intent intent = new Intent(this, UserCertificationActivity.class);
        UserCertificationStatus userCertificationStatus = new UserCertificationStatus(
                tvDepartment.getText().toString().trim(), tvProfession.getText().toString().trim(),
                tvGrade.getText().toString().trim(), tvClass.getText().toString().trim(),
                tvStudentId.getText().toString().trim(), studentUrlImages);
        if (cardIdUrlImages != null && cardIdUrlImages.size() > 1) {
            userCertificationStatus.setFrontImageBase64(cardIdUrlImages.get(0));
            userCertificationStatus.setBackImageBase64(cardIdUrlImages.get(1));
        }

        EventBus.getDefault().postSticky(userCertificationStatus);
        startActivity(intent);
        this.finish();
    }


    private void initJect() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
    }

    private void setTitleVisiable(int visiable) {
        tvTitle.setVisibility(visiable);
        viewLine.setVisibility(visiable);
    }


    private void rxjavaByteConverFile(String name, String imageBase64, Action1<File> action1) {
        Observable.just(imageBase64)
                .subscribeOn(Schedulers.io())
                .map(s -> Base64.decode(s, Base64.DEFAULT)).subscribeOn(Schedulers.io())
                .map(bytes -> createFileFromBytes(name, bytes)).observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public File createFileFromBytes(String name, byte[] bytes) {

        if (bytes == null || bytes.length == 0) return null;
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/";


        File path = new File(filePath);
        if (!path.exists() && !path.mkdirs()) {
//            onError(R.string.no_sd_card_premission);
            return null;
        }
        int random = (int) (Math.random() * 100 + 1);
        File outputImage = new File(filePath, name + random + System.currentTimeMillis() + ".jpg");

        try {

            if (outputImage.exists()) {
                outputImage.delete();
            }
            boolean b = outputImage.createNewFile();
            if (!b) return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        FileIOUtils.writeFileFromBytesByStream(outputImage, bytes);
        return outputImage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDetach();
    }


    private void initLoadingAnim() {
        loadingAnimator = ValueAnimator.ofInt(0, 3, 0);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setDuration(1000);
        loadingAnimator.setInterpolator(new LinearInterpolator());
        loadingAnimator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            Log.wtf(TAG, currentValue + "");
            if (ivLoading != null)
                ivLoading.setImageResource(loadingRes[currentValue]);
        });
    }

    @OnClick(R.id.certification)
    public void certification() {
        startActivity(this, UserCertificationActivity.class);
    }

    boolean needReferDormitory = false;

    @OnClick(R.id.change_dormitory)
    public void changeDormitory() {
        needReferDormitory = true;
        Intent intent;
        intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, USER_CERTIFICATION_STATUS_ACTIVITY_SRC);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
        startActivity(intent);
    }


    private void initImageAdd() {
        screenWidth = ScreenUtils.getScreenWidth(this);
//        imageWidth = (screenWidth - ScreenUtils.dpToPxInt(this, 62)) / 3;
        imageWidth = ScreenUtils.dpToPxInt(this, 100);
        cardIdAdapter = new ImageAddAdapter(this, R.layout.item_image_add, cardIdImages, false);
        cardIdAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(UserCertificationStatusActivity.this, AlbumItemActivity.class);
                intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, position);
                intent.putStringArrayListExtra(AlbumItemActivity.EXTTRA_TYPE_LOCAL_LIST, (ArrayList<String>) cardIdUrlImages);
                intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_NORMAL);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        cardIdAdapter.setViewWidth(imageWidth
        );
        cardId.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cardId.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        cardId.setAdapter(cardIdAdapter);


        studentIdAdapter = new ImageAddAdapter(this, R.layout.item_image_add, studentIdImages, false);
        studentIdAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(UserCertificationStatusActivity.this, AlbumItemActivity.class);
                intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, position);
                intent.putStringArrayListExtra(AlbumItemActivity.EXTTRA_TYPE_LOCAL_LIST, (ArrayList<String>) studentUrlImages);
                intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_NORMAL);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        studentIdAdapter.setViewWidth(imageWidth);
        studentCardId.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        studentCardId.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        studentCardId.setAdapter(studentIdAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedRefer || needReferDormitory) {
            presenter.getDormitory();
            tvDormitory.setText(presenter.getDormInfo());
        }

        if (isNeedRefer) {
            if (!TextUtils.isEmpty(department))
                tvDormitory.setText(department);

            if (!TextUtils.isEmpty(profession))
                tvProfession.setText(profession);

            if (!TextUtils.isEmpty(grade))
                tvGrade.setText(grade);

            if (!TextUtils.isEmpty(classstr))
                tvClass.setText(classstr);

            if (studentUrlImages != null && studentUrlImages.size() > 0 && studentIdAdapter != null) {
                studentIdAdapter.notifyDataSetChanged();
            }

            if (cardIdUrlImages != null && cardIdUrlImages.size() > 0 && cardIdAdapter != null) {
                cardIdAdapter.notifyDataSetChanged();
            }
            if (type != -1) {
                setStatus(type);
            }

        }
        isNeedRefer = false;
        needReferDormitory = false;

    }

    @Override
    public void setInfo(UserCertifyInfoRespDTO data) {

        svMainContainer.setVisibility(View.VISIBLE);
        setStatus(data.getStatus());
        if (data.getStatus() == CERTIFICATION_FAILURE) {
            tvReason.setText(data.getFailReason());
            reasonLl.setVisibility(View.VISIBLE);
            reasonLine.setVisibility(View.VISIBLE);
        } else {
            reasonLl.setVisibility(View.GONE);
            reasonLine.setVisibility(View.GONE);
        }

        tvDepartment.setText(data.getFaculty());
        tvProfession.setText(data.getMajor());
        tvGrade.setText(data.getGrade() + "");
        tvClass.setText(data.getClassName());
        tvStudentId.setText(data.getStuNum() + "");
        tvDormitory.setText(presenter.getDormInfo());
        this.dto = data;
        writeImageToFile(data);
    }

    private void writeImageToFile(UserCertifyInfoRespDTO data) {
        if (cardIdUrlImages.size() > 0) cardIdUrlImages.clear();

        if (studentUrlImages.size() > 0) studentUrlImages.clear();


        for (String url : data.getStuPicturesData()) {
            rxjavaByteConverFile("images", url, file -> {
                if (file != null && file.exists()) {
                    String imagePath = file.getAbsolutePath();
                    studentIdImages.add(new ImageAddAdapter.ImageItem(imagePath));
                    studentUrlImages.add(imagePath);
                    if (studentUrlImages.size() == data.getStuPicturesData().size()) {
                        if (studentIdAdapter != null) studentIdAdapter.notifyDataSetChanged();
                    }
                }
            });
        }


        if (cardIdImages != null) {
            cardIdImages.clear();

            rxjavaByteConverFile("ivFrontImage", data.getIdCardFrontData(), file -> {
                if (file != null && file.exists()) {
                    String imagePath = file.getAbsolutePath();
                    cardIdImages.add(new ImageAddAdapter.ImageItem(imagePath));
                    cardIdUrlImages.add(imagePath);
                    rxjavaByteConverFile("ivBackImage", data.getIdCardBehindData(), file2 -> {
                        if (file2 != null && file2.exists()) {
                            String imagePath2 = file2.getAbsolutePath();
                            cardIdImages.add(new ImageAddAdapter.ImageItem(imagePath2));
                            cardIdUrlImages.add(imagePath2);
                            cardIdAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });


        }

    }


    @Override
    public void showAnimaLoading() {
        loadingRl.setVisibility(View.VISIBLE);
        if (loadingAnimator == null) return;

        if (loadingAnimator.isRunning()) {
            loadingAnimator.cancel();
        }
        loadingAnimator.start();
    }

    private void setErrorNetListener() {
        if (errorNetLayout != null)
            errorNetLayout.setReferListener(() -> {
                if (presenter != null) {
                    presenter.getCertifyInfo();
                }
            });
    }

    @Override
    public void showErrorLayout() {
        errorNetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        svMainContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorLayout() {
        errorNetLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideAnimaLoading() {
        loadingRl.setVisibility(View.GONE);
        if (loadingAnimator == null) return;

        if (loadingAnimator.isRunning()) loadingAnimator.cancel();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().postSticky(new CertificationStatusTypeEvent(type));
        super.onBackPressed();
    }


}
