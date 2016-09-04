package com.dubinets.requisition.activity.navigation;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.dialog.ItemDialog;
import com.dubinets.requisition.activity.info.ItemInfo;
import com.dubinets.requisition.activity.menu.MenuStatment;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 02.09.2016.
 */
public class ItemActivity extends AppCompatActivity {
    private final String    ITEM_ID         = "ITEM_ID";
    private final String    ITEM_NAME       = "ITEM_NAME";
    private final String    ITEM_SHELFTIME  = "ITEM_SHELFTIME";
    private final String    ITEM_PRICE      = "ITEM_PRICE";

    private ListFragment    mListFragment;
    private SimpleAdapter   mAdapter;

    private ArrayList<HashMap<String, Object>> mItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list_items);

        this.mListFragment  = (ListFragment) getFragmentManager().findFragmentById(R.id.activity_navigation_list_item_listfragment);

        //-----------------------------------------------------------
        setmListFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setmListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_button:
                Intent intent = new Intent(this, ItemDialog.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setmListFragment() {
        if(!mItemList.isEmpty()) mItemList.clear();

        List<Item> items = DatabaseHelper.getItemDao().loadAll();
        for(Item item : items) {
            long    item_id         = item.getId();
            String  item_name       = item.getItem_name();
            String  item_shelftime  = "Срок годности: " + DatabaseHelper.getShelftimeDao().load(item.getShelftime_id()).getShelftime().toString() + " сут.";
            String  item_price      = "Цена: " + item.getItem_price() + " руб./кг.";


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(ITEM_ID,        item_id);
            hashMap.put(ITEM_NAME,      item_name);
            hashMap.put(ITEM_SHELFTIME, item_shelftime);
            hashMap.put(ITEM_PRICE,     item_price);
            mItemList.add(hashMap);
        }

        this.mAdapter = new SimpleAdapter(
                this,
                mItemList,
                R.layout.activity_navigation_list_items_item,
                new String[] {
                        ITEM_NAME,
                        ITEM_SHELFTIME,
                        ITEM_PRICE
                },
                new int[] {
                        R.id.activity_navigation_list_items_item_name,
                        R.id.activity_navigation_list_items_item_shelftime,
                        R.id.activity_navigation_list_items_item_price
                }
        );

        this.mListFragment.setListAdapter(mAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        String[] menuItems = getResources().getStringArray(R.array.menu);
        menu.add(0, MenuStatment.INFO_ID,    0,  menuItems[0]);
        menu.add(0, MenuStatment.EDIT_ID,    0,  menuItems[1]);
        menu.add(0, MenuStatment.DELETE_ID,  0,  menuItems[2]);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        HashMap<String, Object> hashMap         = (HashMap<String, Object>) mAdapter.getItem(info.position);
        Long item_id                            = (Long) hashMap.get(ITEM_ID);
        Intent intent                           = null;

        switch (item.getItemId()) {
            case MenuStatment.INFO_ID:
                intent = new Intent(this, ItemInfo.class);
                intent.putExtra("item_id", item_id);
                startActivity(intent);
                return true;

            case MenuStatment.EDIT_ID:
                intent = new Intent(this, ItemDialog.class);
                intent.putExtra("item_id", item_id);
                startActivity(intent);
                return true;

            case MenuStatment.DELETE_ID:
                DatabaseHelper.getItemDao().deleteByKey(item_id);
                setmListFragment();
                return true;
        }

        return super.onContextItemSelected(item);
    }
}
