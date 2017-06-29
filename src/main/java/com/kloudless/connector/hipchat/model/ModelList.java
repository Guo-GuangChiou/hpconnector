/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.model;

import java.util.List;

/**
 * A Based model returns a list of same type resources. 
 *
 * @author Guo-Guang Chiou created on 26/06/2017.
 */
public interface ModelList<T extends BaseModel> {

  List<T> getResult();

}
