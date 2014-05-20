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
import com.proglogicjlib.vhdl.VhdlFile;
import com.proglogicjlib.vhdl.VhdlKeywords;
import com.proglogicjlib.vhdl.components.VhdlTopLevel;



public class EdgeDetector {

	public static void main(String[] args) throws WarningException {
		
		/*
		 * In the example schematic of the edge detector logic we have 4 different
		 * nets. Hence, we create an string array that will later contain the for net names
		*/
		String[] nets = new String[4];
		boolean upperCase = false; //all VHDL keywords are printed in lower case letters
		boolean showUnusedPins = true; //Show pins in VHDL code that have not been connected to a net
		String vhdlTopLevelName = "EdgeDetector";
		String pkgSuffix = "EdgeDetector";
		
		/**
		 * Create all VHDL Top level modules
		 */
        VhdlTopLevel topIn1 = new VhdlTopLevel("TOP1", true, upperCase);
        VhdlTopLevel topIn2 = new VhdlTopLevel("TOP2", true, upperCase);
        VhdlTopLevel topOut = new VhdlTopLevel("TOP1", false, upperCase);
        
        topIn1.setPinName("push_button");
        topIn1.setPinSize("0");

        topIn2.setPinName("clk");
        topIn2.setPinSize("0");
        
        topOut.setPinName("sig_out");
        topOut.setPinSize("0");
        
		/**
		 * Create the VHDL XOR  and flip flop component
		 */
		XorComponent xor = new XorComponent("XOR_1","XOR", upperCase);
	    DFFcomponent dff = new DFFcomponent("FF1","FF", upperCase);

		/**
		 * Assign net names.
		 * In this example the top level pins will dictate the net name. If a net is not connected 
		 * to a top level pin the output connected to the net will dictate the name. Hence we have 
		 * the following nets:
		 * 1. push_button on Top level input component TOP1
		 * 2. clk on Top level input component TOP2
		 * 3. sig_out  Top Level input component TOP3
		 * 4. FF1_Q as the output of the flip flop
		 * 
		 * Note: Top level inputs are logically outputs for the net, because the net is driven
		 * by the input pin
		 */
		nets[0] = topIn1.getVhdlPin().getName(); //Top Level input
		nets[1] = topIn2.getVhdlPin().getName(); //Top Level input
		nets[2] = topOut.getVhdlPin().getName(); //Top Level output
		nets[3] = "FF1_Q";
		
		//Set up nets connected to XOR
		xor.getVhdlPins().get(XorComponent.OUTPUT_C).setHDLsigName(nets[2]); //set up xor output c as Vhdl signal name
		xor.getVhdlPins().get(XorComponent.INPUT_A).setHDLsigName(nets[0]);
		xor.getVhdlPins().get(XorComponent.INPUT_B).setHDLsigName(nets[3]);
		
		//Set up nets connected to DFF
		dff.getVhdlPins().get(DFFcomponent.OUTPUT_Q).setHDLsigName(nets[3]); //set up dff output q as vhdl signal name
		dff.getVhdlPins().get(DFFcomponent.OUTPUT_Q).setIsSignal(true); //mark dff output q as a signal that needs to be added to VHLD signal list because it is driving a net and is not a top level connection
		dff.getVhdlPins().get(DFFcomponent.INPUT_CLK).setHDLsigName(nets[1]);
		dff.getVhdlPins().get(DFFcomponent.INPUT_D).setHDLsigName(nets[0]);

		//Add all VhldComponents to a list for further processing
		ArrayList<VhdlComponent> allComponents = new ArrayList<VhdlComponent>();
		allComponents.add(topIn1);
		allComponents.add(topIn2);
		allComponents.add(topOut);
		allComponents.add(xor);
		allComponents.add(dff);

		
		//Now we are going to create all components that we need for the vhdl code generation
        //Get all signals for VHDL file
		ArrayList<VhdlData> vhdlSignals = new ArrayList<VhdlData>();
		for(int i = 0; i < allComponents.size(); i++){
			vhdlSignals.addAll(allComponents.get(i).getVhdlSignalCode());
		}//end for (i)

		//Create entity for top level VHDL file
        ArrayList<VhdlData> topLevelEntity = VHDLmanipulation.getTopLevelEntity(allComponents, vhdlTopLevelName, upperCase, false, 0);
        for(int i = 0; i < topLevelEntity.size(); i++){
			System.out.println(topLevelEntity.get(i).getCode());
		}//end for(i)
        
		//Create component for top level VHDL file
        ArrayList<VhdlData> topLevelComponent = VHDLmanipulation.getTopLevelEntity(allComponents, vhdlTopLevelName, upperCase, true, VhdlKeywords.LEVEL_2);
        for(int i = 0; i < topLevelComponent.size(); i++){
			System.out.println(topLevelComponent.get(i).getCode());
		}//end for(i)
        
        //Create library code for top level VHDL file; all VhdlComponents that are defined as non inline component must need a package declaration
        ArrayList<VhdlData> libraries = new ArrayList<VhdlData>();
        for(int i = 0; i < allComponents.size(); i++){
        	if(!allComponents.get(i).getIsInlineComp()){//add library when it is not inline code
        		libraries.add(allComponents.get(i).getVhdlPackageName());
        	}//end if
        	
        }//end for(i)
        for(int i = 0; i < libraries.size(); i++){
			System.out.println(libraries.get(i).getCode());
		}//end for(i)
        
        //Now we are ready to create vhld code
        VhdlFile vhdlFile = new VhdlFile();
        //vhdlFile.componentToVhdlFile(topLevelEntity, topLevelComponent, libraries, vhdlSignals, vhdlTopLevelName, "_topPack", "C:\\vhdlTest\\", upperCase, allComponents, false);
        vhdlFile.componentToVhdlFile(vhdlTopLevelName, pkgSuffix, "C:\\vhdlTest\\", upperCase, allComponents, showUnusedPins);
		
	}//end main

}
