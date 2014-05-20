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
package edgedetector;

import java.util.ArrayList;

import com.proglogicjlib.misc.WarningException;
import com.proglogicjlib.vhdl.VHDLmanipulation;
import com.proglogicjlib.vhdl.VhdlComponent;
import com.proglogicjlib.vhdl.VhdlData;
import com.proglogicjlib.vhdl.VhdlKeywords;
import com.proglogicjlib.vhdl.VhdlPin;

public class DFFcomponent extends VhdlComponent {
	public static int INPUT_D = 0;
	public static int INPUT_CLK = 2;
	public static int OUTPUT_Q = 1;
	public DFFcomponent(String ref, String val, boolean upperCase) throws WarningException{
		VhdlPin d = new VhdlPin();
		VhdlPin q = new VhdlPin();
		VhdlPin clk = new VhdlPin();
		
		//Set up all pin names
		d.setName("d");
		q.setName("q");
		clk.setName("clk");
		
		//Set up all pin directions (input/outputs)
		d.setType(VhdlPin.TYPE_IN);
		q.setType(VhdlPin.TYPE_OUT);
		clk.setType(VhdlPin.TYPE_IN);
		
		//Set up all pins as vhdl std logic types
		d.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
		q.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
		clk.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
				
		//set up vhdl component for flip flop
		super.addVhdlPin(d);
		super.addVhdlPin(q);
		super.addVhdlPin(clk);
		
		super.setReference(ref);
		super.setValue(val);
		super.setUpperCase(upperCase);
		super.setIsInlineComp(true);
		
	}//end DFFcompoen()
	
	
	/**
	 * Override the getVhdlComponentInstatiation and add here the code that represents 
	 * the vhdl code that describes this component
	 */
	@Override
	public ArrayList<VhdlData> getVhdlComponentIntantiation(boolean showUnusedInputs) {
		ArrayList<VhdlData> arr = new ArrayList<VhdlData>();
		VhdlData data = new VhdlData();
		
		String vhdlCode = this.getReference() + "_pro : " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_PROCESS_ID, this.getUpperCase())
				+ "(" + this.getVhdlPins().get(2).getHDLsigName() + ")" ;
		
		data.setCode(vhdlCode);

		arr.add(data);
		data = new VhdlData();
		
		//add begin statement of process
		vhdlCode = VhdlKeywords.getKeyword(VhdlKeywords.KEY_BEGIN_ID, this.getUpperCase());
		data.setCode(vhdlCode);

		arr.add(data);
		data = new VhdlData();
		
		//add rising edge code
		vhdlCode = VHDLmanipulation.whiteSpaceCreator(2) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_IF_ID, this.getUpperCase()) + " " + 
		           VhdlKeywords.getKeyword(VhdlKeywords.KEY_RISING_EDGE_ID, this.getUpperCase()) + "(" + this.getVhdlPins().get(2).getHDLsigName() + ") " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_THEN_ID, this.getUpperCase()) ;
		data.setCode(vhdlCode);

		arr.add(data);
		data = new VhdlData();
		
		//add output assignment
		vhdlCode = VHDLmanipulation.whiteSpaceCreator(4) + this.getVhdlPins().get(1).getHDLsigName() + " <= " + this.getVhdlPins().get(0).getHDLsigName() + ";";
		data.setCode(vhdlCode);

		arr.add(data);
		data = new VhdlData();
		
		//add end if code
		vhdlCode = VHDLmanipulation.whiteSpaceCreator(2) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, this.getUpperCase()) + " "+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_IF_ID, this.getUpperCase()) + ";";
		data.setCode(vhdlCode);
	
		arr.add(data);
		data = new VhdlData();
		
		//add end process statement
		vhdlCode = VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, this.getUpperCase()) +" "+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_PROCESS_ID, this.getUpperCase()) + ";";
		data.setCode(vhdlCode);

		arr.add(data);
		data = new VhdlData();
		
		return arr;
	}//end getVhldComponentInstantiation()

	/**
	 * This simple component does not need an entity, it is rather an inline logic
	 */
	@Override
	public ArrayList<VhdlData> getVhdlEntity(boolean getComponent, int leadingWhiteSpaces) {
		//inline code does not have an entity nor component code
		return null;
	}//end getVhdlEntitz
	
	
}//end class
