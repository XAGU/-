package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 修改密码提示dialog
 *
 * @author zcd
 * @date 17/9/25
 */

public class ChangeHostDialog extends Dialog {

    private static final String QA1 = "http://116.62.236.67:";
    private static final String QA2 = "http://39.108.97.229";
    private static final String QA3= "http://39.108.74.164";
    private static final String QA4 = "http://47.107.47.24";
    private static final String QA5 = "http://47.107.45.109";
    private static final String BATH2 ="http://120.77.214.212";
    private static final String BATH1 = "http://39.108.85.163";

    private static final String HOST1 = "5081/";
    private static final String HOST2 = "5082/";
    private static final String HOST3 = "5097/";

    private TextView tvOk;
    private TextView tvCancel;
    private EditText etHost;
    private EditText etHost2;
    private EditText etHost3;
    private Spinner sp1;
    private Spinner sp2;
    private Spinner sp3;
    private OnOkClickListener listener;

    private List<HostAddress> userHosts = new ArrayList<HostAddress>() {
        {
            add(new HostAddress("qa1", QA1+ HOST1));
            add(new HostAddress("qa2", QA2 + HOST1));
            add(new HostAddress("qa3", QA3 + HOST1));
            add(new HostAddress("qa4", QA4+ HOST1));
            add(new HostAddress("qa5" , QA5 + HOST1));
            add(new HostAddress("bath1" ,BATH1 + HOST1));
            add(new HostAddress("bath2" , BATH2 + HOST1));
            add(new HostAddress("预发", "http://47.106.62.186/c/"));
        }
    };

    private List<HostAddress> bathroomHosts = new ArrayList<HostAddress>() {
        {
            add(new HostAddress("qa1", QA1+ HOST2));
            add(new HostAddress("qa2", QA2 + HOST2));
            add(new HostAddress("qa3", QA3 + HOST2));
            add(new HostAddress("qa4", QA4+ HOST2));
            add(new HostAddress("qa5" , QA5 + HOST2));
            add(new HostAddress("bath1" ,BATH1 + HOST2));
            add(new HostAddress("bath2" , BATH2 + HOST2));
            add(new HostAddress("预发", "http://47.106.62.186/b/"));
        }
    };


    private List<HostAddress> h5Hosts = new ArrayList<HostAddress>() {
        {
            add(new HostAddress("qa1", QA1+ HOST3));
            add(new HostAddress("qa2", QA2 + HOST3));
            add(new HostAddress("qa3", QA3 + HOST3));
            add(new HostAddress("qa4", QA4+ HOST3));
            add(new HostAddress("qa5" , QA5 + HOST3));
            add(new HostAddress("bath1" ,BATH1 + HOST3));
            add(new HostAddress("bath2" , BATH2 + HOST3));
            add(new HostAddress("预发", "http://47.106.62.186:5097/"));
        }
    };

    public ChangeHostDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.dialog_changehost);
        tvOk = findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(etHost.getText())) {
//                Toast.makeText(context, "请输入host", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (listener != null) {
                String etText1 = etHost.getText().toString();
                String etText2 = etHost2.getText().toString();
                String etText3 = etHost3.getText().toString();
                String spText1 = userHosts.get(sp1.getSelectedItemPosition()).getAddress();
                String spText2 = h5Hosts.get(sp2.getSelectedItemPosition()).getAddress();
                String spText3 = bathroomHosts.get(sp3.getSelectedItemPosition()).getAddress();
                listener.onOkClick(this, TextUtils.isEmpty(etText1) ? spText1 : etText1,
                        TextUtils.isEmpty(etText2) ? spText2 : etText2,
                        TextUtils.isEmpty(etText3) ? spText3 : etText3);
            }
            dismiss();
        });
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
        etHost = findViewById(R.id.et_host);
        etHost.setTypeface(null, Typeface.NORMAL);
//        etHost.setText(BuildConfig.SERVER);
        etHost2 = findViewById(R.id.et_host2);
        etHost2.setTypeface(null, Typeface.NORMAL);
        etHost3 = findViewById(R.id.et_host3);
        etHost3.setTypeface(null, Typeface.NORMAL);
//        etHost2.setText(BuildConfig.H5_SERVER);
        ViewUtil.setEditHintAndSize("请输入server", 14, etHost);
        ViewUtil.setEditHintAndSize("请输入h5 server", 14, etHost2);
        ViewUtil.setEditHintAndSize("请输入bathroom server", 14, etHost3);
        sp1 = findViewById(R.id.sp1);
        SpinnerAdapter sa = new SpinnerAdapter(userHosts, getContext());
        sp1.setAdapter(sa);
        sp2 = findViewById(R.id.sp2);
        SpinnerAdapter sa2 = new SpinnerAdapter(h5Hosts, getContext());
        sp2.setAdapter(sa2);
        sp3 = findViewById(R.id.sp3);
        SpinnerAdapter sa3 = new SpinnerAdapter(bathroomHosts, getContext());
        sp3.setAdapter(sa3);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog, String host, String host2, String bathroomHost);
    }

    @Data
    public static final class HostAddress {
        private String name;
        private String address;

        public HostAddress(String name, String address) {
            this.name = name;
            this.address = address;
        }
    }

    public static final class SpinnerAdapter extends BaseAdapter {
        private List<HostAddress> hosts;
        private Context context;

        public SpinnerAdapter(List<HostAddress> hosts, Context context) {
            this.hosts = hosts;
            this.context = context;
        }

        @Override
        public int getCount() {
            return hosts == null ? 0 : hosts.size();
        }

        @Override
        public Object getItem(int position) {
            return hosts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_spinner, null);
            if (convertView != null) {
                TextView textView = convertView.findViewById(R.id.tv_content);
                textView.setText(hosts.get(position).getName() + " " + hosts.get(position).getAddress());
            }
            return convertView;
        }
    }
}
