package com.dubinets.requisition.activity.navigation;

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
import com.dubinets.requisition.activity.dialog.ClientDialog;
import com.dubinets.requisition.activity.info.ClientInfo;
import com.dubinets.requisition.activity.menu.MenuStatment;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private final String CLIENT_ID          = "CLIENT_ID";
    private final String CLIENT_NAME        = "CLIENT_NAME";
    private final String CLIENT_ADDRESS     = "CLIENT_ADDRESS";
    private final String CLIENT_COMMENTARY  = "CLIENT_COMMENTARY";

    private ListFragment    mListFragment;
    private SimpleAdapter   mAdapter;

    private ArrayList<HashMap<String, Object>> mClientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_list_clients);

        this.mListFragment = (ListFragment) getFragmentManager()
                .findFragmentById(R.id.activity_navigation_list_client_listfragment);

        //-------------------------------------------------------------------
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_add_button:
                intent = new Intent(this, ClientDialog.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setmListFragment() {
        if(!mClientList.isEmpty()) mClientList.clear();

        List<Client> clients = DatabaseHelper.getClientDao().loadAll();
        for(Client client : clients) {
            long client_id              = client.getId();
            String client_name          = client.getClient_name();
            String client_address       = client.getClient_address();
            String client_commentary    = client.getClient_commentary();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(CLIENT_ID,          client_id);
            hashMap.put(CLIENT_NAME,        client_name);
            hashMap.put(CLIENT_ADDRESS,     client_address);
            hashMap.put(CLIENT_COMMENTARY,  client_commentary);
            mClientList.add(hashMap);
        }

        this.mAdapter = new SimpleAdapter(
                this,
                mClientList,
                R.layout.activity_navigation_list_clients_item,
                new String[] {
                        CLIENT_NAME,
                        CLIENT_ADDRESS,
                        CLIENT_COMMENTARY
                },
                new int[] {
                        R.id.activity_navigation_list_client_item_name,
                        R.id.activity_navigation_list_client_item_address,
                        R.id.activity_navigation_list_client_list_item_commentary
                }
        );

        this.mListFragment.setListAdapter(this.mAdapter);
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
                intent = new Intent(this, ClientDialog.class);
                intent.putExtra("client_id", client_id);
                startActivity(intent);
                return true;

            case MenuStatment.DELETE_ID:
                DatabaseHelper.getSpotDao().deleteByKey(client_id);
                setmListFragment();
                return true;
        }

        return super.onContextItemSelected(item);
    }
}
