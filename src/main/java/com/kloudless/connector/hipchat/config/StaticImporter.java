/*
 * Copyright (c) 2017 Kloudless Inc. All rights reserved.
 * Kloudless PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kloudless.connector.hipchat.config;

/**
 * Shorten the names of static class when accessing
 * <p/>
 * @author Guo-Guang Chiou created on 29/06/2017
 */
public final class StaticImporter {
	
	public static final PropertiesHelper Props = PropertiesHelper.getInst();

	private StaticImporter() {
	}
}
