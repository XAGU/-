package com.xiaolian.amigo.ui.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.RxHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 单选列表Dialog
 */
public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;
    private RecyclerView mRecyclerView;
    private int itemGravity = Gravity.CENTER;
    private int itemIndex = -1;
    private int footLayout;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        // 获取Dialog布局
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayout_content = view.findViewById(R.id.sLayout_content);
        lLayout_content = view
                .findViewById(R.id.lLayout_content);
        txt_title = view.findViewById(R.id.txt_title);
        txt_cancel = view.findViewById(R.id.txt_cancel);
        mRecyclerView = view.findViewById(R.id.view_actionsheet_listview);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        if (title == null) {
            return this;
        }
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public ActionSheetDialog setShowCanceleButton(boolean cancel) {
        if (!cancel) {
            txt_cancel.setVisibility(View.GONE);
        }
        return this;
    }


    public ActionSheetDialog setSelectItem(int index) {
        itemIndex = index;
        return this;
    }

    public ActionSheetDialog setItemGravity(int gravity) {
        itemGravity = gravity;
        return this;
    }

    public ActionSheetDialog addFooter(@LayoutRes int foot) {
        footLayout = foot;
        return this;
    }

    public ActionSheetDialog addSheetItem(int iconResId, String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(iconResId != 0 ? context.getResources().getDrawable(iconResId) : null, strItem, color, listener, false));
        return this;
    }

    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(null, strItem, color, listener, false));
        return this;
    }

    /**
     * 添加item
     *
     * @param strItem   名字
     * @param isInvalid 是否无效(默认false)
     * @param color     颜色
     * @param listener  事件
     */
    public ActionSheetDialog addSheetItem(String strItem, boolean isInvalid, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(null, strItem, color, listener, isInvalid));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LayoutParams params = (LayoutParams) mRecyclerView.getLayoutParams();
            params.height = display.getHeight() / 2;
            mRecyclerView.setLayoutParams(params);
        }
        IOSDialogItemAdapter dialogItemAdapter = new IOSDialogItemAdapter(context, R.layout.item_actionsheetdialog, sheetItemList);
        dialogItemAdapter.setSelectItem(itemIndex);
//        if (footLayout > 0) {
//            View view = LayoutInflater.from(this.context).inflate(footLayout, null, false);
//            mListView.addFooterView(view);
//        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(dialogItemAdapter);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }

    public void setOnCancalListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
    }

    public void show() {
        setSheetItems();
        //  延迟10毫秒显示
        RxHelper.delay(10, TimeUnit.MILLISECONDS)
                .subscribe(integer -> dialog.show());

    }

    public Dialog getDialog() {
        return dialog;
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;
        Drawable icon;
        /**
         * 是否无效
         */
        boolean isInvalid;
        //int gravity;

        public SheetItem(Drawable icon, String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener, boolean isInvalid) {
            super();
            this.name = name;
            this.itemClickListener = itemClickListener;
            this.color = color;
            this.icon = icon;
            this.isInvalid = isInvalid;
        }


    }

    public enum SheetItemColor {
        Orange("#f77600"), Red("#FD4A2E"), Blue("#FF03a9f5");
        //int gravity;
        private String name;

        private Drawable icon;


		/*public int getGravity() {
			return gravity;
		}

		public void setGravity(int gravity) {
			this.gravity = gravity;
		}*/

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    class IOSDialogItemAdapter extends CommonAdapter<SheetItem> {

        LinearLayout mLayout;
        ImageView mIvIcon;
        TextView mTvTitle;
        ImageView mIvTick;
        private int selectItem = -1;

        public IOSDialogItemAdapter(Context context, int layoutId, List<SheetItem> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, SheetItem sheetItem, int position) {
            mLayout = holder.getView(R.id.item_actionsheetdialog_layout);
            if (itemGravity != Gravity.CENTER) {
                mLayout.setGravity(itemGravity);
            }
            mIvIcon = holder.getView(R.id.item_actionsheetdialog_imageview_icon);
            if (sheetItem.icon != null) {
                mIvIcon.setImageDrawable(sheetItem.icon);
                mIvIcon.setVisibility(View.VISIBLE);
            }
            mTvTitle = holder.getView(R.id.item_actionsheetdialog_textview_title);
            mIvTick = holder.getView(R.id.item_actionsheetdialog_imageview_tick);
            mTvTitle.setText(sheetItem.name);
            if (sheetItem.isInvalid) {
                mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black20));
                mLayout.setOnClickListener(null);
            } else {
                if (position == selectItem) {
                    mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black70));
                    mIvTick.setVisibility(View.VISIBLE);
                } else {
                    mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black70));
                    mIvTick.setVisibility(View.GONE);
                }
                mLayout.setOnClickListener(v -> {
                    sheetItem.itemClickListener.onClick(position);
                    dialog.dismiss();
                });
            }
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }
    }
}
