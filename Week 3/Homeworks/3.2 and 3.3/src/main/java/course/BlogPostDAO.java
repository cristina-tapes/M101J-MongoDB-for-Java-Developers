package course;

import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonArray;
import org.bson.BsonDateTime;
import org.bson.BsonTimestamp;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
  MongoCollection<Document> postsCollection;

  public BlogPostDAO(final MongoDatabase blogDatabase) {
    postsCollection = blogDatabase.getCollection("posts");
  }

  // Return a single post corresponding to a permalink
  public Document findByPermalink(String permalink) {
    Document post = postsCollection.find(new Document("permalink", permalink)).first();
    return post;
  }

  // Return a list of posts in descending order. Limit determines
  // how many posts are returned.
  public List<Document> findByDateDescending(int limit) {

    // Return a list of DBObjects, each one a post from the posts collection

    List<Document> posts = new ArrayList<>();
    MongoCursor<Document> iterator = postsCollection.find().sort(new Document("date", -1)).limit(limit).iterator();

    while(iterator.hasNext()) {
      Document doc  = iterator.next();
      posts.add(doc);
    }

    return posts;
  }


  public String addPost(String title, String body, List tags, String username) {

    System.out.println("inserting blog entry " + title + " " + body);

    String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
    permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
    permalink = permalink.toLowerCase();


    // YYY HW 3.2, Work Here
    // Remember that a valid post has the following keys:
    // author, body, permalink, tags, comments, date
    //
    // A few hints:
    // - Don't forget to create an empty list of comments
    // - for the value of the date key, today's datetime is fine.
    // - tags are already in list form that implements suitable interface.
    // - we created the permalink for you above.

    // Build the post object and insert it
    Document post = new Document("title", title)
                    .append("author", username)
                    .append("body", body)
                    .append("permalink", permalink)
                    .append("tags", tags)
                    .append("comments", new ArrayList<>())
                    .append("date", new Date());

    postsCollection.insertOne(post);
    return permalink;
  }


  // White space to protect the innocent


  // Append a comment to a blog post
  public void addPostComment(final String name, final String email, final String body,
                             final String permalink) {

    // YYY HW 3.3, Work Here
    // Hints:
    // - email is optional and may come in NULL. Check for that.
    // - best solution uses an update command to the database and a suitable
    //   operator to append the comment on to any existing list of comments

    Document comment = new Document("author", name).append("body", body);
    if(email != null) comment.append("email", email);

    postsCollection.updateOne(new Document("permalink",  permalink),
      new Document("$push", new Document("comments", comment)));
  }
}
