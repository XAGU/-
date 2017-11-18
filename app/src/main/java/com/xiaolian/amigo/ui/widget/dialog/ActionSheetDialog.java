package com.xiaolian.amigo.ui.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.ArrayList;
import java.util.List;

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
    private ListView mListView;
    private int itemGravity= Gravity.CENTER;
    private int itemIndex=-1;
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
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view
                .findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        mListView=(ListView) view.findViewById(R.id.view_actionsheet_listview);
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
        itemIndex=index;
        return this;
    }

    public ActionSheetDialog setItemGravity(int gravity) {
        itemGravity=gravity;
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
        sheetItemList.add(new SheetItem(iconResId!=0?context.getResources().getDrawable(iconResId):null,strItem, color, listener,false));
        return this;
    }

    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(null,strItem, color, listener,false));
        return this;
    }
    /**
     * 添加item
     * @param strItem 名字
     * @param isInvalid 是否无效(默认false)
     * @param color 颜色
     * @param listener 事件
     */
    public ActionSheetDialog addSheetItem(String strItem, boolean isInvalid, SheetItemColor color, OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(null,strItem, color, listener,isInvalid));
        return this;
    }

    /** 设置条目布局 */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LayoutParams params = (LayoutParams) mListView.getLayoutParams();
            params.height = display.getHeight() / 2;
            mListView.setLayoutParams(params);
        }
		/*
		// 循环添加条目
		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;
			LinearLayout linearLayout=new LinearLayout(context);
			LinearLayout.LayoutParams linearLayout_lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			linearLayout.setGravity(sheetItem.gravity);
			int layoutpadding=DensityUtil.dip2px(context, 10);
			linearLayout.setPadding(layoutpadding, layoutpadding, layoutpadding, layoutpadding);
			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(18);
			textView.setGravity(Gravity.CENTER);
			textView.setSingleLine(true);
			if (sheetItem.icon!=null) {
				Drawable icon = sheetItem.icon;
				icon.setBounds(0, 0, DensityUtil.dip2px(context, 22), DensityUtil.dip2px(context, 22));
				textView.setCompoundDrawables(icon, null, null, null);
				textView.setCompoundDrawablePadding(DensityUtil.dip2px(context, 10));
				ImageView imageView=new ImageView(context);
				imageView.setImageDrawable(icon);
				LinearLayout.LayoutParams imageView_lp=new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 22), DensityUtil.dip2px(context, 22));
				imageView_lp.setMargins(0, 0, layoutpadding, 0);
				linearLayout.addView(imageView, imageView_lp);
			}
			linearLayout.setBackgroundResource(R.drawable.amile_btn_corners_white_color);
			// 背景图片
			if (size == 1) {
				if (showTitle) {
					linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					linearLayout.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						linearLayout.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						linearLayout.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						linearLayout.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}

			// 字体颜色
			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Orange.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}

			// 高度
			//float scale = context.getResources().getDisplayMetrics().density;
			//int height = (int) (45 * scale + 0.5f);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			// 点击事件
			linearLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					dialog.dismiss();
				}
			});
			linearLayout.addView(textView);
			lLayout_content.addView(linearLayout,linearLayout_lp);
		}*/
        IOSDialogItemAdapter dialogItemAdapter=new IOSDialogItemAdapter(sheetItemList, context);
        dialogItemAdapter.setSelectItem(itemIndex);
        if (footLayout > 0) {
            View view = LayoutInflater.from(this.context).inflate(footLayout, null, false);
            mListView.addFooterView(view);
        }
        mListView.setAdapter(dialogItemAdapter);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }

    public void setOnCancalListener(DialogInterface.OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
    }

    public void show() {
        setSheetItems();
        dialog.show();
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
        /** 是否无效 */
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
        Orange("#f77600"), Red("#FD4A2E"),Blue("#FF03a9f5");
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


    class IOSDialogItemAdapter extends BaseAdapter {
        private List<SheetItem> mListModels;
        private Context mContext;
        private LayoutInflater mInflater=null;

        public IOSDialogItemAdapter(List<SheetItem> mListModels,
                                    Context mContext) {
            super();
            this.mListModels = mListModels;
            this.mContext = mContext;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mListModels.size();
        }

        @Override
        public Object getItem(int position) {
            return mListModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null) {
                convertView = mInflater.inflate(R.layout.item_actionsheetdialog, null);
                holder = new ViewHolder();
                holder.mLayout=(LinearLayout) convertView.findViewById(R.id.item_actionsheetdialog_layout);
                holder.mIvIcon=(ImageView) convertView.findViewById(R.id.item_actionsheetdialog_imageview_icon);
                holder.mTvTitle=(TextView) convertView.findViewById(R.id.item_actionsheetdialog_textview_title);
                holder.mIvTick=(ImageView) convertView.findViewById(R.id.item_actionsheetdialog_imageview_tick);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (itemGravity!= Gravity.CENTER) {
                holder.mLayout.setGravity(itemGravity);
            }
            if (mListModels.get(position).icon!=null) {
                holder.mIvIcon.setImageDrawable(mListModels.get(position).icon);
                holder.mIvIcon.setVisibility(View.VISIBLE);
            }
            holder.mTvTitle.setText(mListModels.get(position).name);
            // 字体颜色
			/*if (mListModels.get(position).color == null) {
				holder.mTvTitle.setTextColor(Color.parseColor(SheetItemColor.Orange.getName()));
			} else {
				holder.mTvTitle.setTextColor(Color.parseColor(mListModels.get(position).color.getName()));
			}*/
            //holder.mTvTitle.setTextColor(mListModels.get(position).color);
            // 点击事件
            if (mListModels.get(position).isInvalid) {
                holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black20));
                holder.mLayout.setOnClickListener(null);
            }else{
                if (position == selectItem) {
                    holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black70));
                    holder.mIvTick.setVisibility(View.VISIBLE);
                }
                else {
                    holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.black70));
                    holder.mIvTick.setVisibility(View.GONE);
                }
                holder.mLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListModels.get(position).itemClickListener.onClick(position);
                        dialog.dismiss();
                    }
                });
            }


            return convertView;
        }
        public  void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }
        private int  selectItem=-1;
        private class ViewHolder{
            LinearLayout mLayout;
            TextView mTvTitle;
            ImageView mIvIcon;
            ImageView mIvTick;
        }

    }
}
