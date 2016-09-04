package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.adapter.ItemAdapter;
import com.dubinets.requisition.activity.adapter.ItemTmp;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Spot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubinets on 29.08.2016.
 */
public class ItemActivity extends AppCompatActivity {
    public final static String ITEM_ID            = "ITEM_ID";
    public final static String ITEM_NAME          = "ITEM_NAME";
    public final static String ITEM_SHELFTIME     = "ITEM_SHELFTIME";
    public final static String ITEM_PRICE         = "ITEM_PRICE";
    public final static String ITEM_COUNT         = "ITEM_COUNT";

    private ArrayList<ItemTmp>  mItemList = new ArrayList<>();
    private ListFragment        mListFragment;
    private ItemAdapter         mItemAdapter;

    private long itinerary_id;
    private long client_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        this.itinerary_id   = getIntent().getLongExtra("itinerary_id", 1L);
        this.client_id      = getIntent().getLongExtra("client_id", 1L);

        this.mListFragment = (ListFragment) getFragmentManager()
                .findFragmentById(R.id.activity_list_item_listfragment);

        setItemList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_button:

                for(ItemTmp itemTmp : mItemList) {
                    if(itemTmp.isVisible()) {
                        Spot spot = new Spot();
                        spot.setClient_id(this.client_id);
                        spot.setItinerary_id(this.itinerary_id);
                        spot.setItem_id((Long) itemTmp.get(ITEM_ID));
                        spot.setCount_id( DatabaseHelper.getCountIdByValue((Integer) itemTmp.get(ITEM_COUNT)));

                        DatabaseHelper.getSpotDao().insert(spot);
                    }
                }

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setItemList() {
        List<Item> items = DatabaseHelper.itemListFiltered(itinerary_id, client_id);

        for(Item item : items) {
            long item_id        = item.getId();
            long shelftime_id   = item.getShelftime_id();

            String item_name        = item.getItem_name();
            Integer item_shelftime  = DatabaseHelper.getShelftimeDao().load(shelftime_id).getShelftime();
            Double item_price       = item.getItem_price();
            int item_count          = 1;

            ItemTmp itemTmp = new ItemTmp();
            itemTmp.put(ITEM_ID, item_id);
            itemTmp.put(ITEM_NAME, item_name);
            itemTmp.put(ITEM_PRICE, item_price);
            itemTmp.put(ITEM_SHELFTIME, item_shelftime);
            itemTmp.put(ITEM_COUNT, item_count);

            itemTmp.setVisible(false);

            mItemList.add(itemTmp);
        }

        this.mItemAdapter = new ItemAdapter(
                this,
                mItemList
        );

        mListFragment.setListAdapter(this.mItemAdapter);
    }
}
