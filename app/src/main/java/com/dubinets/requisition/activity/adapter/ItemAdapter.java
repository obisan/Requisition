package com.dubinets.requisition.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.ItemActivity;

import java.util.ArrayList;

/**
 * Created by dubinets on 29.08.2016.
 */

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemTmp> mAdapterItemList;

    public ItemAdapter(
            Context context,
            ArrayList<ItemTmp> mAdapterItemList
            ) {
        this.mAdapterItemList       = mAdapterItemList;
        this.context                = context;
        this.inflater               = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAdapterItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_list_items_item, parent, false);

        }

        ItemTmp itemTmp = (ItemTmp) getItem(position);

        TextView item_name_txv          = (TextView) view.findViewById(R.id.list_item_name);
        TextView item_shelftime_txv     = (TextView) view.findViewById(R.id.list_item_shelftime);
        TextView item_count_txt         = (TextView) view.findViewById(R.id.list_item_number_item);

        Button buttonSubstract      = (Button) view.findViewById(R.id.list_item_substract_button);
        Button buttonAdd            = (Button) view.findViewById(R.id.list_item_add_button);

        buttonSubstract.setOnClickListener(mClickListener);
        buttonSubstract.setTag(position);
        buttonAdd.setOnClickListener(mClickListener);
        buttonAdd.setTag(position);

        Long item_id            = (Long) itemTmp.get(ItemActivity.ITEM_ID);
        String item_name        = (String) itemTmp.get(ItemActivity.ITEM_NAME);
        Integer item_shelftime  = (Integer) itemTmp.get(ItemActivity.ITEM_SHELFTIME);
        Integer item_count      = (Integer)  itemTmp.get(ItemActivity.ITEM_COUNT);

        item_name_txv       .setText(item_name);
        item_shelftime_txv  .setText("Срок годности: " + item_shelftime.toString() + " сут.");
        item_count_txt      .setText(item_count.toString());

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.list_item_item_framelayout_numbers);
        if(itemTmp.isVisible()) {
            view.setBackgroundColor(context.getResources().getColor(R.color.background));
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
            frameLayout.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position                = (int) v.getTag();
            TextView number_item_txv    = (TextView) ((View) v.getParent()).findViewById(R.id.list_item_number_item);
            ItemTmp item                = mAdapterItemList.get(position);
            Integer count               = (Integer) item.get(ItemActivity.ITEM_COUNT);

            switch (v.getId()) {
                case R.id.list_item_substract_button:
                    if(count == 1) return;
                    count--;
                    break;
                case R.id.list_item_add_button:
                    count++;
                    break;

            }

            item.put(ItemActivity.ITEM_COUNT, count);
            number_item_txv.setText(count.toString());
        }
    };



}
