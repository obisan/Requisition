package com.dubinets.requisition.activity.order;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Count;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Shelftime;
import com.dubinets.requisition.db.Spot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 24.08.2016.
 */
public class SpotListFragment extends ListFragment {
    private final String ITEM_NAME = "ITEM_NAME";
    private final String ITEM_COUNT = "ITEM_COUNT";
    private final String ITEM_SHELFTIME = "ITEM_COUNT_SHELFTIME";

    private ArrayList<HashMap<String, Object>> mSpotList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Long order_id = getActivity().getIntent().getLongExtra("order_id", 1L);
        List<Spot> spots = DatabaseHelper.getSpotDao()._queryOrder_SpotList(order_id);

        for(Spot spot : spots) {
            HashMap<String, Object> hashMap = new HashMap<>();

            Item item = DatabaseHelper.getItemDao().load(spot.getItem_id());
            Count count = DatabaseHelper.getCountDao().load(spot.getCount_id());
            Shelftime shelftime = DatabaseHelper.getShelftimeDao().load(item.getShelftime_id());

            hashMap.put(ITEM_NAME, item.getItem_name());
            hashMap.put(ITEM_COUNT, "Количество: " + count.getCount());
            hashMap.put(ITEM_SHELFTIME, "Срок годности: " + shelftime.getShelftime() + " дн.");

            mSpotList.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                mSpotList,
                R.layout.list_spot_item,
                new String[] {ITEM_NAME, ITEM_COUNT, ITEM_SHELFTIME},
                new int[] {
                        R.id.list_spot_item_name,
                        R.id.list_spot_item_count,
                        R.id.list_spot_item_shelftime
                }
        );

        setListAdapter(adapter);

    }
}
