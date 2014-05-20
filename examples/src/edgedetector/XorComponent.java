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

import com.proglogicjlib.data.Pin;
import com.proglogicjlib.misc.WarningException;
import com.proglogicjlib.vhdl.VhdlComponent;
import com.proglogicjlib.vhdl.VhdlData;
import com.proglogicjlib.vhdl.VhdlKeywords;
import com.proglogicjlib.vhdl.VhdlPin;

public class XorComponent extends VhdlComponent {
	
	public static int INPUT_A = 0;
	public static int INPUT_B = 1;
	public static int OUTPUT_C = 2;

	
	public XorComponent(String ref, String val, boolean upperCase) throws WarningException{
		VhdlPin inputA = new VhdlPin();
		VhdlPin inputB = new VhdlPin();
		VhdlPin outputC = new VhdlPin();
		
		//Set up all pin names
		inputA.setName("A");
		inputB.setName("B");
		outputC.setName("C");
		
		//Set up pin numbers
		inputA.setNo(0);
		inputB.setNo(1);
		outputC.setNo(2);
		
		//Set up all pin directions (input/outputs)
		inputA.setType(Pin.TYPE_IN);
		inputB.setType(Pin.TYPE_IN);
		outputC.setType(Pin.TYPE_OUT);
		
		//Set up all pins as vhdl std logic types
		inputA.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
		inputB.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
		outputC.setDatatype(VhdlKeywords.getDataType(VhdlKeywords.TYPE_STD_LOGIC, upperCase, false, ref));
				
		//set up component information
		super.addVhdlPin(inputA);
		super.addVhdlPin(inputB);
		super.addVhdlPin(outputC);
	
		super.setReference(ref);
		super.setValue(val);
		super.setUpperCase(upperCase);
		super.setIsInlineComp(true);
		
	}//end XorComponent()

	/**
	 * Override the getVhdlComponentInstatiation and add here the code that represents 
	 * the vhdl code that describes this component
	 */
	@Override
	public ArrayList<VhdlData> getVhdlComponentIntantiation(boolean showUnusedInputs) {
		
		String vhdlCode = this.getVhdlPins().get(OUTPUT_C).getHDLsigName() + " <= " + this.getVhdlPins().get(INPUT_A).getHDLsigName() + " " + 
		                  VhdlKeywords.getKeyword(VhdlKeywords.KEY_XOR_ID, this.getUpperCase()) + " " +  this.getVhdlPins().get(INPUT_B).getHDLsigName()+ ";";
		
		VhdlData data = new VhdlData();
		ArrayList<VhdlData> arr = new ArrayList<VhdlData>();
		
        //Set up VHDL code line and comment for that line
		data.setCode(vhdlCode);
		
		arr.add(data);
		
		return arr;
	}//end getVhldComponentInstantiation()

	/**
	 * This simple component does not need an entity, it is rather an inline logic
	 */
	@Override
	public ArrayList<VhdlData> getVhdlEntity(boolean getComponent, int leadingWhiteSpaces) {
		//inline code does not have an entity nor compoent
		return null;
	}//end getVhdlEntitz
	
	public void printCodeToConsole(){
		this.getVhdlPins().get(INPUT_A).print();
		this.getVhdlPins().get(INPUT_B).print();
		this.getVhdlPins().get(OUTPUT_C).print();
		
		
		for(int i = 0; i < this.getVhdlComponentIntantiation(false).size(); i++){
			System.out.println(this.getVhdlComponentIntantiation(false).get(i).getCode());
		}//end for(i)
		
	}
	
	
}//End XorComponent
