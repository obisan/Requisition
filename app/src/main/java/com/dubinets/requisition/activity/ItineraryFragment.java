package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by dubinets on 25.08.2016.
 */
public class ItineraryFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT ).show();

        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getListAdapter().getItem(position);
        Long itinerary_id = (Long) hashMap.get("ITINERARY_ID");
        Intent intent = new Intent(getActivity(), ClientActivity.class);
        intent.putExtra("itinerary_id", itinerary_id);

        startActivity(intent);

    }
}
