package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * Created by bbednarczyk on 09.08.15.
 */
public class HelloWorldSparkFreemarkerStyle {

  public static void main(String[] args) {

    // Free-marker documentation

    final Configuration configuration = new Configuration();
    configuration.setClassForTemplateLoading(HelloWorldSparkFreemarkerStyle.class, "/");

    Spark.get("/", (request, response) -> {

      StringWriter writer = new StringWriter();

      try {
        Template helloTemplate = configuration.getTemplate("hello.ftl");
        Map<String, Object> helloMap = new HashMap<String, Object>();
        helloMap.put("name", "Freemarker");
        helloTemplate.process(helloMap, writer);

      } catch (Exception e) {
        halt(500);
        e.printStackTrace();
      }

      return writer;
    });
  }
}
