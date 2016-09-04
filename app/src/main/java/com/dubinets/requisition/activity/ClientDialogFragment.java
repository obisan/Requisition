package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.CrossItineraryClient;

import java.util.HashMap;

/**
 * Created by dubinets on 31.08.2016.
 */
public class ClientDialogFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getListAdapter().getItem(position);
        Long client_id                  = (Long) hashMap.get(ClientActivity.CLIENT_ID);
        Long itinerary_id               = getActivity().getIntent().getLongExtra("itinerary_id", 1L);

        CrossItineraryClient client     = new CrossItineraryClient(null, itinerary_id, client_id);

        synchronized (this) {
            DatabaseHelper.getCrossItineraryClientDao().insert(client);
        }

        getActivity().finish();

    }
}
