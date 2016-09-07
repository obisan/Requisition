package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.info.ItemInfo;
import com.dubinets.requisition.activity.menu.MenuStatment;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Count;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Shelftime;
import com.dubinets.requisition.db.Spot;
import com.dubinets.requisition.mail.EmailGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 27.08.2016.
 */
public class SpotActivity extends AppCompatActivity {
    public static final String SPOT_ID         = "SPOT_ID";
    public static final String ITEM_NAME       = "ITEM_NAME";
    public static final String ITEM_COUNT      = "ITEM_COUNT";
    public static final String ITEM_SHELFTIME  = "ITEM_COUNT_SHELFTIME";

    private ArrayList<HashMap<String, Object>>  mSpotList = new ArrayList<>();
    private SimpleAdapter                       mAdapter;

    private long client_id;
    private long itinerary_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_spots);

        this.client_id      = getIntent().getLongExtra("client_id"      , 1L);
        this.itinerary_id   = getIntent().getLongExtra("itinerary_id"   , 1L);

        setSpotList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setSpotList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mail_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_mail_email:
                sendEmail();
                return true;

            case R.id.menu_mail_add:
                intent = new Intent(this, ItemActivity.class);
                intent.putExtra("client_id",    client_id);
                intent.putExtra("itinerary_id", itinerary_id);
                startActivity(intent);
                return true;

            case R.id.menu_mail_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSpotList() {
        if(!mSpotList.isEmpty()) mSpotList.clear();
        List<Spot> clientSpots = DatabaseHelper._queryClientItinerary_SpotList(client_id, itinerary_id);

        for(Spot spot : clientSpots) {
            long item_id    = spot.getItem_id();
            long count_id   = spot.getCount_id();
            long spot_id    = spot.getId();

            Item item = DatabaseHelper.getItemDao().load(item_id);
            Count count = DatabaseHelper.getCountDao().load(count_id);

            long shelftime_id = item.getShelftime_id();
            Shelftime shelftime = DatabaseHelper.getShelftimeDao().load(shelftime_id);

            String item_name = item.getItem_name();
            Integer item_count = count.getCount();
            Integer item_shelftime = shelftime.getShelftime();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(SPOT_ID, spot_id);
            hashMap.put(ITEM_NAME, item_name);
            hashMap.put(ITEM_COUNT, "Количество: " + item_count);
            hashMap.put(ITEM_SHELFTIME, "Срок годности: " + item_shelftime + " сут.");
            mSpotList.add(hashMap);
        }

        this.mAdapter = new SimpleAdapter(
                this,
                mSpotList,
                R.layout.activity_list_spots_item,
                new String[] {
                        ITEM_NAME,
                        ITEM_COUNT,
                        ITEM_SHELFTIME
                },
                new int[] {
                        R.id.list_spot_item_name,
                        R.id.list_spot_item_count,
                        R.id.list_spot_item_shelftime
                }
        );

        ListFragment fragment = (ListFragment) getFragmentManager().findFragmentById(R.id.activity_list_spots_listfragment_spots);
        fragment.setListAdapter(mAdapter);
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
        Long spot_id                            = (Long) hashMap.get(SpotActivity.SPOT_ID);
        Intent intent                           = null;

        switch (item.getItemId()) {
            case MenuStatment.INFO_ID:
                Spot spot = DatabaseHelper.getSpotDao().load(spot_id);
                Long item_id = DatabaseHelper.getItemDao().load(spot.getItem_id()).getId();
                intent = new Intent(this, ItemInfo.class);
                intent.putExtra("item_id", item_id);
                startActivity(intent);
                return true;

            case MenuStatment.DELETE_ID:
                DatabaseHelper.getSpotDao().deleteByKey(spot_id);

                setSpotList();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void sendEmail() {
        String subject      = EmailGenerator.subject_client(this.itinerary_id, this.client_id);
        String body         = EmailGenerator.body_client(this.itinerary_id, this.client_id);
        String recipients   = PreferenceManager.getDefaultSharedPreferences(this).getString("emails", "");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,     new String[] {recipients} );
        intent.putExtra(Intent.EXTRA_SUBJECT,   subject );
        intent.putExtra(Intent.EXTRA_TEXT,      body );

        try {
            startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
