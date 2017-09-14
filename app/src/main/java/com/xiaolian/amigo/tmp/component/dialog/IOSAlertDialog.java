package com.xiaolian.amigo.tmp.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;

/**
 * 仿IOS Alertdialog
 */

public class IOSAlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private View txt_msg_note_view;
    private ScrollView txt_msg_note_scroll;
    private TextView txt_msg_note;
    private TextView btn_neg;
    private TextView btn_pos;
    private ImageView img_line;
    private Display display;
    private EditText edt_text;
    private boolean showEdit=false;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showMsgNote = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public IOSAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IOSAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        txt_msg_note_view = view.findViewById(R.id.txt_msg_note_view);
        txt_msg_note_scroll = (ScrollView) view.findViewById(R.id.txt_msg_note_scroll);
        txt_msg_note_scroll.setVisibility(View.GONE);
        txt_msg_note = (TextView) view.findViewById(R.id.txt_msg_note);
        btn_neg = (TextView) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (TextView) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        edt_text=(EditText)view.findViewById(R.id.edt_text);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }

    public IOSAlertDialog setTitle(String title) {
        showTitle = true;
        if (title==null || "".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IOSAlertDialog setEditHint(String hint){
        showEdit = true;
        if (TextUtils.isEmpty(hint)) {
            edt_text.setHint("");
        } else {
            edt_text.setHint(hint);
        }
        return this;
    }

    public IOSAlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }
    public IOSAlertDialog setMsg(Spanned msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }


    public IOSAlertDialog setMsgNote(String msg) {
        showMsgNote = true;
        if ("".equals(msg)) {
            txt_msg_note.setText("内容");
        } else {
            txt_msg_note.setText(msg);
        }
        return this;
    }
    /**
     * 触碰对话框外区域时,是否关闭对话框,默认:是
     * @param cancel 是否可取消
     * @return IOSAlertDialog
     */
    public IOSAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(cancel);
        }
        return this;
    }
    /**
     * 按返回键,是否关闭对话框,默认:是
     * @param cancel 是否可取消
     * @return IOSAlertDialog
     */
    public IOSAlertDialog setCancelable(boolean cancel) {
        if (dialog != null)
            dialog.setCancelable(cancel);
        return this;
    }

    public IOSAlertDialog setPositiveClickListener(String text,
                                                   final OnDialogClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) {
                    listener.onDialogClickListener(IOSAlertDialog.this);
                }else{
                    dismiss();
                }
            }
        });
        return this;
    }

    public IOSAlertDialog setNegativeClickListener(String text,
                                                   final OnDialogClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }

        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) {
                    listener.onDialogClickListener(IOSAlertDialog.this);
                }else{
                    dismiss();
                }

            }
        });
        return this;
    }

    public IOSAlertDialog setPositiveButton(String text,
                                            final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public IOSAlertDialog setNegativeButton(String text,
                                            final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }
        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }
        if(showEdit){
            edt_text.setVisibility(View.VISIBLE);
        }else if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if (showMsgNote) {
            LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(context)/3);
            txt_msg_note_scroll.setLayoutParams(layoutParams);
			/*LayoutParams layoutParams=(LayoutParams) txt_msg_note_scroll.getLayoutParams();
			if (layoutParams.height>GloableParams.WINDOW_HEIGHT/2) {
				layoutParams.height=GloableParams.WINDOW_HEIGHT/2;
				txt_msg_note_scroll.requestLayout();
			}*/
            txt_msg_note_view.setVisibility(View.VISIBLE);
            txt_msg_note_scroll.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            //btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            //btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            //btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            //btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            //btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    /**
     * 对话框消失时事件,包括触碰对话框外区域,按返回键
     * @param listener listener
     * @return IOSAlertDialog
     */
    public IOSAlertDialog setOnDismiss(OnDismissListener listener){
        if (dialog != null)
            dialog.setOnDismissListener(listener);
        return this;
    }
    public void show() {
        setLayout();
        if(dialog!=null && !dialog.isShowing())
            dialog.show();
    }

    public void dismiss() {
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
    }


    public TextView getMsgTV(){
        return txt_msg;
    }

    public String getEditText(){
        return edt_text.getText().toString();

    }

    public EditText getEdtText(){
        return edt_text;
    }
    /**
     * dialog点击事件
     */
    public interface OnDialogClickListener{
        /**
         *  dialog点击事件
         * @param dialog  当前对话框
         */
        void onDialogClickListener(IOSAlertDialog dialog);
    }
}