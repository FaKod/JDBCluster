/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.domain;

/**
 * Helper Class to store validation entries
 * @author Christopher Schmidt
 * 
 */
final class Valid {
	/*
	 * if this was a valid or invalid tag
	 */
	boolean valid;
	
	/*
	 * content of the value tag. Null if there was'nt one
	 */
	String value;
	
	/*
	 * content of the all tag. False if there was'nt one
	 */
	boolean all = false;
	
	/*
	 * content of the null tag. False if there was'nt one
	 */
	boolean nullValue = false;
}