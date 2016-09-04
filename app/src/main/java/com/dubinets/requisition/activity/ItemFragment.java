package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.adapter.ItemTmp;


/**
 * Created by dubinets on 29.08.2016.
 */
public class ItemFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        v.setClickable(true);

        LinearLayout layout     = (LinearLayout)v.findViewById(R.id.list_item_item_main_layout);
        FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.list_item_item_framelayout_numbers);

        ItemTmp itemTmp = (ItemTmp) getListAdapter().getItem(position);
        itemTmp.setVisible( (itemTmp.isVisible()) ? false : true );


        if(itemTmp.isVisible()) {
            layout.setBackgroundColor( getResources().getColor(R.color.background) );
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            layout.setBackgroundColor( getResources().getColor(R.color.white) );
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }
}