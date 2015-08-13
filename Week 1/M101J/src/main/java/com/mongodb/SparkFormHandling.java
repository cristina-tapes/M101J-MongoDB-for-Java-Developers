package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bbednarczyk on 13.08.15.
 */
public class SparkFormHandling {

  public static void main(String[] args) {

    // Configure Freemarker
    final Configuration configuration = new Configuration();
    configuration.setClassForTemplateLoading(SparkFormHandling.class, "/");

    // Configure routes
    Spark.get("/", (request,response) -> {
      Map<String, Object> fruitsMap = new HashMap<>();
      fruitsMap.put("fruits", Arrays.asList("apple", "orange", "banana", "peach"));
      Template fruitPickerTemplate = configuration.getTemplate("fruitPicker.ftl");
      StringWriter writer = new StringWriter();
      fruitPickerTemplate.process(fruitsMap, writer);
      return writer;
    });

    Spark.post("/favorite_fruit", (request, response) -> {
      final String fruit = request.queryParams("fruit");
      return fruit == null ? "Why don't you pick one?" : "Your favorite fruit is " + fruit;
    });

  }

}