/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.config;

import ch.qos.logback.classic.LoggerContext;
import com.google.common.base.Preconditions;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.ObjectUtils;

/**
 *
 * @author Guo-Guang Chiou created on 27/06/2017.
 */

public final class AppConfig {

  private static final AppConfigBuilder builder = new AppConfigBuilder();

  private AppConfig() {}

  /**
   * Returns a {@link org.springframework.context.ApplicationContext}
   *
   * @param paths if paths is <code>null</code> {@link org.springframework.context.annotation.AnnotationConfigApplicationContext}
   *    will be returned. Otherwise, {@link org.springframework.context.support.ClassPathXmlApplicationContext} will be returned.
   * @return {@link org.springframework.context.ApplicationContext}
   */
  public static ApplicationContext getApplicationContext(String paths) {
    if(paths == null) return builder.createApplicationContext();
    else return builder.createApplicationContext(paths);
  }

  public static void close(ApplicationContext context) {
    builder.close(context);
  }


  /**
   * The builder creates contexts inheriting {@link org.springframework.context.ApplicationContext}
   */
  static class AppConfigBuilder {

    private ConfigurableApplicationContext context = null;

    AppConfigBuilder() {}

    synchronized ApplicationContext createApplicationContext(String paths) {

      if(context != null) return context;

      context = createAppContext(paths);

      // scan default packages
      if(paths == null) {
        final String[] basePackages = {"com.kloudless.connector.hipchat.service"};
        scanBasePackages(context, basePackages);
      }

      // register a factory bean to create a Jackson's ObjectMapper
      context = register(context, JacksonObjectMapperFactoryBean.class);

      context.refresh();

      context.registerShutdownHook();

      // catch events
      context.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
        @Override
        public void onApplicationEvent(ApplicationEvent event) {
          LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
          loggerContext.stop();
        }
      });

      return context;
    }

    ApplicationContext createApplicationContext() {
      return createApplicationContext(null);
    }

    public synchronized void close(ApplicationContext context) {
      if(this.context != null && this.context.equals(context)) {
        this.context.close();
        this.context = null;
      }
    }

    private ConfigurableApplicationContext scanBasePackages(ConfigurableApplicationContext context,
        final String[] basePackages) {
      Preconditions.checkNotNull(context, "The context reference must not be null");
      Preconditions.checkNotNull(basePackages, "The packages must not be null");
      Preconditions.checkArgument(basePackages.length > 0,
          "At least one package path should be provided");
      if(context instanceof AnnotationConfigApplicationContext) {
        ((AnnotationConfigApplicationContext)context).scan(basePackages);
      }
      return context;
    }

    private ConfigurableApplicationContext register(ConfigurableApplicationContext context,
        Class<?> annotatedClass) {
      if(context instanceof AnnotationConfigApplicationContext &&
          !ObjectUtils.isEmpty(annotatedClass)) {
        ((AnnotationConfigApplicationContext)context).register(annotatedClass);
      }
      return context;
    }

    /* (non-Javadoc) - used for test purpose */
    private ConfigurableApplicationContext createAppContext(final String configPaths) {
      return (ObjectUtils.isEmpty(configPaths) ? new AnnotationConfigApplicationContext() :
          new ClassPathXmlApplicationContext(configPaths));
    }
  }
}
