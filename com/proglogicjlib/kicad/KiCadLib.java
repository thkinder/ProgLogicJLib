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
 * Functions for KiCad Libraries
 */
package com.proglogicjlib.kicad;

public class KiCadLib {


	
	
	/**
	 * This is used to create a line comment for a KiCad lib file
	 * @param text
	 * @return
	 */
	public static String getLineComment(String text){
		return "# " + text; 
	}//end getLineComment()
	
	
	public static String getBlockComment(String text){
		return "#------------------------------------------------\n" 
	         + "#" + text + "\n" 
			+  "#-------------------------------------------------\n";
	} //end getBlockComment()
	
	public static String getDEF(String name, String reference, boolean drawPinNumber, boolean drawPinName, String content){
		
		String pinOffset = "40 ";
		String DRAW_PIN_NO = "";
		String DRAW_PIN_NAME = "";
		String UNIT_COUNT = "1 ";
		
		if(drawPinNumber){
			DRAW_PIN_NO = "Y ";
		}else{
			DRAW_PIN_NO = "N ";
		}
		
		if(drawPinName){
			DRAW_PIN_NAME = "Y ";
		}else{
			DRAW_PIN_NAME = "N ";
		}
		
		return getHeader() + KiCadLib.getBlockComment(name) + "DEF " + name + " " + reference + " 0 " + pinOffset 
				+ DRAW_PIN_NO + DRAW_PIN_NAME + UNIT_COUNT + "F" + " N\n"
		        + content + "ENDDEF";
		
	}//end getDEV()
	
	public static String getDRAW(String content){
		return "DRAW\n" + content + "ENDDRAW\n";
	}
	
	public static String getF0(String name,int posX, int posY){
		return "F0 " + "\"" + name + "\"" + " " +  posX + " " + posY + " " + "60 "
	           + "H " + "V " + "C "+ "CNN\n";
				
	}//end getF0() 
	
	public static String getF1(String reference, int posX, int posY){
		return "F1 " + "\"" + reference + "\"" + " " +  posX + " " + posY + " " + "60 "
	           + "H " + "V " + "C "+ "CNN\n";
				
	}//end getF0() 
	
	public static String getRectangle(int startX, int startY, int endX, int endY){
		return "S " + startX + " " + startY + " " + endX + " " + endY + " 0 1 0 N\n";
	}
	
	public static String getField(int number,String value, String name, int posX, int posY, boolean isVisible){
		String visible = "";
		if(isVisible){
			visible = "V ";
		}else{
			visible = "I ";
		}
		
		return "F" + number + " \"" + value + "\"" + " " +  posX + " " + posY + " " + "60 "
		           + "H " + visible + "C "+ "CNN \"" + name + "\"\n";

	}
	
	public static String getHeader(){
		return "EESchema-LIBRARY Version 2.3\n"; 
	}
	
	
	public static String getPin(String name, int number, int posX, int posY, String direction, String electricalDirection){
		
		return "X " + name + " " + number + " " + posX + " " + posY + " " + 200 + " " + direction + 
				" 50 " + "50 1 1 " + electricalDirection + " \n";
		 
	}//end getPin()1
	
	
}//end class
