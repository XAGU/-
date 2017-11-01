package com.xiaolian.amigo.ui.main.update;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.AppUtils;

/**
 * Created by adamzfc on 2017/3/29.
 */

public class UpdateDialog extends DialogFragment {
    private UpdateActivity mActivity;
    protected IVersionModel mModel;

    public static UpdateDialog newInstance(IVersionModel model) {

        Bundle args = new Bundle();
        args.putSerializable(IntentKey.MODEL, model);
        UpdateDialog fragment = new UpdateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = (IVersionModel) getArguments().getSerializable(IntentKey.MODEL);
//        closeIfNoNewVersionUpdate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContent(view, R.id.tv_update_content);
        setTitle(view, R.id.tv_update_title);
        initView(view);
    }

    private void closeIfNoNewVersionUpdate() {
        if (mModel.getVersionCode() <= AppUtils.getAppVersionCode(getActivity().getApplicationContext())) {
            isLatest();
            getActivity().finish();
        }
    }

    private String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(mModel.getVersionDescription() == null ? "" : mModel.getVersionDescription());
        return sb.toString();
    }

    private String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("发现新版本V")
                .append(mModel.getVersionName());
        return sb.toString();
    }

    private void isLatest() {
        Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.btnCancel) {
//            Logger.d("cancel");
//            onCancel();
//        } else if (id == R.id.btnUpdate) {
//            Logger.d("update");
//            onUpdate();
//        }
//    }

    protected void onCancel() {
//        SharedPreferencesUtil.putInteger(getActivity(), "deny_version", mModel.getVersionCode());
        getActivity().finish();
    }

    protected void onUpdate() {

        mActivity.showDownLoadProgress();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UpdateActivity) {
            mActivity = (UpdateActivity) activity;
        }
    }

    protected int getLayout() {
        return R.layout.fragment_update;
    }

    protected void setContent(View view, int contentId) {
        TextView tvContext = (TextView) view.findViewById(contentId);
        tvContext.setText(getContent());
    }

    private void setTitle(View view, int titleId) {
        TextView tvTitle = (TextView) view.findViewById(titleId);
        tvTitle.setText(getTitle());
    }

    protected void initIfMustUpdate(View view, int id) {
        if (mModel.isMustUpdate()) {
            view.findViewById(id).setVisibility(View.GONE);
        }
    }

    protected void initView(View view) {
        bindUpdateListener(view,R.id.tv_ok);
        bindCancelListener(view, R.id.tv_cancel);
        initIfMustUpdate(view,R.id.tv_cancel);
        initIfMustUpdate(view,R.id.v_divide);
    }

    protected void bindUpdateListener(View view, int updateId) {
        view.findViewById(updateId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
            }
        });
    }

    protected void bindCancelListener(View view, int cancelId) {
        view.findViewById(cancelId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }
}
