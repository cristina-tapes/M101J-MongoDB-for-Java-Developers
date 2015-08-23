package com.mongodb;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/* My wrapper to mongo-db */
public class MyDatabase {

  private MongoClientOptions options;
  private MongoClient client;
  private MongoDatabase db;
  @Getter MongoCollection<Document> students;

  void loadDatabase() throws IOException {

    if (Sets.newHashSet(db.listCollectionNames()).contains("students")) {
      db.getCollection("students").drop();
    }

    students = db.getCollection("students");

    for (String jsonContent : Files.readAllLines(Paths.get("src/main/java/com/mongodb/students.json"))) {
      students.insertOne(Document.parse(jsonContent));
    }
  }

  public MyDatabase() {
    options = MongoClientOptions.builder().connectionsPerHost(1000).build();
    client = new MongoClient(new ServerAddress("127.0.0.1:27017"), options);
    db = client.getDatabase("schools");
  }
}
