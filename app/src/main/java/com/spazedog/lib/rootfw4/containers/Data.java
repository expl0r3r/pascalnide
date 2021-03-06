/*
 * This file is part of the RootFW Project: https://github.com/spazedog/rootfw
 *  
 * Copyright (c) 2015 Daniel Bergløv
 *
 * RootFW is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * RootFW is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with RootFW. If not, see <http://www.gnu.org/licenses/>
 */

package com.spazedog.lib.rootfw4.containers;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This container is used to store any kind of text. All of the text is located within a String Array, where each index is considered a line.
 */
@SuppressWarnings("unchecked")
public class Data<DATATYPE extends Data<DATATYPE>> extends BasicContainer {
	protected String[] mLines;
	
	/**
	 * This interface is used as the argument for the <code>sort()</code> and <code>assort()</code> methods. It can be used to create custom sorting of the text array.
	 */
	public interface DataSorting {
		/**
		 * The method which checks the line and determines whether or not it should be added or removed from the array. 
		 * <br />
		 * Note that the <code>sort()</code> method will remove the line upon false, whereas <code>assort()</code> will remove it upon true.
		 * 
		 * @param input
		 *     One line of the text array
		 */
        Boolean test(String input);
	}
	
	/**
	 * This interface is used as the argument for the <code>replace()</code> method. It can be used to replace or alter lines in the output.
	 */
	public interface DataReplace {
		/**
         * This method is used to alter the lines in the text array. Each lineInfo is parsed to this method, and whatever is returned will replace the current lineInfo.
         *
		 * @param input
		 *     One line of the text array
		 */
        String replace(String input);
	}
	
	/**
	 * Create a new Data instance.
	 * 
	 * @param lines
	 *     An array representing the lines of the text
	 */
	public Data(String[] lines) {
		mLines = lines;
	}
	
	/**
     * This can be used to replace part of a lineInfo, or the whole lineInfo. It uses the replace() method in the DataSorting interface where custom replacement of a lineInfo can be done. It parses the original lineInfo as an argument, and requires the new lineInfo to be returned.
     *
	 * @return
	 *     This instance
	 */
	public DATATYPE replace(DataReplace dataReplace) {
		if (size() > 0) {
			List<String> list = new ArrayList<>();

			for (String mLine : mLines) {
				list.add(dataReplace.replace(mLine));
			}
		}
		
		return (DATATYPE) this;
	}
	
	/**
	 * This can be used to replace whole lines based on a contained pattern. 
	 * 
	 * @param contains
	 *     The pattern which the line should contain
	 *     
	 * @param newLine
	 *     The new line that should be used as a replacement
	 *     
	 * @return
	 *     This instance
	 */
	public DATATYPE replace(final String contains, final String newLine) {
		return replace(new DataReplace() {
			@Override
			public String replace(String input) {
				return input != null && input.contains(contains) ? newLine : input;
			}
		});
	}
	
	/**
	 * This is used to determine whether or not to remove lines from the text array. Each line will be parsed to the custom <code>DataSorting</code> instance and then removed upon a true return.
	 * 
	 * @return
	 *     This instance
	 */
	public DATATYPE assort(DataSorting test) {
		if (size() > 0) {
			List<String> list = new ArrayList<>();

			for (String mLine : mLines) {
				if (!test.test(mLine)) {
					list.add(mLine);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return (DATATYPE) this;
	}
	
	/**
     * This is used to determine whether or not to remove lines from the text array. Each lineInfo will be compared to the argument. If the lineInfo contains anything from the argument, it will be removed from the text array.
     *
	 * @param contains
     *     A string to locate within each lineInfo to determine whether or not to remove the lineInfo
     *
	 * @return
	 *     This instance
	 */
	public DATATYPE assort(final String contains) {
		return assort(new DataSorting() {
			@Override
			public Boolean test(String input) {
				return input.contains( contains );
			}
		});
	}
	
	/**
	 * This is used to determine whether or not to keep lines in the text array. Each line will be parsed to the custom <code>DataSorting</code> instance and then removed upon a false return.
	 * 
	 * @return
	 *     This instance
	 */
	public DATATYPE sort(DataSorting test) {
		if (size() > 0) {
			List<String> list = new ArrayList<>();

			for (String mLine : mLines) {
				if (test.test(mLine)) {
					list.add(mLine);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return (DATATYPE) this;
	}
	
	/**
     * This is used to determine whether or not to keep lines in the text array. Each lineInfo will be compared to the argument. If the lineInfo contains anything from the argument, it will not be removed from the text array.
     *
	 * @param contains
     *     A string to locate within each lineInfo to determine whether or not to remove the lineInfo
     *
	 * @return
	 *     This instance
	 */
	public DATATYPE sort(final String contains) {
		return sort(new DataSorting() {
			public Boolean test(String input) {
				return input.contains( contains );
			}
		});
	}
	
	/**
	 * @see Data#sort(Integer, Integer)
	 */
	public DATATYPE sort(Integer start) {
		return sort(start, mLines.length);
	}
	
	/**
	 * This is used to determine whether or not to keep lines in the text array. The method will keep each index within the <code>start</code> and <code>stop</code> indexes parsed via the arguments.
	 * <br />
	 * Note that the method will also except negative values. 
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 1:</span></dt>
	 * <dd>Remove the first and last index in the array</dd>
	 * <dd><code>sort(1, -1)</code></dd>
	 * </dl>
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 2:</span></dt>
	 * <dd>Only keep the first and last index in the array</dd>
	 * <dd><code>sort(-1, 1)</code></dd>
	 * </dl>
	 * 
	 * @param start
	 *     Where to start
	 *     
	 * @param stop
	 *     Where to stop
	 *     
	 * @return
	 *     This instance
	 */
	public DATATYPE sort(Integer start, Integer stop) {
		if (size() > 0) {
			List<String> list = new ArrayList<>();
			Integer begin = start < 0 ? (mLines.length + start) : start;
			Integer end = stop < 0 ? (mLines.length + stop) : stop;
			
			Integer[] min = null, max = null;
			
			if (begin > end) {
				if (end == 0) {
					min = new Integer[]{ begin };
					max = new Integer[]{ mLines.length };
					
				} else {
					min = new Integer[]{ 0, begin };
					max = new Integer[]{ end, mLines.length };
				}
				
			} else {
				min = new Integer[]{ begin };
				max = new Integer[]{ end };
			}
			
			for (int i=0; i < min.length; i++) {
				list.addAll(Arrays.asList(mLines).subList(min[i], max[i]));
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return (DATATYPE) this;
	}
	
	/**
	 * This is used to determine whether or not to remove lines from the text array. The method will remove each index within the <code>start</code> and <code>stop</code> indexes parsed via the arguments.
	 * <br />
	 * Note that the method will also except negative values. 
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 1:</span></dt>
	 * <dd>Remove the first and last index in the array</dd>
	 * <dd><code>sort(-1, 1)</code></dd>
	 * </dl>
	 * 
	 * <dl>
	 * <dt><span class="strong">Example 2:</span></dt>
	 * <dd>Only keep the first and last index in the array</dd>
	 * <dd><code>sort(1, -1)</code></dd>
	 * </dl>
	 * 
	 * @param start
	 *     Where to start
	 *     
	 * @param stop
	 *     Where to stop
	 *     
	 * @return
	 *     This instance
	 */
	public DATATYPE assort(Integer start, Integer stop) {
		return sort(stop, start);
	}
	
	/**
	 * @see Data#assort(Integer, Integer)
	 */
	public DATATYPE assort(Integer start) {
		return assort(mLines.length, start);
	}
	
	/**
	 * This method will remove all of the empty lines from the text array
	 *     
	 * @return
	 *     This instance
	 */
	public DATATYPE trim() {
		if (size() > 0) {
			List<String> list = new ArrayList<>();

			for (String mLine : mLines) {
				if (mLine.trim().length() > 0) {
					list.add(mLine);
				}
			}
			
			mLines = list.toArray( new String[list.size()] );
		}
		
		return (DATATYPE) this;
	}
	
	/**
	 * This will return the text array
	 *     
	 * @return
	 *     The text array
	 */
	public String[] getArray() {
		return mLines;
	}
	
	/**
	 * This will return a string of the text array with added line breakers
	 *     
	 * @return
	 *     The text array as a string
	 */
	public String getString() {
		return getString("\n");
	}
	
	/**
	 * This will return a string of the text array with custom characters used as line breakers
	 * 
	 * @return
	 *     The text array as a string
	 */
	public String getString(String separater) {
		return mLines == null ? null : TextUtils.join(separater, Arrays.asList(mLines));
	}
	
	/**  
	 * @return
	 *     The last non-empty line of the text array
	 */
	public String getLine() {
		return getLine(-1, true);
	}
	
	/**  
	 * @see Data#getLine(Integer, Boolean)
	 */
	public String getLine(Integer aline) {
		return getLine(aline, false);
	}
	
	/**
	 * This will return one specified line of the text array.
	 * <br />
	 * Note that this also takes negative number to get a line from the end and up
	 * 
	 * @param aline
	 *     The line number to return
	 *     
	 * @param aSkipEmpty
	 *     Whether or not to include empty lines
	 *     
	 * @return
	 *     The specified line
	 */
	public String getLine(Integer aline, Boolean aSkipEmpty) {
		if (size() > 0) {
			Integer count = aline < 0 ? (mLines.length + aline) : aline;
			
			while(count >= 0 && count < mLines.length) {
				if (!aSkipEmpty || mLines[count].trim().length() > 0) {
					return mLines[count].trim();
				}
				
				count = aline < 0 ? (count - 1) : (count + 1);
			}
		}
		
		return null;
	}
	
	/**  
	 * Count the lines in the text array
	 * 
	 * @return
	 *     The number of lines
	 */
	public Integer size() {
		return mLines == null ? 0 : mLines.length;
	}
}
