package com.dubinets.requisition.activity.order;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Itinerary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 25.08.2016.
 */
public class ItineraryListFragment extends ListFragment {
    private final String ITINERARY_ID = "ITINERARY_ID";
    private final String ITINERARY_NAME = "ITINERARY_NAME";
    private final String NUMBER_CLIENTS = "NUMBER_CLIENTS";
    private final String DAY_OF_WEEK = "DAY_OF_WEEK";

    private ArrayList<HashMap<String, Object>> mItineraryList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Itinerary> itineraries = DatabaseHelper.getItineraryDao().loadAll();

        for(Itinerary itinerary : itineraries) {
            int numberClients = itinerary.getCross_Itinerary_ClientList().size();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(ITINERARY_ID, itinerary.getId());
            hashMap.put(DAY_OF_WEEK, itinerary.getDay_of_week());
            hashMap.put(ITINERARY_NAME, itinerary.getItinerary_name());
            hashMap.put(NUMBER_CLIENTS, "Количество точек: " + numberClients);

            mItineraryList.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                mItineraryList,
                R.layout.list_itinerary_item,
                new String[] {
                        DAY_OF_WEEK,
                        //ITINERARY_NAME,
                        NUMBER_CLIENTS
                },
                new int[] {
                        R.id.list_itinerary_item_text_day_of_week,
                        //R.id.list_itinerary_item_itinerary_name,
                        R.id.list_itenerary_item_number_clients
                }
        );

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT ).show();

        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getListAdapter().getItem(position);
        Long itinerary_id = (Long) hashMap.get(ITINERARY_ID);
        Intent intent = new Intent(getActivity(), ClientListActivity.class);
        intent.putExtra("itinerary_id", itinerary_id);

        startActivity(intent);

    }
}
