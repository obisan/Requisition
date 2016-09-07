package com.dubinets.requisition.activity;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dubinets.requisition.R;
import com.dubinets.requisition.activity.navigation.ClientActivity;
import com.dubinets.requisition.activity.navigation.ItemActivity;
import com.dubinets.requisition.activity.navigation.TypeActivity;
import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.CrossItineraryClient;
import com.dubinets.requisition.db.Itinerary;
import com.dubinets.requisition.db.Week;
import com.dubinets.requisition.locale.Locale;
import com.dubinets.requisition.mail.EmailGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {
    private final String ITINERARY_ID   = "ITINERARY_ID";
    private final String ITINERARY_NAME = "ITINERARY_NAME";
    private final String NUMBER_CLIENTS = "NUMBER_CLIENTS";
    private final String DAY_OF_WEEK    = "DAY_OF_WEEK";

    private ArrayList<HashMap<String, Object>> mItineraryList = new ArrayList<>();

    private int current_week;
    private List<Week> weeks;
    private final static SimpleDateFormat SDF = new SimpleDateFormat("dd / MMMM / yyyy", Locale.RUSSIAN);

    private ListFragment mListFragment;
    private SimpleAdapter mAdapter;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapterDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mListFragment  = (ListFragment) getFragmentManager().findFragmentById(R.id.activity_main_listfragment_itineraries);
        this.mDrawerList    = (ListView) findViewById(R.id.activity_main_navigation_list);

        this.current_week   = 0;
        this.weeks          = DatabaseHelper.loadAllWeeksSorted();
        this.weeks          = isWeeksEmpty(weeks);


        Week week       = weeks.get(current_week);
        Long week_id    = week.getId();
        getIntent().putExtra("week_id", week_id);

        /////////////////----------------------------------
        setDrawerList();
        setViews(current_week);
        setListFragment(current_week);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViews(current_week);
        setListFragment(current_week);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mail_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_mail_email:
                sendEmail();
                return true;

            case R.id.menu_mail_add:
                addWeek();
                return true;

            case R.id.menu_mail_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickButtonPrev(View v) {
        if (current_week == weeks.size() - 1) return;

        current_week++;
        Toast.makeText(this, "current week = " + current_week, Toast.LENGTH_SHORT).show();

        getIntent().putExtra("week_id", weeks.get(current_week).getId());
        update(current_week);
    }

    public void onClickButtonNext(View v) {
        if (current_week == 0) return;

        current_week--;
        Toast.makeText(this, "current week = " + current_week, Toast.LENGTH_SHORT).show();

        getIntent().putExtra("week_id", weeks.get(current_week).getId());
        update(current_week);
    }

    public void update(int current_week) {
        setViews(current_week);
        setListFragment(current_week);
    }

    private void setDrawerList() {
        String[] osArray = getResources().getStringArray(R.array.navigation_menu);
        mAdapterDrawer = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapterDrawer);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    public void setViews(int current_week) {
        Week week = weeks.get(current_week);
        TextView tv = (TextView) findViewById(R.id.activity_main_txv_week);
        tv.setText(SDF.format(week.getDate_start_week()));
    }

    public void setListFragment(int current_week) {
        long week_id = weeks.get(current_week).getId();

        if (!mItineraryList.isEmpty()) mItineraryList.clear();

        List<Itinerary> itineraries = DatabaseHelper.getItineraryDao()._queryWeek_ItineraryList(week_id);
        for (Itinerary itinerary : itineraries) {
            Long itinerary_id = itinerary.getId();
            String itinerary_name = itinerary.getItinerary_name();
            Integer number_client = DatabaseHelper
                    .getCrossItineraryClientDao()
                    ._queryItinerary_CrossItineraryClientList(itinerary_id).size();
            String day_of_week = DatabaseHelper
                    .getDayOfWeekDao()
                    .load(itinerary.getDay_of_week_id())
                    .getDay_of_week();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(ITINERARY_ID,       itinerary_id);
            hashMap.put(ITINERARY_NAME,     itinerary_name);
            hashMap.put(NUMBER_CLIENTS,     "Количество точек: " + number_client);
            hashMap.put(DAY_OF_WEEK,        day_of_week);

            mItineraryList.add(hashMap);

        }

        this.mAdapter = new SimpleAdapter(
                this,
                mItineraryList,
                R.layout.activity_list_itineraries_item,
                new String[]{
                        NUMBER_CLIENTS,
                        DAY_OF_WEEK
                },
                new int[]{
                        R.id.list_itinerary_item_number_clients,
                        R.id.list_itinerary_item_text_day_of_week
                }
        );

        this.mListFragment.setListAdapter(mAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch (position) {
                // Itinerary
                case 0:
                    intent = new Intent(getBaseContext(), ItineraryActivity.class);
                    startActivity(intent);
                    break;
                // Client
                case 1:
                    intent = new Intent(getBaseContext(), ClientActivity.class);
                    startActivity(intent);
                    break;
                // Item
                case 2:
                    intent = new Intent(getBaseContext(), ItemActivity.class);
                    startActivity(intent);
                    break;
                // Types
                case 3:
                    intent = new Intent(getBaseContext(), TypeActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    }

    private List<Week> isWeeksEmpty(List<Week> weeks) {
        if(weeks.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear(Calendar.MINUTE);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MILLISECOND);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

            Week week = new Week(null, calendar.getTime());

            DatabaseHelper.getWeekDao().insert(week);
        }

        return weeks;
    }

    private void addWeek() {
        Week week           = DatabaseHelper.getWeekDao().load(this.weeks.get(0).getId());
        Calendar calendar   = Calendar.getInstance();
        calendar.setTime(week.getDate_start_week());
        calendar.add(Calendar.DAY_OF_YEAR, 7);

        Week newWeek        = new Week(null, calendar.getTime());
        DatabaseHelper.getWeekDao().insert(newWeek);

        List<Itinerary> itineraries = DatabaseHelper.getWeekDao().load(this.weeks.get(0).getId()).getItineraryList();
        for(Itinerary itinerary : itineraries) {
            List<CrossItineraryClient> crossItineraryClients
                    = DatabaseHelper.getCrossItineraryClientDao()
                    ._queryItinerary_CrossItineraryClientList(itinerary.getId());

            itinerary.setId(null);
            itinerary.setWeek_id(newWeek.getId());

            Long rowId = DatabaseHelper.getItineraryDao().insert(itinerary);
            itinerary = DatabaseHelper.getItineraryDao().loadByRowId(rowId);

            for(CrossItineraryClient crossItineraryClient : crossItineraryClients) {
                crossItineraryClient.setId(null);
                crossItineraryClient.setItinerary_id(itinerary.getId());
                DatabaseHelper.getCrossItineraryClientDao().insert(crossItineraryClient);
            }

        }

        Toast.makeText(this, "Новая неделя добавлена", Toast.LENGTH_SHORT).show();

        this.weeks = DatabaseHelper.loadAllWeeksSorted();

        setViews(0);
        setListFragment(0);

    }

    private void sendEmail() {
        String subject      = EmailGenerator.subject_itineraries(weeks.get(current_week).getId());
        String body         = EmailGenerator.body_itineraries   (weeks.get(current_week).getId());
        String recipients   = PreferenceManager.getDefaultSharedPreferences(this).getString("emails", "");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,     new String[] {recipients} );
        intent.putExtra(Intent.EXTRA_SUBJECT,   subject );
        intent.putExtra(Intent.EXTRA_TEXT,      body );

        try {
            startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
