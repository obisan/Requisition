package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SimpleAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 31.08.2016.
 */
public class ClientDialog extends AppCompatActivity {
    public static final String CLIENT_ID          = "CLIENT_ID";
    public static final String CLIENT_NAME        = "CLIENT_NAME";
    public static final String CLIENT_ADDRESS     = "CLIENT_ADDRESS";
    public static final String NUMBER_POSITIONS   = "NUMBER_POSITIONS";

    private Long itinerary_id;

    private ArrayList<HashMap<String, Object>> mClientList = new ArrayList<>();

    private ListFragment mListFragment;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_client);

        this.itinerary_id   = getIntent().getLongExtra("itinerary_id", 1L);

        this.mListFragment  = (ListFragment) getFragmentManager().findFragmentById(R.id.dialog_client_listfragment);

        setClientList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    public void setClientList() {
        if(!mClientList.isEmpty()) mClientList.clear();

        List<Client> clients            = DatabaseHelper.clientListFiltered(itinerary_id);
        for(Client client : clients) {
            Long client_id              = client.getId();
            Integer number_positions    = DatabaseHelper._queryClientItinerary_SpotList(itinerary_id, client_id).size();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(CLIENT_ID,          client.getId());
            hashMap.put(CLIENT_NAME,        client.getClient_name());
            hashMap.put(CLIENT_ADDRESS,     client.getClient_address());
            hashMap.put(NUMBER_POSITIONS,   "Количество позиций: " + number_positions);

            this.mClientList.add(hashMap);
        }

        this.mAdapter = new SimpleAdapter(
                this,
                mClientList,
                R.layout.dialog_client_item,
                new String[] {
                        CLIENT_NAME,
                        CLIENT_ADDRESS
                },
                new int[] {
                        R.id.dialog_client_item__name,
                        R.id.dialog_client_item_address
                }
        );

        this.mListFragment.setListAdapter(mAdapter);
    }
}
