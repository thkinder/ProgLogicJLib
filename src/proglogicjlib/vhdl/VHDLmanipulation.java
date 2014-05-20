/*BSD 3 clause license

Copyright (c) 2014, Thomas Kinder, info@thinkingsand.com
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification,are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
   may be used to endorse or promote products derived from this software 
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Thomas Kinder OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.proglogicjlib.vhdl;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import com.proglogicjlib.misc.WarningException;


public class VHDLmanipulation {
	
	public static String GENERIC_TO_FIELD_ERROR_ID = "(VhdlManip:001)";
	public static String GENERIC_TO_FIELD_ERROR = GENERIC_TO_FIELD_ERROR_ID + " Generic could not be converted to field. Missing charactes like brackets \";\" or \":\"";
	
	public static String GENERIC_TO_FIELD_DATATYPE_ERROR_ID = "(VhdlManip:002)";
	public static String GENERIC_TO_FIELD_DATATYPE_ERROR = GENERIC_TO_FIELD_DATATYPE_ERROR_ID + " Datatype of one generic is not supported.";
	
	public static String GENERIC_TO_FIELD_SIZE_IS_NO_INTEGER_ERROR_ID = "(VhdlManip:003)";
	public static String GENERIC_TO_FIELD_SIZE_IS_NO_INTEGER_ERROR = GENERIC_TO_FIELD_SIZE_IS_NO_INTEGER_ERROR_ID + " The upper and lower size limits of a generic vector are no integer values.";
	
	public static String PORT_TO_PIN_ERROR_ID = "(VhdlManip:004)";
	public static String PORT_TO_PIN_ERROR = PORT_TO_PIN_ERROR_ID + " Port could not be converted to pin. Missing charactes like brackets \";\" or \":\"";
	
	public static String PORT_TO_PIN_DATATYPE_ERROR_ID = "(VhdlManip:005)";
	public static String PORT_TO_PIN_DATATYPE_ERROR = PORT_TO_PIN_DATATYPE_ERROR_ID + " Datatype of one port is not supported.";
	
	public static String PORT_TO_PIN_SIZE_IS_NO_INTEGER_ERROR_ID = "(VhdlManip:006)";
	public static String PORT_TO_PIN_SIZE_IS_NO_INTEGER_ERROR = PORT_TO_PIN_SIZE_IS_NO_INTEGER_ERROR_ID + " The upper and lower size limits of a port vector are no integer values.";

	public static String GENERIC_DOES_NOT_EXIST_ID = "(VhdlManip:007)";
	public static String GENERIC_DOES_NOT_EXIST = GENERIC_DOES_NOT_EXIST_ID + " VHDL file does not have any generics.";
	
	public static String PORT_DOES_NOT_EXIST_ID = "(VhdlManip:008)";
	public static String PORT_DOES_NOT_EXIST = PORT_DOES_NOT_EXIST_ID + " VHDL file does not have any ports.";

	
	/**
	 * This method can write a string to the system clip board
	 * @param s string that shall be copied to clip board
	 */
	public static void writeToClipboard(String s){
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  Transferable transferable = new StringSelection(s);
	  clipboard.setContents(transferable, null);
	}//end writeToClipBoard()
	
	
	/**
	 * Set the maximum code length. This value can be used later
	 * to properly a line vhdl code and comment in respect to the
	 * longest vhdl code line that was processed. 
	 * @param maxLength
	 */
	public void setMaxCodeLength(int maxLength){
		this.maxLengthCode = maxLength;
	}//end setMaxCodeLengt()
	private int maxLengthCode;
	
	/**
	 * Use this method to find the longest code segment within an arraylist of vhdlData
	 * @param vhdlData
	 * @return
	 */
	public static int findMaxLenghtCode(ArrayList<VhdlData> vhdlData){
		int max = 0;
		for(int i = 0; i < vhdlData.size(); i++){
			if(vhdlData.get(i).getCode().length() > max){
				max = vhdlData.get(i).getCode().length();
			}
		}//end for(i)
		return max;
	}//end findMaxLengthCode();
	
	/**
	 * Returns the length of the longest vhdl code line processed in the last 
	 * call of the method lineUpKeyword
	 * @return
	 */
	public int getMaxLength(){
		return this.maxLengthCode;
	}//end getMaxLength()
	
	/**
	 * With this method a VhdlData ArrayList can be reorder according to certain pattern strings
	 * @param data VHDL data that shall be aligned
	 * @param keyWord keyword that shall be used for string alignment
	 * @param leadingWhiteSpace the number of leading white spaces that shall be add to any line of the data input
	 * @param leadingString a leading string can be added to the data input 
	 * @return returns the aligned data as and ArrayList of VhdlData
	 */
	public ArrayList<VhdlData> lineUpKeyword(ArrayList<VhdlData> vhdlData, String keyWord, String leadingString, Boolean removeMultipleWhiteSpaces, int leadingWhiteSpaces){
			if(vhdlData == null || keyWord == null) return null;
			int maxKeywordIndex = 0;
			int indexKeyWord = 0;
			ArrayList<Integer> keywordLineFound = new ArrayList<Integer>(); 
			ArrayList<Integer> keywordIndexFound = new ArrayList<Integer>();
			ArrayList<String> newData = new ArrayList<String>();
			String code = null;
			int tempIndex;
			
			//Determine maximum index of keyword position and remove multiple white spaces, remember the the lines where the code was found and the index position
			for(int i = 0; i < vhdlData.size(); i++){
				code = vhdlData.get(i).getCode();
				tempIndex = vhdlData.get(i).getCodeIndex();
				
				if (removeMultipleWhiteSpaces){
					code = removeMultiWhiteSpace(code);
				}//end if
				
				if(tempIndex >= 0){//line contains code
					//code = code.trim();
					if(leadingString != null)code = leadingString + code; //add a leading string for signal conversion
					
					indexKeyWord = code.indexOf(keyWord);
					
					if(indexKeyWord >= 0){//line contains kyword
						keywordLineFound.add(i);
						keywordIndexFound.add(indexKeyWord);
						if(indexKeyWord > maxKeywordIndex)maxKeywordIndex = indexKeyWord; 
					}//end if
					
					newData.add(code);
				}//end if
				else{
					newData.add("");
				}//end else		
			}//end for
			
			//reorder all strings that are containing the keyWord
			if(keywordLineFound.size() > 0){
				for(int i = 0; i < keywordLineFound.size(); i++){
					code = newData.get(keywordLineFound.get(i));
					String firstPart = code.substring(0,keywordIndexFound.get(i));
					String secondPart = code.substring(keywordIndexFound.get(i));
					int difference = maxKeywordIndex - firstPart.length();
					String code_t = whiteSpaceCreator(leadingWhiteSpaces) + firstPart + whiteSpaceCreator(difference) + secondPart;
					if(code_t.length() > this.maxLengthCode)this.maxLengthCode = code_t.length() ; //TODO : Check if this statement is really necessary and if not make a static method out of it
					vhdlData.get(keywordLineFound.get(i)).setCode(code_t);
				}//end for
			}//end if
			
			if(leadingWhiteSpaces > 0){
				for(int i = 0; i < vhdlData.size(); i++ ){
					vhdlData.get(i).setCode(whiteSpaceCreator(leadingWhiteSpaces)+vhdlData.get(i).getCode().trim());
				}//end for
			}//end if
				
			return vhdlData;		
		}//end lineUpKeyword()


	/**
	 * This method can remove a specified keyword from all vhdl code lines
	 * @param vhdlData vhdl code data as ArrayList of VhdlData
	 * @param keyword keyword that shall be removed from string line
	 * @param replace string that shall be added to the sting line instead of keyword
	 * @return
	 */
	public static ArrayList<VhdlData> removeKeyWord(ArrayList<VhdlData> vhdlData, String keyword, String replace){
		if(keyword == null) return vhdlData;
		if(vhdlData == null) return null;
		
		for(int i = 0; i < vhdlData.size(); i++){
			String code = vhdlData.get(i).getCode();
			int indexKeyword = code.indexOf(keyword);
			if(indexKeyword >= 0){
				vhdlData.get(i).setCode(code.replace(keyword, replace));
			}//end if
		}//end for
		return vhdlData;
	}//end removeKeyWord()
	
	/**
	 * This method can replace a part of the code that is defined between a start and an end
	 * character or string. 
	 * @param vhdlData vhdl code data that shall be manipulated
	 * @param start start string
	 * @param end end string
	 * @param replace replace everything between start and end with this string. If this is null it will only remove
	 * the start and the stop string from the vhdlData input
	 * @param replaceEndMissing if the end string has not been found in the input vhdlData, the code line from the start
	 * index to the end of the line is exchanged with this string. 
	 * @return
	 */
	public static ArrayList<VhdlData> replaceCodePart(ArrayList<VhdlData> vhdlData, String start, String end, String replace, String replaceEndMissing){
		if(vhdlData == null || start == null || end == null ) return null;
		int indexEnd = -1;
		int indexStart = -1;
		for(int i = 0; i < vhdlData.size(); i++){
			String code = vhdlData.get(i).getCode();
			indexStart = code.indexOf(start);
			if(indexStart >= 0){
				indexEnd = code.indexOf(end);
				if(replace != null){
					if(indexEnd >= 0){
						if(indexEnd >= 0) vhdlData.get(i).setCode(code.substring(0, indexStart)+ replace + code.substring(indexEnd+end.length()));
						else vhdlData.get(i).setCode(code.substring(0, indexStart)+ replaceEndMissing);
					}
				}//end if
				else{
					if(indexEnd >= 0){
						vhdlData.get(i).setCode(replaceEndMissing + code.substring((indexStart+start.length()),indexEnd));	
					}//end if
					else{
						vhdlData.get(i).setCode( replaceEndMissing + code.substring((indexStart+start.length())));	
					}//end else
				}//end else
			}//end if
		}//end for
		return vhdlData;
	}//end replaceCodePart()
	
	/**
	 * Create a string that only contains of white spaces
	 * @param no number of whitespace in the return string
	 * @return
	 */
	public static String whiteSpaceCreator(int no){
		String returnStr = "";
		if(no <= 0) return "";
		for(int i = 0; i < no ; i++){
			returnStr = returnStr + " ";
		}//end for
		return returnStr;
	}//end whiteSpaceCreator
	
	/**
	 * This method removes multiple white spaces " " from a string
	 * @param line data from which the multiple white spaces shall be removed
	 * @return 
	 */
	public static String removeMultiWhiteSpace(String line){
		if(line == null) return null;
		
		while(line.contains("  ")){
			line = line.replace ("  ",  " ");
		}//end while
		return line;
	}//end removeMultiWhiteSpace()
	
	/**
	 * Remove leading and trailing whitespaces from each code element of an VhdlData object
	 * stored in an ArrayList<VhdlData>
	 * @param vhdlData
	 * @return
	 */
	public static ArrayList<VhdlData> trimCode(ArrayList<VhdlData> vhdlData){
		for(int i = 0; i < vhdlData.size(); i++){//go over all code and remove leading and trailing whitespaces
			vhdlData.get(i).setCode(vhdlData.get(i).getCode().trim());	
		}//end for(i)	
		return vhdlData;
	}//end removeMultiWhiteSpace()
	
	/**
	 * This method counts the leading white spaces of a string and returns the
	 * counted value. 
	 * @param line
	 * @return number of leading spaces if return value is equal to 0 no whitespace
	 * was found at the beginning of the String
	 */
	public static int countLeadingWhiteSpace(String line){
		int i = 0;
		while(i < line.length() && line.charAt(i) == ' '){
			i++;
		}//end while
		return i;
	}//end countLeadingWhiteSpace()
	

	/**
	 * This method converts an ArrayList of VhdlData objects into a single string
	 * @param vhdlData
	 * @return
	 */
	public static String convertVhdlDataArrayToString(ArrayList<VhdlData> vhdlData){
		String ret_str = "";
		if(vhdlData == null)return null;
		for(int i = 0; i < vhdlData.size(); i++){
			String code = vhdlData.get(i).getCode();
			String comment = vhdlData.get(i).getComment();
			
			if(i < vhdlData.size()-1) ret_str += code + comment+ "\n";
			else ret_str += code + comment;
		}//end for
		return ret_str;
	}//end convertVhdlDataArrayToString()

    /**
     * Print an ArrayList that contains strings to the console
     * System.out.print
     * @param list
     */
	public static void printArray(ArrayList<String> list){
		if (list == null){
			System.out.println("nullpointer");
			return;
		}//end if
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
			
		}//end for
		return;
	}//end printArray()
	
	
	/**
	 * Method adds a leading string to all code lines of the vhdl elements in the array that are stored in the array list 
	 * in between the startIndex and the endIndex. If the endIndex is grater than the array list size it will be truncated 
	 * to that value. 
	 * @param vhdlData
	 * @param startIndex
	 * @param endIndex
	 * @param leading
	 * @return
	 */
	public static ArrayList<VhdlData> addLeadingStringToLines(ArrayList<VhdlData> vhdlData, int startIndex, int endIndex, String leading){
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		VhdlData data = new VhdlData();
		if(endIndex > vhdlData.size())endIndex = vhdlData.size();
		if(startIndex > vhdlData.size())startIndex = vhdlData.size();
		if(leading == null)return vhdlData;
		
		for(int i = 0; i < startIndex; i++){
			data = new VhdlData();
			data.setCode(vhdlData.get(i).getCode());
			retVal.add(data);
		}
		
		for(int i = startIndex; i < endIndex; i++){
			data = new VhdlData();
			data.setCode(leading + vhdlData.get(i).getCode());
			retVal.add(data);
		}//end for(i)
		
		for(int i = endIndex; i < vhdlData.size(); i++){
			data = new VhdlData();
			data.setCode(vhdlData.get(i).getCode());
			retVal.add(data);
		}
		
		return retVal;
	}
	
	
	/**
	 * Code block allignment rules
	 * @param vhdlData
	 * @return
	 */
	public ArrayList<VhdlData> alignCodeBlock(ArrayList<VhdlData> vhdlData){
		vhdlData = this.lineUpKeyword(vhdlData, " :", "", false, 0);
		vhdlData = this.lineUpKeyword(vhdlData, " :=", "", false, 0);
		
		return vhdlData;
	}//end alignCodeBlock()

	/**
	 * Returns a sub list of the data array list defined by the start and the end index
	 * @param data
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static ArrayList<VhdlData> getSubList(ArrayList<VhdlData> data, int startIndex, int endIndex){
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		
		if(startIndex > data.size()){
			return null;
		}
		if(endIndex > data.size()){
			return null;
		}
		
		for(int i = startIndex; i <= endIndex; i++){
			retVal.add(data.get(i));
		}//end for(i)
		
		return retVal;
	}//end getSubList()
	
	
	/**
	 * Extract the string part between the start and the end string and return the value
	 * @param line
	 * @param start
	 * @param end
	 * @param lastOccurenceOfEnd
	 * @return
	 */
	public static String extractParameterFromLine(String line, String start, String end, boolean lastOccurenceOfEnd){
		int indexStart = 0, indexEnd = 0;

		indexStart = line.indexOf(start);
		if(indexStart < 0 ) return null;
		
		if(lastOccurenceOfEnd){
			indexEnd = line.lastIndexOf(end);
			if(indexStart < 0) return null;
		}else{
			indexEnd = line.indexOf(end,indexStart);
			if(indexStart < 0) return null;
		}//end if
		
		return line.substring(indexStart+start.length(), indexEnd);

	}//end extractParameterFromLine()

	
	/**
	 * Generate top level entity out of a component description
	 * @param upperCase
	 * @return
	 * @throws WarningException 
	 * @throws CloseProgramException 
	 */
	public static ArrayList<VhdlData> getTopLevelEntity(ArrayList<VhdlComponent> comp, String topLevelEntityName, boolean upperCase, boolean getComponent, int leadingWhiteSpaces) throws WarningException{
		
		ArrayList<VhdlGeneric> vhdlGenerics = new ArrayList<VhdlGeneric>();
		ArrayList<VhdlPin> vhdlPins = new ArrayList<VhdlPin>();
		ArrayList<String> compRef = new ArrayList<String>();
		
		//Get all generics of all components
		for(int i = 0; i < comp.size(); i++){
			vhdlGenerics.addAll(comp.get(i).getVhdlGenerics());
			vhdlPins.addAll(comp.get(i).getVhdlToplevelPins());
			compRef.add(comp.get(i).getReference());
		}//end for (i)
		
		
		VhdlComponent entityComp = new VhdlComponent();
		entityComp.setGenericsPins(vhdlGenerics, vhdlPins);
		
		ArrayList<VhdlData> ports = VhdlConstructor.createPorts(upperCase, entityComp, topLevelEntityName);
		ArrayList<VhdlData> generics = VhdlConstructor.createGenerics(vhdlGenerics, upperCase);
		
		return VhdlConstructor.createEntity(topLevelEntityName, generics, ports, leadingWhiteSpaces, upperCase, getComponent);
	}// end getTopLevelEntity();
	
	
}//end class
