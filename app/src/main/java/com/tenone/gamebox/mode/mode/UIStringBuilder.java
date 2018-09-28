/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * A modifiable {@link CharSequence sequence of characters} for use in creating
 * strings. This class is intended as a direct replacement of
 * {@link StringBuffer} for non-concurrent use; unlike {@code StringBuffer} this
 * class is not synchronized.
 * 
 * <p>
 * For particularly complex string-building needs, consider
 * {@link java.util.Formatter}.
 * 
 * <p>
 * The majority of the modification methods on this class return {@code this} so
 * that method calls can be chained together. For example:
 * {@code new StringBuilder("a").append("b").append("c").toString()}.
 * 
 * @see CharSequence
 * @see Appendable
 * @see StringBuffer
 * @see String
 * @see String#format
 * @since 1.5
 */
public final class UIStringBuilder implements Serializable {

	private static final long serialVersionUID = -2239270427050857207L;

	public static final String NUKK_REPLACE_TYPE = "null";

	public static final String EMPITY_REPLACE_TYPE = "";

	private StringBuilder builder = new StringBuilder();

	private String nullReplaced;

	public UIStringBuilder() {
		nullReplaced = EMPITY_REPLACE_TYPE;
	}

	public UIStringBuilder append(CharSequence str) {
		if (str == null) {
			builder.append(nullReplaced);
			return this;
		}

		builder.append(str);
		return this;
	}
	
	
	public void setNullReplaceStr(String replaceStr){
		if(replaceStr == null){
			replaceStr = EMPITY_REPLACE_TYPE;
		}
		this.nullReplaced = replaceStr;
		
	}
	
	
	public String toString(){
		return builder.toString();
	}
}
