package com.mongodb;

import spark.Spark;

/**
 * Created by bbednarczyk on 09.08.15.
 */

public class HelloWorldSparkStyle {
  public static void main(String[] args) {
    Spark.get("/", (request, response) -> "Hello World from Spark!!\n");
    Spark.get("/test", (request, response) -> "This is a test page\n");
    Spark.get("/echo/:thing", (request, response) -> request.params(":thing"));
  }
}