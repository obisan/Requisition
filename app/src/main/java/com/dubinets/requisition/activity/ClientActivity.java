package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.info.ClientInfo;
import com.dubinets.requisition.activity.menu.MenuStatment;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.CrossItineraryClient;
import com.dubinets.requisition.db.Itinerary;
import com.dubinets.requisition.db.Spot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 25.08.2016.
 */
public class ClientActivity extends AppCompatActivity {
    public static final String CLIENT_ID          = "CLIENT_ID";
    public static final String CLIENT_NAME        = "CLIENT_NAME";
    public static final String CLIENT_ADDRESS     = "CLIENT_ADDRESS";
    public static final String NUMBER_POSITIONS   = "NUMBER_POSITIONS";

    private Long itinerary_id;

    private ArrayList<HashMap<String, Object>> mClientList = new ArrayList<>();

    private ListFragment    mListFragment;
    private SimpleAdapter   mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients);

        this.itinerary_id   = getIntent().getLongExtra("itinerary_id", 1L);

        this.mListFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.listframent_client);

        setClientList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setClientList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mail_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mail_email:
                sendEmail();
                return true;

            case R.id.menu_mail_add:
                Intent intent = new Intent(this, ClientDialog.class);
                intent.putExtra("itinerary_id", itinerary_id);
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
        Long client_id                          = (Long) hashMap.get(CLIENT_ID);
        Intent intent                           = null;

        switch (item.getItemId()) {
            case MenuStatment.INFO_ID:
                intent = new Intent(this, ClientInfo.class);
                intent.putExtra("client_id", client_id);
                startActivity(intent);
                return true;

            case MenuStatment.EDIT_ID:
                intent = new Intent(this, com.dubinets.requisition.activity.dialog.ClientDialog.class);
                intent.putExtra("client_id", client_id);
                startActivity(intent);
                return true;

            case MenuStatment.DELETE_ID:
                List<Spot> spots                        = DatabaseHelper._queryClientItinerary_SpotList(itinerary_id, client_id);
                for(Spot spot : spots) {
                    DatabaseHelper.getSpotDao().deleteByKey(spot.getId());
                }

                Itinerary itinerary                     = DatabaseHelper.getItineraryDao().load(itinerary_id);
                List<CrossItineraryClient> clientList   = itinerary.getCrossItineraryClientList();
                for(CrossItineraryClient client : clientList) {
                    if(client.getClient_id() == client_id)
                        DatabaseHelper.getCrossItineraryClientDao().deleteByKey(client.getId());
                }

                setClientList();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void setClientList() {
        if(!mClientList.isEmpty()) mClientList.clear();

        List<Client> clients = new ArrayList<>();
        for(CrossItineraryClient crossItineraryClient : DatabaseHelper.getCrossItineraryClientDao()._queryItinerary_CrossItineraryClientList(itinerary_id)) {
            Long client_id  = crossItineraryClient.getClient_id();
            Client client   = DatabaseHelper.getClientDao().load(client_id);
            clients.add(client);
        }

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
                R.layout.activity_list_clients_item,
                new String[] {
                        CLIENT_NAME,
                        CLIENT_ADDRESS,
                        NUMBER_POSITIONS
                },
                new int[] {
                        R.id.list_client_item_name,
                        R.id.list_client_item_address,
                        R.id.list_client_item_number_items
                }
        );
        this.mListFragment.setListAdapter(mAdapter);
    }

    private void sendEmail() {

    }
}
