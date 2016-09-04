package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator {

    private static final Integer DATABASE_VERSION = 15;
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(DATABASE_VERSION, "com.dubinets.requisition.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity client                   = addClient(schema);
        Entity itinerary                = addItinerary(schema);
        Entity cross_itinerary_client   = addCross_Itinerary_Client(schema);
        Entity telephone                = addTelephone(schema);
        Entity shelftime                = addShelftime(schema);
        Entity type                     = addType(schema);
        Entity item                     = addItem(schema);
        Entity photo                    = addPhoto(schema);
        Entity count                    = addCount(schema);
        Entity spot                     = addSpot(schema);
        Entity week                     = addWeek(schema);
        Entity dayofweek                = addDay_of_week(schema);

        week.addToMany(itinerary,        itinerary.addLongProperty("week_id").notNull().getProperty());
        dayofweek.addToMany(itinerary,   itinerary.addLongProperty("day_of_week_id").notNull().getProperty());

        itinerary.addToMany(cross_itinerary_client, cross_itinerary_client.addLongProperty("itinerary_id").notNull().getProperty());

        client.addToMany(telephone, telephone.addLongProperty("client_id").notNull().getProperty());
        client.addToMany(cross_itinerary_client, cross_itinerary_client.addLongProperty("client_id").notNull().getProperty());


        shelftime.addToMany(item,   item.addLongProperty("shelftime_id").getProperty());
        type.addToMany(item,        item.addLongProperty("type_id").notNull().getProperty());

        item.addToMany(photo,       photo.addLongProperty("item_id").notNull().getProperty());

        client.addToMany(spot,      spot.addLongProperty("client_id").notNull().getProperty());
        item.addToMany(spot,        spot.addLongProperty("item_id").notNull().getProperty());
        count.addToMany(spot,       spot.addLongProperty("count_id").notNull().getProperty());
        itinerary.addToMany(spot,   spot.addLongProperty("itinerary_id").notNull().getProperty());

    }

    private static Entity addWeek(final Schema schema) {
        Entity week = schema.addEntity("Week");
        week.addIdProperty().primaryKey().autoincrement();
        week.addDateProperty("date_start_week");

        return week;
    }

    private static Entity addDay_of_week(final Schema schema) {
        Entity day_of_week = schema.addEntity("DayOfWeek");
        day_of_week.addIdProperty().primaryKey().autoincrement();
        day_of_week.addStringProperty("day_of_week");

        return day_of_week;
    }

    private static Entity addClient(final Schema schema) {
        Entity user = schema.addEntity("Client");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("client_name").notNull();
        user.addStringProperty("client_address");
        user.addStringProperty("client_commentary");

        return user;
    }

    private static Entity addItinerary(final Schema schema) {
        Entity itinerary = schema.addEntity("Itinerary");
        itinerary.addIdProperty().primaryKey().autoincrement();
        itinerary.addStringProperty("itinerary_name").notNull();

        return itinerary;
    }

    private static Entity addCross_Itinerary_Client(final Schema schema) {
        Entity cross_itinerary_client = schema.addEntity("CrossItineraryClient");
        cross_itinerary_client.addIdProperty().primaryKey().autoincrement();

        return cross_itinerary_client;
    }

    private static Entity addTelephone(final Schema schema) {
        Entity telephone = schema.addEntity("Telephone");
        telephone.addIdProperty().primaryKey().autoincrement();
        telephone.addStringProperty("telephone").notNull();
        telephone.addStringProperty("name");

        return telephone;
    }

    private static Entity addShelftime(final Schema schema) {
        Entity shelftime = schema.addEntity("Shelftime");
        shelftime.addIdProperty().primaryKey().autoincrement();
        shelftime.addIntProperty("shelftime");

        return shelftime;
    }

    private static Entity addType(final Schema schema) {
        Entity type = schema.addEntity("Type");
        type.addIdProperty().primaryKey().autoincrement();
        type.addStringProperty("type_name");

        return type;
    }

    private static Entity addItem(final Schema schema) {
        Entity item = schema.addEntity("Item");
        item.addIdProperty().primaryKey().autoincrement();
        item.addStringProperty("item_name");
        item.addDoubleProperty("item_price");

        return item;
    }

    private static Entity addPhoto(final Schema schema) {
        Entity photo = schema.addEntity("Photo");
        photo.addIdProperty().primaryKey().autoincrement();
        photo.addStringProperty("photo");

        return photo;
    }

    private static Entity addCount(final Schema schema) {
        Entity count = schema.addEntity("Count");
        count.addIdProperty().primaryKey().autoincrement();
        count.addIntProperty("count");

        return count;
    }

    private static Entity addSpot(final Schema schema) {
        Entity spot = schema.addEntity("Spot");
        spot.addIdProperty().primaryKey().autoincrement();

        return spot;
    }

}
