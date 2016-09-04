package com.dubinets.requisition.activity.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Shelftime;
import com.dubinets.requisition.db.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 02.09.2016.
 */
public class ItemDialog extends AppCompatActivity {
    private final String    ITEM_ID         = "ITEM_ID";
    private final String    ITEM_NAME       = "ITEM_NAME";
    private final String    ITEM_PRICE      = "ITEM_PRICE";
    private final String    SHELFTIME_ID    = "SHELFTIME_ID";
    private final String    SHELFTIME       = "ITEM_SHELFTIME";
    private final String    PHOTO_ID        = "PHOTO_ID";
    private final String    PHOTO           = "PHOTO";
    private final String    TYPE_ID         = "TYPE_ID";
    private final String    TYPE_NAME       = "TYPE_NAME";
    private final long      NULL            = 0L;

    private Long        item_id;
    private Item        item;

    private EditText    item_name_et;
    private EditText    item_shelftime_et;
    private EditText    item_price_et;

    private ArrayList<HashMap<String, Object>>  mTypeList    = new ArrayList<>();
    private Spinner                             mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dialog_item);

        this.item_name_et       = (EditText) findViewById(R.id.activity_navigation_dialog_item_name);
        this.item_shelftime_et  = (EditText) findViewById(R.id.activity_navigation_dialog_item_shelftime);
        this.item_price_et      = (EditText) findViewById(R.id.activity_navigation_dialog_item_price);

        this.mSpinner   = (Spinner) findViewById(R.id.activity_navigation_dialog_item_spinner);
        this.item_id    = getIntent().getLongExtra("item_id", NULL);
        this.item       = getItemById(item_id);

        setmSpinner();
        setEditText();
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
                setItem2DB();

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setEditText() {
        if(item.getId() != null) {
            Shelftime   shelftime   = DatabaseHelper.getShelftimeDao().load(item.getShelftime_id());
            Type        type        = DatabaseHelper.getTypeDao().load(item.getType_id());

            item_name_et        .setText(item.getItem_name());
            item_shelftime_et   .setText(shelftime.getShelftime().toString());
            item_price_et       .setText(item.getItem_price().toString());

            int position = getPositionTypeByID(item.getType_id());

            mSpinner.setSelection(position);
        }
    }

    private void setmSpinner() {
        List<Type> types = DatabaseHelper.getTypeDao().loadAll();

        for(Type type : types) {
            long    type_id     = type.getId();
            String  type_name   = type.getType_name();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(TYPE_ID,    type_id);
            hashMap.put(TYPE_NAME,  type_name);
            mTypeList.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                mTypeList,
                R.layout.activity_navigation_list_items_spinner_item,
                new String[] {
                        TYPE_NAME
                },
                new int[] {
                        R.id.activity_navigation_list_items_spinner_item_name
                }
        );

        mSpinner.setAdapter(adapter);

        mSpinner.setSelection(0);
    }

    private void setItem2DB() {
        String item_name        = this.item_name_et.getText().toString();
        String item_shelftime   = this.item_shelftime_et.getText().toString();
        String item_price       = this.item_price_et.getText().toString();

        Long shelftime_id   = DatabaseHelper.getShelftimeByValue(Integer.valueOf(item_shelftime));
        Long type_id        = (Long) ((HashMap<String, Object>) mSpinner.getSelectedItem()).get(TYPE_ID);

        item.setItem_name(item_name);
        item.setItem_price(Double.valueOf(item_price.toString()));
        item.setShelftime_id(shelftime_id);
        item.setType_id(type_id);

        synchronized (this) {
            if(item.getId() == null) {
                DatabaseHelper.getItemDao().insert(item);
            } else {
                DatabaseHelper.getItemDao().update(item);
            }
        }

    }

    private Item getItemById(long id) {
        Item item = null;
        if(id != NULL) {
            item = DatabaseHelper.getItemDao().load(id);
        } else {
            item = new Item(null);
        }
        return item;
    }

    private Integer getPositionTypeByID(Long id) {
        SpinnerAdapter adapter = mSpinner.getAdapter();
        for(int i = 0; i < adapter.getCount(); i++) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) adapter.getItem(i);
            Long type_id                    = (Long) hashMap.get(TYPE_ID);
            if(type_id == id) return i;
        }
        return 0;
    }

}
