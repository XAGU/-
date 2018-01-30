package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * 充值提现详情
 *
 * @author zcd
 * @date 17/10/27
 */

public class WithdrawRechargeDetailAdapter extends MultiItemTypeAdapter<WithdrawRechargeDetailAdapter.Item> {
    public static final int TITLE_CONTENT_TYPE = 1;
    public static final int TITLE_CONTENT_COPY_TYPE = 2;
    public static final int TITLE_CONTENT_LIST_TYPE = 3;

    public WithdrawRechargeDetailAdapter(Context context, List<Item> datas) {
        super(context, datas);
    }

    @Data
    public static class Item {
        private String title;
        private String content;
        private int type;

//        public Item(String title, String content) {
//            this.title = title;
//            this.content = content;
//        }


        public Item(int type) {
            this.type = type;
        }

        public Item(String title, String content, int type) {
            this.title = title;
            this.content = content;
            this.type = type;
        }
    }
}
