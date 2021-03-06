package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/3
 */
public class ChooseBathroomOuterAdapter extends CommonAdapter<ChooseBathroomOuterAdapter.BathGroupWrapper> {
    private Map<Integer, ChooseBathroomAdapter> adapters = new HashMap<>();
    private Context context;
    private BathroomOuterSelectListener listener;

    public ChooseBathroomOuterAdapter(Context context, int layoutId, List<BathGroupWrapper> datas,
                                      BathroomOuterSelectListener listener) {
        super(context, layoutId, datas);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, BathGroupWrapper bathGroupWrapper, int position) {
        if (null == adapters.get(position)) {
            ChooseBathroomAdapter adapter =
                    new ChooseBathroomAdapter(context, R.layout.item_choose_bathroom,
                            bathGroupWrapper.getBathGroups(), new ChooseBathroomAdapter.BathroomSelectListener() {
                        @Override
                        public void onBathroomSelected(int bathroomPosition) {
                            if (listener != null) {
                                listener.onBathroomSelected(position, bathroomPosition);
                            }
                        }
                    });
            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (recyclerView.getItemDecorationAt(0) == null) {
                recyclerView.addItemDecoration(
                        new GridSpacesItemDecoration(bathGroupWrapper.getSpanWidth(),
                                ScreenUtils.dpToPxInt(context, 3), false));
            }
            recyclerView.setLayoutManager(new GridLayoutManager(context, bathGroupWrapper.getSpanWidth()));
            recyclerView.setAdapter(adapter);
            adapters.put(position, adapter);
        } else {
            ChooseBathroomAdapter adapter = adapters.get(position);
            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (recyclerView.getItemDecorationAt(0) == null) {
                recyclerView.addItemDecoration(
                        new GridSpacesItemDecoration(bathGroupWrapper.getSpanWidth(),
                                ScreenUtils.dpToPxInt(context, 3), false));
            }
            recyclerView.setLayoutManager(new GridLayoutManager(context, bathGroupWrapper.getSpanWidth()));
            recyclerView.setAdapter(adapter);
        }
        holder.setText(R.id.tv_content, bathGroupWrapper.getName());
    }


    public static final class BathGroupWrapper {
        List<ChooseBathroomAdapter.BathroomWrapper> bathGroups;
        String name;
        int spanWidth = 3;

        public BathGroupWrapper() {
        }

        public BathGroupWrapper(List<ChooseBathroomAdapter.BathroomWrapper> bathGroups, String name) {
            this.bathGroups = bathGroups;
            this.name = name;
        }

        public List<ChooseBathroomAdapter.BathroomWrapper> getBathGroups() {
            return bathGroups;
        }

        public void setBathGroups(List<ChooseBathroomAdapter.BathroomWrapper> bathGroups) {
            this.bathGroups = bathGroups;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSpanWidth() {
            return spanWidth;
        }

        public void setSpanWidth(int spanWidth) {
            this.spanWidth = spanWidth;
        }
    }

    public boolean isScaled() {
        return adapters.get(0).isScaled();
    }

    public void setScaled(boolean scaled) {
        Iterator iterator = adapters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ChooseBathroomAdapter adapter = (ChooseBathroomAdapter) entry.getValue();
            adapter.setScaled(scaled);
            adapter.notifyDataSetChanged();
        }
    }

    public interface BathroomOuterSelectListener {
        void onBathroomSelected(int groupPosition, int bathroomPosition);
    }
}
