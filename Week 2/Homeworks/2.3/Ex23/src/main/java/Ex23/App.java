package Ex23;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;

public class App {

  static final int numberOfStudents = 200;

  public static void main(String[] args) throws IOException {
    MyDatabase db = new MyDatabase();
    db.loadDatabase();
    MongoCollection<Document> grades = db.getGrades();

    for (int studentId = 0; studentId < numberOfStudents; studentId++) {

      double worstScore = grades
        .find(new Document("student_id", studentId))
        .projection(new Document("_id", 0).append("score", 1))
        .sort(new Document("score", 1))
        .iterator().next().getDouble("score");

      grades.deleteOne(new Document("student_id", studentId).append("score", worstScore));
    }

    System.out.println(
      "Type : db.grades.aggregate({'$group':{'_id':'$student_id', " +
        "'average':{$avg:'$score'}}},{'$sort':{'average':-1}}, " +
        "{'$limit':1})" + " to get an answer to homework 2.3");
  }
}
