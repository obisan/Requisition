package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.databasehelper.DatabaseHelper;

import java.util.HashMap;

/**
 * Created by dubinets on 24.08.2016.
 */
public class SpotFragment extends ListFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(getListView());
    }


}
