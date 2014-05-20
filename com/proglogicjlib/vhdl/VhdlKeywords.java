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
/**
 * This class contains VHDL Keywords and global used keywords
 */
package com.proglogicjlib.vhdl;

import com.proglogicjlib.misc.InfoAndMessages;
import com.proglogicjlib.misc.WarningException;



public class VhdlKeywords {
	
    private static String VHDL_KEYWORDS_TYPE_NOT_SUPPORTED_ID = "(VHDL_KEY:001)";
    private static String VHDL_KEYWORDS_TYPE_NOT_SUPPORTED = VHDL_KEYWORDS_TYPE_NOT_SUPPORTED_ID + "The defined type is not supported.";
	
	private static String[] keywords = {
		"port map", //id 0
		"entity",   //id 1
		"is",       //id 2
		"port",     //id 3
		"std_logic",//id 4
		"std_logic_vector", //id 5
		"end",       // 6
		"signal",  //id 7
		"in",  //id 8
		"out", //id 9
		"inout", //id 10
		"open", //id 11
		"component", //id 12
		"integer", //id 13
		"generic", //id 14
		"package", //id 15
		"architecture", //id 16 
		"of", //id 17
		"begin", //id 18
		"others", //id 19
		"downto", //id 20
		"library", //id21
		"use",  //id 22
		"all", //id 23
		"std_ulogic",//id 24
		"std_ulogic_vector", //id 25
		"boolean", //id 26
		"map", //id 27
		"natural", //id 28
        "when", //id 29
        "and", // id 30
        "else", //id 31
        "work", //id 32
        "xor",//id 33
        "process", //id 34
        "if", //id 35
        "rising_edge", //id 36
        "then"//id 37
	};
	
	/**
	 * Keyword ID s for all used vhdl keywords
	 */
	public static int KEY_PORTMAP_ID = 0;
	public static int KEY_ENTITY_ID = 1;
	public static int KEY_IS_ID = 2;
	public static int KEY_PORT_ID = 3;
	public static int KEY_STD_LOGIC_ID = 4;
	public static int KEY_STD_LOGIC_VECTOR_ID = 5;
	public static int KEY_END_ID = 6;
	public static int KEY_SIGNAL_ID = 7;
	public static int KEY_IN_ID = 8;
	public static int KEY_OUT_ID = 9;
	public static int KEY_INOUT_ID = 10;
	public static int KEY_OPEN_ID = 11;
	public static int KEY_COMPONENT_ID = 12;
	public static int KEY_INTEGER_ID = 13;
	public static int KEY_GENERIC_ID = 14;
	public static int KEY_PACKAGEC_ID = 15;
	public static int KEY_ARCHITECTURE_ID = 16;
	public static int KEY_OF_ID = 17;
	public static int KEY_BEGIN_ID = 18;
	public static int KEY_OTHERS_ID = 19;
	public static int KEY_DOWNTO_ID = 20;
	public static int KEY_LIBRARY_ID = 21;
	public static int KEY_USE_ID = 22;
	public static int KEY_ALL_ID = 23;
	public static int KEY_STD_ULOGIC_ID = 24;
	public static int KEY_STD_ULOGIC_VECTOR_ID = 25;
	public static int KEY_BOOL_ID = 26;
	public static int KEY_MAP_ID = 27;
	public static int KEY_NATURAL_ID = 28;
	public static int KEY_WHEN_ID = 29;
	public static int KEY_AND_ID = 30;
	public static int KEY_ELSE_ID = 31;
	public static int KEY_WORK_ID = 32;
	public static int KEY_XOR_ID = 33;
	public static int KEY_PROCESS_ID = 34;
	public static int KEY_IF_ID = 35;
	public static int KEY_RISING_EDGE_ID = 36;
	public static int KEY_THEN_ID = 37;
	


	
	/**
	 * The level of the code for text alignment in vhd file (number of leading whitespaces in a line of one level)
	 */
	public static int LEVEL_1 = 0;
	public static int LEVEL_2 = 2;
	public static int LEVEL_3 = 4;
	
	/**
	 * Names that are not allowed to used by the user others thatn the vhdl restricitons
	 */
	public static String reservedLibraries[] = {
		"TOP_IN"
	};
	
	/**
	 * Check if the name is defined as a reserved library name
	 * @param name
	 * @return
	 */
	public static boolean isReservedLib(String name){
		for(int i  = 0 ; i < reservedLibraries.length; i++){
			if(reservedLibraries[i].equalsIgnoreCase(name))return true;
		}//end for(i)
		return false;
	}//end isReservedLib()	
	
	/**
	 * Get a vhdl keyword and decide with the upperCase inout if it shall be returned in lower case or upper case.
	 * This feature is used to allow both upper case and lower case printing of keywords within the VHDL result files
	 * @param id
	 * @param upperCase
	 * @return
	 */
	public static String getKeyword(int id, boolean upperCase){
		if(upperCase){
			return keywords[id].toUpperCase();
		}else{
			return keywords[id];
		}//end if
	}

    
    /**
     * get the data type of a signal according to its specified value in the KiCad field.
     * Return value is null when type is not defined. 
     * @param type
     * @param upperCase
     * @param isVector
     * @return
     */
    public static String TYPE_INTEGER = "int";
    public static String TYPE_STD_LOGIC = "stdl";
    public static String TYPE_STD_ULOGIC = "stdul";
    public static String TYPE_BOOL = "bool";
    public static String TYPE_NATURAL = "nat";
    
	public static String getDataType(String type, boolean upperCase, boolean isVector, String warnName) throws WarningException{
		if(type == null || type.equals("")){	
				return getKeyword(KEY_INTEGER_ID, upperCase);
		}
		
		
		if(type.equalsIgnoreCase(TYPE_INTEGER)){
			return getKeyword(KEY_INTEGER_ID, upperCase);
		}else if(type.equalsIgnoreCase(TYPE_STD_LOGIC)){
			if(isVector){
				return getKeyword(KEY_STD_LOGIC_VECTOR_ID, upperCase);
			}else{
				return getKeyword(KEY_STD_LOGIC_ID, upperCase);
			}//end if	
		}else if(type.equalsIgnoreCase(TYPE_STD_ULOGIC)){
			if(isVector){
				return getKeyword(KEY_STD_ULOGIC_VECTOR_ID, upperCase);
			}else{
				return getKeyword(KEY_STD_ULOGIC_ID, upperCase);
			}//end if
		}else if(type.equalsIgnoreCase(TYPE_BOOL)){
			return getKeyword(KEY_BOOL_ID, upperCase);
		}else if(type.equalsIgnoreCase(TYPE_NATURAL)){
			return getKeyword(KEY_NATURAL_ID, upperCase);
		}
		else{
			InfoAndMessages.showWarning(VHDL_KEYWORDS_TYPE_NOT_SUPPORTED + " Instance = " + warnName);
			return "";
		}		
	}//end getDataType()
	
	
	public static VhdlDatatype convertVhdlDatatypeToSystemDatatype(String type){
		type = type.toLowerCase();
		
		if(type.contains(getKeyword(KEY_STD_LOGIC_VECTOR_ID, false))){
			return new VhdlDatatype(TYPE_STD_LOGIC, true);
		}else if(type.equalsIgnoreCase(getKeyword(KEY_STD_LOGIC_ID, false))){
			return new VhdlDatatype(TYPE_STD_LOGIC, false);
		}else if(type.contains(getKeyword(KEY_STD_ULOGIC_VECTOR_ID, false))){
			return new VhdlDatatype(TYPE_STD_ULOGIC, true);
		}else if(type.equalsIgnoreCase(getKeyword(KEY_STD_ULOGIC_ID, false))){
			return new VhdlDatatype(TYPE_STD_ULOGIC, false);
		}else if(type.equalsIgnoreCase(getKeyword(KEY_BOOL_ID, false))){
			return new VhdlDatatype(TYPE_BOOL, false);
		}else if(type.equalsIgnoreCase(getKeyword(KEY_INTEGER_ID, false))){
			return new VhdlDatatype(TYPE_INTEGER, false);
		}else{
			return null;
		}
	}//end convertVhdlDatatyoeToSystemDatatype()
	
}//end class00
