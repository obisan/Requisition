package com.dubinets.requisition.activity.order;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.Order;
import com.dubinets.requisition.locale.Locale;

import java.text.SimpleDateFormat;

/**
 * Created by dubinets on 23.08.2016.
 */
public class OrderDetailedActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_order);
    }

    @Override
    protected void onStart() {
        super.onStart();

        long order_id = getIntent().getLongExtra("order_id", 1L);

        TextView client_name = (TextView) findViewById(R.id.order_detailed_client_name);
        TextView creation_date = (TextView) findViewById(R.id.order_detailed_creation_date);

        Order order = DatabaseHelper.getOrderDao().load(order_id);
        Client client = DatabaseHelper.getClientDao().load(order.getClient_id());

        client_name.setText(client.getClient_name());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE dd-MMMM", Locale.RUSSIAN);
        creation_date.setText(simpleDateFormat.format(order.getCreation_date()));

    }
}
