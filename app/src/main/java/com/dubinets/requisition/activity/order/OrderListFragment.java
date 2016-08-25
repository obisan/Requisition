package com.dubinets.requisition.activity.order;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.Order;
import com.dubinets.requisition.locale.Locale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 23.08.2016.
 */
public class OrderListFragment extends ListFragment {
    private final String CLIENT_NAME = "CLIENT_NAME";
    private final String CREATION_DATE= "CREATION_DATE";
    private final String NUMBER_SPOTS = "NUMBER_SPOTS";
    private final String ORDER_ID = "ORDER_ID";

    private ArrayList<HashMap<String, Object>> mOrderList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Order> orders = DatabaseHelper.getOrderDao().loadAll();
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                return rhs.getCreation_date().compareTo(lhs.getCreation_date());
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("E dd-MMMM", Locale.RUSSIAN);
        for(Order order : orders) {
            Client client = DatabaseHelper.getClientDao().load(order.getClient_id());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(CREATION_DATE, sdf.format(order.getCreation_date()));
            hashMap.put(CLIENT_NAME, client.getClient_name());
            hashMap.put(NUMBER_SPOTS, "Количество позиций: " + order.getSpotList().size());
            hashMap.put(ORDER_ID, order.getId());

            mOrderList.add(hashMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                mOrderList,
                R.layout.list_order_item,
                new String[] {CLIENT_NAME, CREATION_DATE, NUMBER_SPOTS},
                new int[]{
                        R.id.list_order_item_client_name,
                        R.id.list_order_item_creation_date,
                        R.id.list_order_item_number_spots}
                );

        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT ).show();

        HashMap<String, Object> hashMap = (HashMap<String, Object>) this.getListAdapter().getItem(position);
        Long order_id = (Long) hashMap.get(ORDER_ID);
        Intent intent = new Intent(getActivity(), OrderDetailedActivity.class);
        intent.putExtra("order_id", order_id);

        startActivity(intent);

    }
}
