-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2016-08-20 13:07:27.408

-- tables
-- Table: adnroid-metadata
CREATE TABLE "android_metadata"(
    "locale" TEXT DEFAULT 'en_US'
);
INSERT INTO "android_metadata" VALUES ('en_US');

-- Table: Client
CREATE TABLE Client (
    _id integer NOT NULL CONSTRAINT Client_pk PRIMARY KEY,
    client_name varchar(256) NOT NULL,
    client_address varchar(256)
);

-- Table: Count
CREATE TABLE Count (
    _id integer NOT NULL CONSTRAINT Count_pk PRIMARY KEY,
    count integer NOT NULL
);

-- Table: Item
CREATE TABLE Item (
    _id integer NOT NULL CONSTRAINT Item_pk PRIMARY KEY,
    item_name varchar(256) NOT NULL,
    type_id integer NOT NULL,
    shelftime_id integer NOT NULL,
    CONSTRAINT Item_Type FOREIGN KEY (type_id)
    REFERENCES Type (_id),
    CONSTRAINT Item_Shelftime FOREIGN KEY (shelftime_id)
    REFERENCES Shelftime (_id)
);

-- Table: Order
CREATE TABLE "Order" (
    _id integer NOT NULL CONSTRAINT Order_pk PRIMARY KEY,
    creation_date datetime NOT NULL,
	client_id integer NOT NULL,
    CONSTRAINT Order_Client FOREIGN KEY (client_id)
    REFERENCES Client (_id)
);

-- Table: Photo
CREATE TABLE Photo (
    _id integer NOT NULL CONSTRAINT Photo_pk PRIMARY KEY,
    photo varchar(256),
    item_id integer NOT NULL,
    CONSTRAINT Photo_Item FOREIGN KEY (item_id)
    REFERENCES Item (_id)
);

-- Table: Shelftime
CREATE TABLE Shelftime (
    _id integer NOT NULL CONSTRAINT Shelftime_pk PRIMARY KEY,
    shelftime integer NOT NULL
);

-- Table: Spot
CREATE TABLE Spot (
    _id integer NOT NULL CONSTRAINT Spot_pk PRIMARY KEY,
    order_id integer NOT NULL,
    item_id integer NOT NULL,
    count_id integer,
    CONSTRAINT Spot_Count FOREIGN KEY (count_id)
    REFERENCES Count (_id),
    CONSTRAINT Spot_Order FOREIGN KEY (order_id)
    REFERENCES "Order" (_id),
    CONSTRAINT Spot_Item FOREIGN KEY (item_id)
    REFERENCES Item (_id)
);

-- Table: Telephone
CREATE TABLE Telephone (
    _id integer NOT NULL CONSTRAINT Telephone_pk PRIMARY KEY,
    telephone varchar(16) NOT NULL,
    context varchar(256),
    client_id integer NOT NULL,
    CONSTRAINT Telephone_Client FOREIGN KEY (client_id)
    REFERENCES Client (_id)
);

-- Table: Type
CREATE TABLE Type (
    _id integer NOT NULL CONSTRAINT Type_pk PRIMARY KEY,
    type_name varchar(256) NOT NULL
);

-- End of file.

