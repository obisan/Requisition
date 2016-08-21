-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2016-08-20 13:07:27.408

-- tables
-- Table: Client
CREATE TABLE Client (
    id integer NOT NULL CONSTRAINT Client_pk PRIMARY KEY,
    client_name varchar(256) NOT NULL,
    client_address varchar(256)
);

-- Table: Count
CREATE TABLE Count (
    id integer NOT NULL CONSTRAINT Count_pk PRIMARY KEY,
    count integer NOT NULL
);

-- Table: Item
CREATE TABLE Item (
    id integer NOT NULL CONSTRAINT Item_pk PRIMARY KEY,
    item_name varchar(256) NOT NULL,
    type_id integer NOT NULL,
    shelftime_id integer NOT NULL,
    CONSTRAINT Item_Type FOREIGN KEY (type_id)
    REFERENCES Type (id),
    CONSTRAINT Item_Shelftime FOREIGN KEY (shelftime_id)
    REFERENCES Shelftime (id)
);

-- Table: Order
CREATE TABLE "Order" (
    id integer NOT NULL CONSTRAINT Order_pk PRIMARY KEY,
    creation_date datetime NOT NULL,
	client_id integer NOT NULL,
    CONSTRAINT Order_Client FOREIGN KEY (client_id)
    REFERENCES Client (id)
);

-- Table: Photo
CREATE TABLE Photo (
    id integer NOT NULL CONSTRAINT Photo_pk PRIMARY KEY,
    photo varchar(256),
    item_id integer NOT NULL,
    CONSTRAINT Photo_Item FOREIGN KEY (item_id)
    REFERENCES Item (id)
);

-- Table: Shelftime
CREATE TABLE Shelftime (
    id integer NOT NULL CONSTRAINT Shelftime_pk PRIMARY KEY,
    shelftime integer NOT NULL
);

-- Table: Spot
CREATE TABLE Spot (
    id integer NOT NULL CONSTRAINT Spot_pk PRIMARY KEY,
    order_id integer NOT NULL,
    item_id integer NOT NULL,
    count_id integer,
    CONSTRAINT Spot_Count FOREIGN KEY (count_id)
    REFERENCES Count (id),
    CONSTRAINT Spot_Order FOREIGN KEY (order_id)
    REFERENCES "Order" (id),
    CONSTRAINT Spot_Item FOREIGN KEY (item_id)
    REFERENCES Item (id)
);

-- Table: Telephone
CREATE TABLE Telephone (
    id integer NOT NULL CONSTRAINT Telephone_pk PRIMARY KEY,
    telephone varchar(16) NOT NULL,
    context varchar(256),
    client_id integer NOT NULL,
    CONSTRAINT Telephone_Client FOREIGN KEY (client_id)
    REFERENCES Client (id)
);

-- Table: Type
CREATE TABLE Type (
    id integer NOT NULL CONSTRAINT Type_pk PRIMARY KEY,
    type_name varchar(256) NOT NULL
);

-- End of file.

