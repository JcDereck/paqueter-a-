package com.mycompany.iniciodesesion;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static MongoDatabase database = mongoClient.getDatabase("empresa_paqueteria");

    public static MongoDatabase getDatabase() {
        return database;
    }
}
