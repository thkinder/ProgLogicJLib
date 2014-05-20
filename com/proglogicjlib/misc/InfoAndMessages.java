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
package com.proglogicjlib.misc;

import java.util.ArrayList;

import javax.swing.JOptionPane;


public class InfoAndMessages {
	//reserved keywords
	private static String[] reservedString = {
		"TOP_IN" //id 0
	};
	
	public static int RES_TOP_IN_ID = 0;
	
	/**
	 * This method checks weather or not the parameter keyword is 
	 * a reserved one or not. It will return the keyword if a match was
	 * found
	 * @param keyword
	 * @return
	 */
	public static String containsReservedKeywords(String keyword){
		for(int i = 0; i < reservedString.length; i++){
			if(keyword.contains(InfoAndMessages.reservedString[i])){
				return InfoAndMessages.reservedString[i];
			}
		}
		return null;
	}//end containsReservedKeyword()
	
	//Global general definitions valid for the complete software
	public static String PROGRAM_NAME = "FPGAdesigner";
	
	/**
	 * Show an error message. The program will be closed with System.exit(-1)
	 * after the user acknowledge the JOptionPane window. 
	 * @param code
	 * @param message
	 * @throws CloseProgramException 
	 */
	public static void showError(String code, String message) throws CloseProgramException{//TODO remove!!!!
		JOptionPane.showMessageDialog(null, getErrorCode(code) + " " + message);
		//MainGUI.setTextArea("Error occured.");
		//System.exit(-1);
		throw new CloseProgramException(getErrorCode(code) + " " + message);
	}//end showEror()
	
	public static void showError(String message) throws CloseProgramException{
		JOptionPane.showMessageDialog(null, message);
		//MainGUI.setTextArea("Error occured.");
		//System.exit(-1);
		throw new CloseProgramException(message);
	}//end show Error()
	
	/**
	 * Show a warning messages. This method displays a ArrayList of strings in 
	 * a JOptionPane Window for warning messages. The programm will NOT be closed
	 * after executing this method
	 * @param code
	 * @param messages
	 * @throws WarningException 
	 */
	public static void showWarning(ArrayList<String> messages) throws WarningException{
		String temp = "";
		for(int i = 0; i < messages.size(); i++){
			temp += messages.get(i) + "\n";
		}//end for
		//JOptionPane.showMessageDialog(null, code + " " + temp);
		throw new WarningException(WARN_STRING + temp);
	}//end showWarning()
	
	/**
	 * Show a warning messages. This method displays a ArrayList of strings in 
	 * a JOptionPane Window for warning messages. The programm will NOT be closed
	 * after executing this method
	 * @param code
	 * @param messages
	 * @throws WarningException 
	 */
	public static void showWarning(String code, String message) throws WarningException{
		//JOptionPane.showMessageDialog(null, code + " " + message);
		throw new WarningException(code + " " + message);
	}//end showWarning()
	
	public static void showWarning(String message) throws WarningException{
		//JOptionPane.showMessageDialog(null, code + " " + message);
		throw new WarningException(message);
	}//end showWarning()

	public static String getErrorCode(String code){
		return ERR_STRING + code;
	}
	public static String ERR_STRING = "[ERR] ";
	public static String INFO_STRING = "[INFO] ";
	public static String WARN_STRING = "[WARNING] ";
	
	
	


	
	
}
