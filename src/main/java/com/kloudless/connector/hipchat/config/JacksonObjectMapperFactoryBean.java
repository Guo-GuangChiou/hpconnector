/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

/**
 * @author Guo-Guang Chiou created on 27/06/2017.
 */
@Configurable
public class JacksonObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

  @Override
  public ObjectMapper getObject() throws Exception {
    Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
    bean.setIndentOutput(false);
    bean.setSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    bean.afterPropertiesSet();
    ObjectMapper objectMapper = bean.getObject();
    objectMapper.registerModule(new JodaModule());
    return objectMapper;
  }

  @Override
  public Class<?> getObjectType() {
    return ObjectMapper.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
