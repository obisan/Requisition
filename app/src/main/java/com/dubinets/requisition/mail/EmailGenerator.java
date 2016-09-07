package com.dubinets.requisition.mail;

import com.dubinets.requisition.databasehelper.DatabaseHelper;
import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.Count;
import com.dubinets.requisition.db.CrossItineraryClient;
import com.dubinets.requisition.db.DayOfWeek;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.Itinerary;
import com.dubinets.requisition.db.Spot;
import com.dubinets.requisition.db.Type;
import com.dubinets.requisition.db.Week;
import com.dubinets.requisition.locale.Locale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dubinets on 05.09.2016.
 */
public class EmailGenerator {
    public static String subject_clients(Long itinerary_id) {
        Itinerary itinerary = DatabaseHelper.getItineraryDao().load(itinerary_id);
        DayOfWeek dayOfWeek = DatabaseHelper.getDayOfWeekDao().load(itinerary.getDay_of_week_id());

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.RUSSIAN);

        StringBuilder subject = new StringBuilder();
        subject.append("Заявка за ");
        subject.append(sdf.format(date));
        subject.append(" (");
        subject.append(dayOfWeek.getDay_of_week());
        subject.append(")");

        return subject.toString();
    }

    public static String body_clients(Long itinerary_id) {
        Itinerary itinerary = DatabaseHelper.getItineraryDao().load(itinerary_id);
        List<CrossItineraryClient> crossItineraryClients = itinerary.getCrossItineraryClientList();
        List<Client> clients = new ArrayList<>();

        for(CrossItineraryClient crossItineraryClient : crossItineraryClients) {
            Client client = DatabaseHelper.getClientDao().load(crossItineraryClient.getClient_id());
            clients.add(client);
        }

        StringBuilder body = new StringBuilder();

        for(Client client : clients) {
            body.append(client.getClient_name());
            body.append("\t(");
            body.append(client.getClient_address());
            body.append(")\n");

            Long client_id = client.getId();
            List<Spot> spots = DatabaseHelper._queryClientItinerary_SpotList(itinerary_id, client_id);
            for(Spot spot : spots) {
                Item    item    = DatabaseHelper.getItemDao().load(spot.getItem_id());
                Count   count   = DatabaseHelper.getCountDao().load(spot.getCount_id());
                Type    type    = DatabaseHelper.getTypeDao().load(item.getType_id());

                body.append("\t\t");
                body.append(item.getItem_name());
                body.append("\t");
                body.append(count.getCount());
                body.append("\n");
            }


        }



        return body.toString();
    }

    public static String subject_itineraries(Long week_id) {
        Week week = DatabaseHelper.getWeekDao().load(week_id);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.RUSSIAN);

        StringBuilder subject = new StringBuilder();
        subject.append("Заявка за ");
        subject.append(sdf.format(date));
        subject.append(" (");
        subject.append(sdf.format(week.getDate_start_week()));
        subject.append(")");

        return subject.toString();
    }

    public static String body_itineraries(Long week_id) {
        Week week = DatabaseHelper.getWeekDao().load(week_id);
        List<Itinerary> itineraries = week.getItineraryList();

        SimpleDateFormat    sdf    = new SimpleDateFormat("dd-MM-yyyy", Locale.RUSSIAN);
        StringBuilder       body   = new StringBuilder();

        body.append("Неделя: ");
        body.append(sdf.format(week.getDate_start_week()));
        body.append("\n");

        for(Itinerary itinerary : itineraries) {
            Long itinerary_id = itinerary.getId();

            DayOfWeek dayOfWeek = DatabaseHelper.getDayOfWeekDao().load(itinerary.getDay_of_week_id());

            body.append("   ");
            body.append(dayOfWeek.getDay_of_week());
            body.append("\n");

            List<Client> clients = new ArrayList<>();
            for(CrossItineraryClient crossItineraryClient : DatabaseHelper.getCrossItineraryClientDao()
                    ._queryItinerary_CrossItineraryClientList(itinerary.getId())) {
                Client client = DatabaseHelper.getClientDao().load(crossItineraryClient.getClient_id());
                clients.add(client);
            }

            for(Client client : clients) {
                Long client_id = client.getId();

                body.append("       ");
                body.append(client.getClient_name());
                body.append("   (");
                body.append(client.getClient_address());
                body.append(")\n");

                List<Spot> spots = DatabaseHelper._queryClientItinerary_SpotList(itinerary_id, client_id);
                for(Spot spot : spots) {
                    Item    item    = DatabaseHelper.getItemDao()   .load(spot.getItem_id());
                    Count   count   = DatabaseHelper.getCountDao()  .load(spot.getCount_id());

                    body.append("           ");
                    body.append(item.getItem_name());
                    body.append("   ");
                    body.append(count.getCount());
                    body.append(" шт.\n");
                }
            }
        }


        return body.toString();
    }

    public static String subject_client(Long itinerary_id, Long client_id) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.RUSSIAN);
        Client client = DatabaseHelper.getClientDao().load(client_id);

        StringBuilder subject = new StringBuilder();
        subject.append("Заявка за ");
        subject.append(sdf.format(date));
        subject.append(" (");
        subject.append(client.getClient_name());
        subject.append(", ");
        subject.append(client.getClient_address());
        subject.append(")");

        return subject.toString();
    }

    public static String body_client(Long itinerary_id, Long client_id) {
        StringBuilder body = new StringBuilder();

        Client client = DatabaseHelper.getClientDao().load(client_id);
        body.append(client.getClient_name());
        body.append(" (");
        body.append(client.getClient_address());
        body.append(")\n");

        List<Spot> spots = DatabaseHelper._queryClientItinerary_SpotList(itinerary_id, client_id);
        for(Spot spot : spots) {
            Item  item      = DatabaseHelper.getItemDao().load(spot.getItem_id());
            Count count     = DatabaseHelper.getCountDao().load(spot.getCount_id());

            body.append("   ");
            body.append(item.getItem_name());
            body.append("   ");
            body.append(count.getCount());
            body.append(" шт.\n");

        }

        return body.toString();
    }

}
