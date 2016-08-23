package com.dubinets.requisition.activity.order;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubinets on 23.08.2016.
 */
public class OrderListActivity extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> list = new ArrayList<>();

        List<Client> clients = DatabaseHelper.getHelper().getClientDao().loadAll();

        for(Client client : clients) {
            list.add(client.getClient_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                list);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setListAdapter(adapter);
    }

}
