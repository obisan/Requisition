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
import com.dubinets.requisition.activity.dialog.ClientDialog;
import com.dubinets.requisition.activity.dialog.TypeDialog;
import com.dubinets.requisition.activity.info.ClientInfo;
import com.dubinets.requisition.activity.menu.MenuStatment;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 07.09.2016.
 */
public class TypeActivity extends AppCompatActivity {
    private final String TYPE_ID = "TYPE_ID";
    private final String TYPE_NAME = "TYPE_NAME";

    private ListFragment mListFragment;

    private ArrayList<HashMap<String, Object>> mTypesList = new ArrayList<>();
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list_types);

        this.mListFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.activity_navigation_list_types_listfragment);

        //-------------------------------------

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
                Intent intent = new Intent(this, TypeDialog.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Long type_id                            = (Long) hashMap.get(TYPE_ID);
        Intent intent                           = null;

        switch (item.getItemId()) {
            case MenuStatment.INFO_ID:
                intent = new Intent(this, ClientInfo.class);
                intent.putExtra("type_id", type_id);
                startActivity(intent);
                return true;

            case MenuStatment.EDIT_ID:
                intent = new Intent(this, ClientDialog.class);
                intent.putExtra("type_id", type_id);
                startActivity(intent);
                return true;

            case MenuStatment.DELETE_ID:
                Type type = DatabaseHelper.getTypeDao().load(type_id);
                for (Item item1 : type.getItemList()) {
                    item1.setType_id(null);
                }
                DatabaseHelper.getTypeDao().deleteByKey(type_id);

                setmListFragment();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void setmListFragment() {
        if(!mTypesList.isEmpty()) mTypesList.clear();

        List<Type> types = DatabaseHelper.getTypeDao().loadAll();
        for(Type type : types) {
            Long type_id        = type.getId();
            String type_name    = type.getType_name();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(TYPE_ID,    type_id);
            hashMap.put(TYPE_NAME,  type_name);

            mTypesList.add(hashMap);
        }

        this.mAdapter = new SimpleAdapter(
                this,
                mTypesList,
                R.layout.activity_navigation_list_types_item,
                new String[] {
                        TYPE_NAME
                },
                new int[] {
                        R.id.activity_navigation_list_types_item_name
                }
        );

        this.mListFragment.setListAdapter(mAdapter);
    }

}
