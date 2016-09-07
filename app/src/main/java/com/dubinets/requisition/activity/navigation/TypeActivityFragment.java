package com.dubinets.requisition.activity.navigation;

import android.app.ListFragment;
import android.os.Bundle;

/**
 * Created by dubinets on 07.09.2016.
 */
public class TypeActivityFragment extends ListFragment {
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
