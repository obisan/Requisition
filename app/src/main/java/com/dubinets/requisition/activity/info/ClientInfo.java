package com.dubinets.requisition.activity.info;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.Telephone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dubinets on 04.09.2016.
 */
public class ClientInfo extends AppCompatActivity {
    private final String TELEPHONE_ID       = "TELEPHONE_ID";
    private final String TELEPHONE_NUMBER   = "TELEPHONE";
    private final String TELEPHONE_NAME     = "TELEPHONE_NAME";

    private TextView    client_name_tv;
    private TextView    client_address_tv;
    private TextView    client_commentary_tv;

    private ListFragment    mTelephoneListFragment;

    private ArrayList<HashMap<String, Object>> mTelephoneList = new ArrayList<>();
    private SimpleAdapter   adapter;

    private long            client_id;
    private Client          client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_client);

        this.client_id              = getIntent().getLongExtra("client_id", 0L);
        this.client                 = DatabaseHelper.getClientDao().load(client_id);

        this.client_name_tv         = (TextView) findViewById(R.id.activity_info_client_name);
        this.client_address_tv      = (TextView) findViewById(R.id.activity_info_client_address);
        this.client_commentary_tv   = (TextView) findViewById(R.id.activity_info_client_commentary);

        this.mTelephoneListFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.activity_info_client_telephone_listfragment);

        //--------------------------------------------------------------
        setTextview();
        setmTelephoneListFragment();
    }

    private void setTextview() {
        String client_name          = client.getClient_name();
        String client_address       = client.getClient_address();
        String client_commentary    = client.getClient_commentary();

        this.client_name_tv         .setText(client_name);
        this.client_address_tv      .setText(client_address);
        this.client_commentary_tv   .setText(client_commentary);
    }

    private void setmTelephoneListFragment() {
        List<Telephone> telephones = this.client.getTelephoneList();

        for(Telephone telephone : telephones) {
            Long telephone_id       = telephone.getId();
            String telephone_number = telephone.getTelephone();
            String telephone_name   = telephone.getName();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(TELEPHONE_ID,       telephone_id);
            hashMap.put(TELEPHONE_NUMBER,   telephone_number);
            hashMap.put(TELEPHONE_NAME,     telephone_name);

            mTelephoneList.add(hashMap);
        }

        this.adapter = new SimpleAdapter(
                this,
                mTelephoneList,
                R.layout.activity_info_client_telephone_list_item,
                new String[] {
                        TELEPHONE_NUMBER,
                        TELEPHONE_NAME
                },
                new int[] {
                        R.id.activity_info_client_telephone_list_item_telephone,
                        R.id.activity_info_client_telephone_list_item_name
                }
        );

        mTelephoneListFragment.setListAdapter(adapter);

    }

}
