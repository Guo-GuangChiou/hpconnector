/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link AppConfig}
 *
 * @author Guo-Guang Chiou created on 27/06/2017.
 */
public class AppConfigTest {

  private final String BEAN_FILE_NAME = "bean_file_name";

  private static final Logger L = LoggerFactory.getLogger(AppConfigTest.class);

  ApplicationContext context = null;
  ObjectMapper objectMapper = null;

  @BeforeClass
  public void beforeClass() {
    context = AppConfig.getApplicationContext(null);
    L.info("ApplicationContext created", context);
    objectMapper = context.getBean(ObjectMapper.class);
    L.info("ObjectMapper created", objectMapper);
  }

  @Test
  public void testCreateObjectMapper() throws JsonProcessingException {
    Map<String,String> map = new HashMap<>();
    map.put("name","kloudless");
    final String expected = "{\"name\":\"kloudless\"}";
    String actual = objectMapper.writeValueAsString(map);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(threadPoolSize = 10, invocationCount = 10)
  public void testCreateObjectMapperConcurrently() {
    // make sure one and only one MapperObject created
    ObjectMapper actual = context.getBean(ObjectMapper.class);
    // should point to the same object
    assertThat(objectMapper == actual);
  }

  @Test(threadPoolSize = 10, invocationCount = 10)
  public void testCreateAnnotatedApplicationContextConcurrently() {
    // make sure one and only one ApplicationContext object created
    ApplicationContext actual = AppConfig.getApplicationContext(null);
    // should point to the same object
    assertThat(context == actual);
  }

  @Test(dependsOnMethods = {"testCreateAnnotatedApplicationContextConcurrently",
      "testCreateObjectMapperConcurrently"})
  public void testCreateClassPathXmlApplicationContext() {
    if(context != null) {
      AppConfig.close(context);
      final String path = StaticImporter.Props.getProperty(BEAN_FILE_NAME);
      context = AppConfig.getApplicationContext(path);
      assertThat(context).isNotNull();
      assertThat(context).isInstanceOf(ClassPathXmlApplicationContext.class);
      if(objectMapper != null) {
        objectMapper = null;
      }
      objectMapper = context.getBean(ObjectMapper.class);
      assertThat(objectMapper).isNotNull();
    }
  }
}
