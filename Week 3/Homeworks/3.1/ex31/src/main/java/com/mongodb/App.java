package com.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

public class App {

  public static void main(String[] args) throws IOException {

    MyDatabase db = new MyDatabase();
    db.loadDatabase();
    MongoCollection<Document> studentsCollection = db.getStudents();

    for (long studentId = 0; studentId < studentsCollection.count(); studentId++) {

      // Get student's record in DBs
      Document studentDbRecord = studentsCollection.find(new Document("_id", studentId)).iterator().next();
      ArrayList<Document> studentScoresArray =  (ArrayList<Document>) studentDbRecord.get("scores");

      // Find worst Score
      double worstHomeworkScore = Double.POSITIVE_INFINITY;

      for(Document studentScore : studentScoresArray ) {
        if(studentScore.getString("type").equals("homework")) {
          worstHomeworkScore = Double.min(worstHomeworkScore, studentScore.getDouble("score"));
        }
      }

      // Update student's grades
      Document findQuery = new Document("_id", studentId);
      Document updateQuery = new Document("$pull", new Document("scores", new Document("type", "homework").append("score", worstHomeworkScore)));
      studentsCollection.updateOne(findQuery, updateQuery);
    }

    // db.students.aggregate( { '$unwind' : '$scores' } , { '$group' : { '_id' : '$_id' , 'average' : { $avg : '$scores.score' } } } , { '$sort' : { 'average' : -1 } } , { '$limit' : 1 } )
    // Answer : 13
  }
}