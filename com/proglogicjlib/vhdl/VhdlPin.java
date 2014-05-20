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
 * Extend the pin class with all VHDL specific parameter
 */
package com.proglogicjlib.vhdl;

import com.proglogicjlib.data.Pin;
import com.proglogicjlib.misc.WarningException;

public class VhdlPin extends Pin {
	/**
	 * Determines the data type of the pin for instance std_logic or std_ulogic for VHDL implementations
	 */
	private String datatype;
	
	/**
	 * Determines if this pin is connected to a physical FPGA pin = Top Level Pin
	 */
	private boolean isTopLevel;
	
	/**
	 * Determines if this pin is a signal that needs to be coded into the HDL code. 
	 */
	private boolean isSignal;
	
	/**
	 * Signal name used for HDL code generation. unique signal name/net
	 */
	private String HDLsigName;
	
	
	/**
	 * Constructor initializes internal variables
	 */
	public VhdlPin(){
		this.datatype = VhdlKeywords.TYPE_STD_LOGIC;
		this.isTopLevel = false;
		this.isSignal = false;
		this.HDLsigName = "HDLsigNameNotSet";		
	}//end cosntructor
	
	/**
	 * Copy an existing pin into a new pin and make sure that it will use a new
	 * id for the object (new pointer)
	 * @param pin
	 */
	public VhdlPin(VhdlPin pin){
		 String datatype_s = pin.getDatatype();
		 String HDLsigName_s = pin.getHDLsigName(); 
     	 boolean isSignal_s = pin.getIsSignal();
     	 boolean isTopLevel_s = pin.getIsTopLevel();
     	 
     	 this.datatype = datatype_s;
     	 this.isSignal = isSignal_s;
     	 this.isTopLevel = isTopLevel_s;
     	 this.HDLsigName = HDLsigName_s;
	}//end constructor
	
	public VhdlPin(Pin pin, String datatype, String hdlSigName){
		 super(pin);
    	 this.datatype = datatype;
    	 this.HDLsigName = hdlSigName;
	}//end constructor

	public void setDatatype(String datatype){this.datatype = datatype;}
	public String getDatatype(){return this.datatype;}
	public boolean getIsTopLevel(){return this.isTopLevel;}
	public boolean getIsSignal(){return this.isSignal;}
	public String getHDLsigName(){return this.HDLsigName;}
	
	/**
	 * Converts the data type of this pin into VHDL conform code 
	 * @param upperCase
	 * @return
	 */
	public String getVhdlDatatype(boolean upperCase){
		try {
			return VhdlKeywords.getDataType(this.datatype,upperCase , this.getIsVector(), null);
		} catch (WarningException e) {
			return null;
		}//end try
	}//end getVhdlDatatype()
	
	/**
	 * Set the pin functionality to top level signal assignment. Method will
	 * set isSignal to false when its called because a pin cannot be a top level pin and a signal 
	 * at the same time. 
	 * @param isTopLevel Indication if pin is a top level pin (connected to physical FPGA pins) or not
	 */
	public void setIsTopLevel(boolean isTopLevel){
		this.isTopLevel = isTopLevel;
		if(isTopLevel)this.isSignal = false;		
	}//end setIsTopLevel()
	

	/**
	 * Set the pin used as a HDL signal assignment. If set 
	 * the isTopLevel attribute of this element will automatically be set to false, because
	 * a pin cannot be a signal and a top level pin simultaneously
	 * @param isSignal
	 */
	public void setIsSignal(boolean isSignal){
		this.isSignal = isSignal;
		if(isSignal)this.isTopLevel = false;
	}
	
	
	/**
	 * Returns the direction of this pin. If the pin is a top level
	 * pin it will directly negate the pins direction for correct code generations
	 * *(Top level inputs are outputs on the schematic object and vice versa)
	 * @return
	 */
	public String getType(){	
		if (this.isTopLevel){
			if(super.getType().equals(Pin.TYPE_IN)){
				return Pin.TYPE_OUT;
			}else if(super.getType().equals(Pin.TYPE_OUT)){
				return Pin.TYPE_IN;
			}else if(super.getType().equals(Pin.TYPE_INOUT)){
				return Pin.TYPE_INOUT;
			}else{
				return Pin.UKNOWN;
			}//end if
		}else{
			return super.getType();
		}//end if
		
	}//end getType()
	
	
	/**
	 * Set the name of the signal that is used later in the generation of the HDL code. Signal name in HDL can be different from pin name.
	 * This allows that pins can be added with a same name to a component but have different names in the later generated HDL file. Outputs 
	 * in the schematic file will determine the name of a net. The attribute HDLsigName will contain the net to which an inout is connected to. 
	 * @param name
	 */
	public void setHDLsigName(String name){this.HDLsigName = name;}
	
	
	
}//end class

