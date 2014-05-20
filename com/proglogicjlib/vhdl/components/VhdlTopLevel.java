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
 * special component for FPGA top level inputs
 */
package com.proglogicjlib.vhdl.components;

import java.util.ArrayList;

import com.proglogicjlib.data.Pin;
import com.proglogicjlib.vhdl.VhdlComponent;
import com.proglogicjlib.vhdl.VhdlData;
import com.proglogicjlib.vhdl.VhdlGeneric;
import com.proglogicjlib.vhdl.VhdlPin;

public class VhdlTopLevel extends VhdlComponent {
	public static String TOP_NAME = "TOP";

	private VhdlPin pin;

	public VhdlTopLevel(String ref, boolean input, boolean upperCase){
		super(ref, TOP_NAME);
		pin = new VhdlPin();

		if(input){pin.setType(Pin.TYPE_OUT);}
		else{pin.setType(Pin.TYPE_IN);}
		
		pin.setNo(1);
		pin.setIsTopLevel(true);
		pin.setSize("0");
		
		super.setUpperCase(upperCase);
		super.setIsTopLevelComponent(true);
		super.addVhdlPin(pin);
		super.setIsInlineComp(true);//no package declaration nor components instantiations in VHDL code file
	}//end consturctor
	
	/**
	 * Top level inputs do not have any component instantiation code
	 */
	@Override
	public ArrayList<VhdlData> getVhdlComponentIntantiation(
			boolean showUnusedInputs) {
		// TODO Auto-generated method stub
		return null;
	}//getVhdlComponentInstantiation()
	
	/**
	 * Top level inputs do not have a entity declaration
	 */
	@Override
	public ArrayList<VhdlData> getVhdlEntity(boolean getComponent,
			int leadingWhiteSpaces) {
		return null;
	}//end getVhdlEntity()

	public VhdlPin getVhdlPin(){return this.getVhdlPins().get(0);}//end getVhdlPin()
	
	/**
	 * Sets the top level pin and net name
	 * @param name
	 */
	public void setPinName(String name){
		this.getVhdlPin().setName(name);
		this.getVhdlPin().setHDLsigName(name);	
	}//end setPinName()
	
	//public void setPinIsVector(boolean isVector){this.getVhdlPin().setIsVector(isVector);}
	public void setPinSize(String size){this.getVhdlPin().setSize(size);}

	@Override
	public void addVhdlGenerics(ArrayList<VhdlGeneric> generics) {
		//here we need to add the size inforamtion of the gneric
		
		super.addVhdlGenerics(generics);
	}
	
	
	
}//end class
