package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.support.annotation.IntDef;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import lombok.Data;

public class SchoolAdapter extends MultiItemTypeAdapter<SchoolAdapter.SchoolWrapper> {
    public  static  final int TITLE = 2 ;
    public static final int SCHOOL_NAME = 1 ;

    public SchoolAdapter(Context context, List<SchoolWrapper> datas) {
        super(context, datas);
    }


   @Data
   public final static class SchoolWrapper{


       @ITEM_TYPE int itemType ;

       String content ;

       public SchoolWrapper(@ITEM_TYPE  int itemType ,String content){
           setContent(content);
           setItemType(itemType);
       }

        @ITEM_TYPE
       public int getItemType() {
           return itemType;
       }

       public void setItemType( @ITEM_TYPE int itemType) {
           this.itemType = itemType;
       }

       public String getContent() {
           return content;
       }

       public void setContent(String content) {
           this.content = content;
       }

       //用 @IntDef "包住" 常量；
       // @Retention 定义策略
       // 声明构造器
       @IntDef({TITLE ,SCHOOL_NAME})
       @Retention(RetentionPolicy.SOURCE)
       public @interface ITEM_TYPE {
       }
    }
}
