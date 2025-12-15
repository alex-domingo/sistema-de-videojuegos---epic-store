package com.epicstore.epicstore;

import com.epicstore.epicstore.util.DBConnection;

public class EpicStore {

    public static void main(String[] args) {

        System.out.println("Hello World, conectando a nuestra DB :)");

        DBConnection connection = new DBConnection();
        connection.connect();

    }

}
