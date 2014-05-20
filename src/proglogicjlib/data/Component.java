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
 * TODO : All VHDL relevant data and method should be remove from this class. Then take this general class and extend it. Advantage of VHDLcomponent or VerilogComponent
 */

/**
 * This class contains all information that are needed for one component and the 
 * methods to manipulate the data
 */
package com.proglogicjlib.data;

public class Component {
	/*Global Variables*/
	private String reference; /**Reference of schematic symbol, for instance L1 for inductor, this is the unique identifier of one component in a schematic. */
	private String value; /** This is the type of component for instance LM317 voltage regulator etc.  */
	
	/**
	 * Constructor for initializations purposes
	 */
	public Component(){
		this.reference = null;
		this.value = null;
	}//end constructor
	
	/**
	 * Constructor to create a component with a reference and value
	 * @param reference unique identifier of the component. Can be any unique identifier.
	 * @param value name/type of component
	 */
	public Component(String reference, String value){
		this.reference = reference;
		this.value     = value;
		
	}//end constructor
	
	/**
	 * Set up the component reference attribute. 
	 * @param reference
	 */
	public void setReference(String reference){this.reference = reference;}
	
	/**
	 * Set up the components value attribute
	 * @param value
	 */
	public void setValue(String value){this.value = value;};
	
	/**
	 * Get the reference name of the component (unique id)
	 * @return
	 */
	public String getReference(){return this.reference;}
	
	/**
	 * Get the components value attribute
	 * @return
	 */
	public String getValue(){return this.value;}
	
	/**
	 * Giving this method a line that contains a parameter information enclosed in a starting end ending delimiter character
	 * information and it will extract value and return the input line without the size information in it. The return array has always two 
	 * elements
	 * 
	 * Example: 
	 * line = in(32) -->return[0] = 32 return[1] = in
	 * line = testSignal(12) --> return[0] = 12 ; return[1] = testSignal
	 * line = tempSignal --> return[0] = null; return[1] = line = tempSignal
	 * line = testSignal(32 --> return[0] = "err"; return[1] = line = testSignal(32
	 * 
	 * 
	 * @param line
	 * @return
	 */
	public static String[] getParameter(String line, char start, char end){
		String[] retVal = {"",""};
		if(line == null)return retVal;
		if(line.length() < 3) return retVal;
		
		int indexStart = line.indexOf(start);
		if(indexStart < 0){
			
			retVal[0] = null;
		    retVal[1] = line;
		    return retVal;
		}
		int indexEnd = line.lastIndexOf(end);//line.indexOf(end, indexStart);
		if(indexEnd < 0){
			retVal[0] = "err";
			retVal[1] = line;
			return retVal;
		}
		

		if((indexEnd - indexStart) > 1){
			retVal[0] = line.substring(indexStart+1, indexEnd);
		}else{
			retVal[0] =  "";
		}//end if
		
		//remove complete size attribute from the input string
		retVal[1] = line.substring(0,indexStart) + line.substring(indexEnd+1, line.length());
		
		return retVal;
		
	}//end getParameter()





}//end class
