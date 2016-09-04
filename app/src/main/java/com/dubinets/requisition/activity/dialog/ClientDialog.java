package com.dubinets.requisition.activity.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;

/**
 * Created by dubinets on 02.09.2016.
 */
public class ClientDialog extends AppCompatActivity {

    private EditText client_name_et;
    private EditText client_address_et;
    private EditText client_commentary_et;

    private final long NULL = 0L;

    private Long    client_id;
    private Client  client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dialog_client);

        this.client_name_et         = (EditText) findViewById(R.id.activity_navigation_dialog_client_name);
        this.client_address_et      = (EditText) findViewById(R.id.activity_navigation_dialog_client_address);
        this.client_commentary_et   = (EditText) findViewById(R.id.activity_navigation_dialog_client_commentary);

        this.client_id = getIntent().getLongExtra("client_id", NULL);
        this.client = getClientById(client_id);

        setEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_button:
                setClientDB();

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Client getClientById(Long id) {
        Client client = null;
        if(id != NULL) {
            client = DatabaseHelper.getClientDao().load(client_id);
        } else {
            client = new Client(null);
        }

        return client;
    }

    private void setEditText() {
        if(client.getId() != null) {
            this.client_name_et         .setText(client.getClient_name());
            this.client_address_et      .setText(client.getClient_address());
            this.client_commentary_et   .setText(client.getClient_commentary());
        }
    }

    private void setClientDB() {
        String client_name          = client_name_et        .getText().toString();
        String client_address       = client_address_et     .getText().toString();
        String client_commentary    = client_commentary_et  .getText().toString();

        this.client.setClient_name(client_name);
        this.client.setClient_address(client_address);
        this.client.setClient_commentary(client_commentary);

        synchronized (this) {
            if(client.getId() == null) {
                DatabaseHelper.getClientDao().insert(client);
            } else {
                DatabaseHelper.getClientDao().update(client);
            }
        }
    }
}
