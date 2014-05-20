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
 * This Class contains the description of a component pin. Each HDL/Schematic components 
 * contains multiple objects of this class to describe the interfaces of it. 
 */
package com.proglogicjlib.data;

public class Pin {
	/**
	 * Used when pin is an output
	 */
	public static String TYPE_OUT = "out";
	
	/**
	 * Used when pin is an input
	 */
	public static String TYPE_IN = "in";
	
	/**
	 * Used when a pin is an inout
	 */
	public static String TYPE_INOUT = "inout"; 
	
	/**
	 * Used when type of pin is not known
	 */
	public static String UKNOWN = "unknown"; 
	
	/**
	 * Pin Number
	 */
	private int no;
	
	
	/**
	 * Name of the pin
	 */
	private String name;
	
	/**
	 * Input type of the pin, see static type attributes of this class. Determines the direction of the pin. 
	 */
	private String type;

	/**
	 * If pin is a vector this value determines the size of it.
	 */
	private String size;


	/**
	 * Indicates if pin is vector or not
	 */
	private boolean isVector;


	
	/**
	 * Constructor initializes internal variables
	 */
	public Pin(){
		no = -1;
		name = null;
		type = "";
		size = "";
		isVector = false;
	}//end cosntructor
	
	/**
	 * Copy an existing pin into a new pin and make sure that it will use a new
	 * id for the object (new pointer)
	 * @param pin
	 */
	public Pin(Pin pin){
		 int no_s = pin.no;
		 String name_s = pin.name;
		 String type_s = pin.type; 
		 String size_s = pin.size;
     	 boolean isVector_s = pin.isVector;

		 no = no_s;            
		 name = name_s;        
		 type = type_s;
 		 size = size_s;        
		 isVector = isVector_s;    
	}
	
	/**
	 * Create pin and directly set up all data
	 * @param no The pin number of this object
	 * @param compId The id of the component to which this pin belongs to. Can be used for debugging purposes
	 * @param name Pin name. The pin name also consist the direction of the pin. <br>
	 * 			Pin is recognized as input when it contains _i_ or _i(end of string) <br>
	 * 			Pin is recognized as output when it contains _o_ or _o (end of string)
	 * @param type Use static variable TYPE_OUT, TYPE_IN, TYPE_INOUT or UNKNOWM to set up the pins functionality
	 * @param datatype Must be set to the data type. For VHDL files use method VhdlKeyword.getDataType()

	 * @param size When pin is a vector type, for instance std_logic_vector in VHDL this parameter determines the size of the vector.
	 * @param isVector Indicates if this pin is a vector or not
	 * @param VHDLsigName The name that is used later as VHDL signal name. If using another HDL language this would be the signal name in that language.
	 * @param isSignal This flag indicates if the pin is a signal that must be coded into the HDL file or not. Open pins for instance would have set this pin to false.
	 */
	public Pin(int no, int compId, String name, String type, String datatype, String size, boolean isVector, String HDLsigName, boolean isSignal){
		this.no = no;
		this.name = name;
		this.type = type;
		this.size = size;
		this.isVector = isVector;
	}
	
	//set methods
	public void setNo(int no){this.no = no;}
	public void setName(String name){this.name = name;}
	public void setType(String type){this.type = type;}
	public void setSize(String size){this.size = size;}
	//public void setIsVector(boolean isVector){this.isVector = isVector;}
    
	//get methods
	public int getNo(){return this.no;}
	public String getName(){return this.name;}
	public String getType(){return this.type;}
	public String getSize(){return this.size;}
	
	public boolean getIsVector(){
		try {
			Integer temp = Integer.parseInt(size);
			if(temp == 0){return false;}
			else{return true;}
			
		} catch (Exception e) {
			return true;
		}//end catch
		
		
		
		
	}//end getIsVector()
	
	/**
	 * This method is only implemented for debugging purposes. It will print the
	 * pin's settings to the standard output.
	 */
	public void print(){
		System.out.println("--------------------Pin-----------------------");
		System.out.println("Name =" + this.name );
		System.out.println("Number =" + this.no );
		System.out.println("Is Vector =" + this.isVector );
		System.out.println("Direction =" + this.type );
		System.out.println("Size =" + this.size );
	}//end print()
	
}//end class
