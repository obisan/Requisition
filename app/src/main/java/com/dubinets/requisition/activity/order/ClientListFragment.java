package com.dubinets.requisition.activity.order;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.Cross_Itinerary_Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 25.08.2016.
 */
public class ClientListFragment extends ListFragment {
    private final String CLIENT_ID          = "CLIENT_ID";
    private final String CLIENT_NAME        = "CLIENT_NAME";
    private final String CLIENT_ADDRESS     = "CLIENT_ADDRESS";
    private final String NUMBER_POSITIONS   = "NUMBER_POSITIONS";

    private ArrayList<HashMap<String, Object>> mClientList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long itinerary_id = getActivity().getIntent().getLongExtra("itinerary_id", 1L);

        List<Client> clients = new ArrayList<Client>() ;
        for(Cross_Itinerary_Client cross : DatabaseHelper.getItineraryDao().load(itinerary_id).getCross_Itinerary_ClientList()) {
            Client client = DatabaseHelper.getClientDao().load(cross.getClient_id());
            clients.add(client);
        }

        for(Client client : clients) {
            int number_positions = client.getOrderList().size();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put(CLIENT_ID, client.getId());
            hashMap.put(CLIENT_NAME, client.getClient_name());
            hashMap.put(CLIENT_ADDRESS, client.getClient_address());
            hashMap.put(NUMBER_POSITIONS, "Количество позиций: " + number_positions);

            mClientList.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                mClientList,
                R.layout.list_client_item,
                new String[] {CLIENT_NAME, CLIENT_ADDRESS, NUMBER_POSITIONS},
                new int[] {
                        R.id.list_client_item_name,
                        R.id.list_client_item_address,
                        R.id.list_client_item_number_items
                }
        );

        setListAdapter(adapter);
    }
}
