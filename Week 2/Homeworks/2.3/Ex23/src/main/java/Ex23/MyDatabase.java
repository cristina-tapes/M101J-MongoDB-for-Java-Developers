package Ex23;

import com.google.common.collect.Sets;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.Getter;

/* My wrapper to mongo-db */
public class MyDatabase {

  MongoClientOptions options;
  MongoClient client;
  MongoDatabase db;
  @Getter MongoCollection<Document> grades;

  void loadDatabase() throws IOException {

    if (Sets.newHashSet(db.listCollectionNames()).contains("grades")) {
      db.getCollection("grades").drop();
    }

    grades = db.getCollection("grades");

    for (String jsonContent : Files.readAllLines(Paths.get("src/main/java/Ex23/grades.json"))) {
      grades.insertOne(Document.parse(jsonContent));
    }
  }

  public MyDatabase() {
    options = MongoClientOptions.builder().connectionsPerHost(1000).build();
    client = new MongoClient(new ServerAddress("127.0.0.1:27017"), options);
    db = client.getDatabase("students");
  }
}
