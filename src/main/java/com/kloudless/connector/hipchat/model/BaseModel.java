/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.model;

/**
 * Serve the base model for this project
 *
 * @author Guo-Guang Chiou created on 26/06/2017.
 */
public abstract class BaseModel {

  @Override
  public abstract String toString();

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object obj);
}
