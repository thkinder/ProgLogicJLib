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
 * This class contains methods that can transform a Kicad netlist file into VHDL source code
 */
package com.proglogicjlib.kicad;

import java.io.File;
import java.util.ArrayList;

import com.proglogicjlib.misc.CloseProgramException;
import com.proglogicjlib.misc.InfoAndMessages;
import com.proglogicjlib.misc.WarningException;
import com.proglogicjlib.vhdl.VhdlComponent;
import com.proglogicjlib.vhdl.VhdlFile;
import com.proglogicjlib.vhdl.VhdlGeneric;
import com.proglogicjlib.vhdl.VhdlPin;
import com.proglogicjlib.vhdl.components.VhdlTopLevel;

public class KiCadToVhdl {
	private static String GENERIC_START_TOKEN = "G_";
	private static String SIGNAL_NAME_FIELD = "SignalName";
	private static String TOP_LEVEL_INPUT_COMP = "TOP_IN";
	private static String TOP_LEVEL_OUTPUT_COMP = "TOP_OUT";
	
	private static String KICAD_TO_VHDL_ERR_ID0 = "KiCadToVhdl_000";
	private static String KICAD_TO_VHDL_ERR_ID1 = "KiCadToVhdl_001";
	private static String KICAD_TO_VHDL_ERR_ID2 = "KiCadToVhdl_002";
	
	/**
	 * This method transforms a KiCad Netlist into VHDL code
	 * @param kicadNetlist This is the KiCad netlist file you want to convert into VHDL
	 * @param VhdlPath This is the path to the folder where you want to save your generated VHDL code file. VHDL files will get the same name as the Kicad schematic name
	 * @param upperCase If true print all VHDL keywords in upper case letters, if false all VHDL keywords are printed in lower case letters
	 * @param showUnusedInputs If true output pins that are not connected will be printed in the VHDL code with the open keyword, inputs that are not assigned will be inserted into code as comments. If false unused pins will not be displayed 
	 * @param defaultDatatype Default data type for entity and signal pins. Use types defined in VhdlKeywords class like VhdlKeywords.TYPE_STD_LOGIC
	 * @param defaultGenericDatatType Default data type for generic datatypes, currently only integer data types can be used
	 * @throws CloseProgramException
	 * @throws WarningException
	 */
	public void netlistToVhdl(File kicadNetlist, String VhdlPath,boolean upperCase, boolean showUnusedInputs, 
                              String defaultDatatype, String defaultGenericDatatType) throws CloseProgramException, WarningException{
		
		boolean noRefferenceFound = true;
		boolean temp_b = false;
		boolean foundoutput = false;
		
		String tempString = "";
		String topLevelName;
		
		int tempPinNo;
		int tempPinIndex; 
		
		//First create VHDLComponents out of KiCad netlist
		/*
		 * We need :
		 *  Reference -> Available in KiCad components attribute
		 *  Name -> Available in KiCad components attribute
		 *  Pins: name, pin number, type -> libparts section of KiCad Netlist
		 *  Field: Name, value 	--> libparts section of KiCad net list
		 *  Nets (HDL signal name of pins) --> nets section of KiCad net list
		 *  	
		 */
		VhdlComponent tempComp = new VhdlComponent(); 
		VhdlTopLevel tempTopComp; 

		ArrayList<Block> blockArr = new ArrayList<Block>(); //Container for KiCad net list, used to separate KiCad net list elements for easier processing
		ArrayList<KiCadDataHelper> valuesRefFieldsData = new ArrayList<KiCadDataHelper>();
		ArrayList<VhdlComponent> vhdlComponents = new ArrayList<VhdlComponent>();
		ArrayList<VhdlTopLevel> vhdlTopComp = new ArrayList<VhdlTopLevel>();
		VhdlPin vhdlTempPin = new VhdlPin();
		ArrayList<KiCadLibparts> pinArr = new ArrayList<KiCadLibparts>();
		ArrayList<VhdlGeneric> tempGeneric = new ArrayList<VhdlGeneric>();

		//Parse KiCad net list
		try {
			blockArr = KiCadParseNetlist.getBlocks(kicadNetlist);
		} catch (CloseProgramException e1) {
			System.out.println("Was not able to parse net list file!. Program will be stopped.");
			System.exit(-1);
		} 
		
		//get the name of the KiCad netlist file, which will be used to determine the name of the vhdl entity
		topLevelName = KiCadParseNetlist.getSchematicName(blockArr.get(KiCadParseNetlist.INDEX_DESIGN));
			
		//Get all values, references and fields that are available in the components block of the KiCad file
		valuesRefFieldsData = KiCadParseNetlist.processComponentsBlock(blockArr.get(KiCadParseNetlist.INDEX_COMPONENTS));
			
		//Create all vhdl components
		for(int i = 0; i < valuesRefFieldsData.size(); i++ ){	
			//Create all VHDL components contained in the KiCad schematic
			if(valuesRefFieldsData.get(i).getValue().equalsIgnoreCase(TOP_LEVEL_INPUT_COMP) || valuesRefFieldsData.get(i).getValue().equalsIgnoreCase(TOP_LEVEL_OUTPUT_COMP)){//create VHDL toplevel input component
				if(valuesRefFieldsData.get(i).getValue().equalsIgnoreCase(TOP_LEVEL_INPUT_COMP)){
					temp_b = true;
				}else{
					temp_b = false;
				}//end if
					
				tempTopComp = new  VhdlTopLevel(valuesRefFieldsData.get(i).getRefference(), temp_b, upperCase);
					
				if((tempGeneric = KiCadToVhdl.convertFieldToGeneric(valuesRefFieldsData.get(i).getFields(), defaultGenericDatatType, tempTopComp.getReference())) != null){
					tempTopComp.addVhdlGenerics(tempGeneric);
				}else{
					//error TopLevelComponent is missing SignalName field!!!
					InfoAndMessages.showError("Top Level pin with the name " + tempTopComp.getReference() + " has no signal name assigned to it. Add field " + SIGNAL_NAME_FIELD + " to the top level pin.");
				}//end if
					
				if(tempTopComp.findGenericByName(SIGNAL_NAME_FIELD) != null){
					tempTopComp.setPinSize( tempTopComp.findGenericByName(SIGNAL_NAME_FIELD).getSize());	
				}//end if
					
				vhdlTopComp.add(tempTopComp);

				}else{//All other VHDL components. 
					//Convert KiCad fields into generics, all KiCad fields that start with GENERIC_START_TOKEN will be recognized as VHDL generics
					tempComp = new VhdlComponent(valuesRefFieldsData.get(i).getRefference(), valuesRefFieldsData.get(i).getValue());
					if((tempGeneric = KiCadToVhdl.convertFieldToGeneric(valuesRefFieldsData.get(i).getFields(), defaultGenericDatatType, tempComp.getReference() )) != null){
						tempComp.addVhdlGenerics(tempGeneric);
						
					}//end if
					
					vhdlComponents.add(tempComp);
				}//end if
			}//end for(i)
			
			//Convert Pinobjects to VhdlPins
			try {
				pinArr = KiCadParseNetlist.getPins(blockArr.get(KiCadParseNetlist.INDEX_LIBPARTS), upperCase);
			} catch (CloseProgramException e) {
				InfoAndMessages.showError(e.getMessage());
			}//end catch
			
			
			//Copy all pins in the right components
			for(int i = 0; i < vhdlComponents.size(); i++){
				for(int k = 0; k < pinArr.size(); k++){
					if(vhdlComponents.get(i).getValue().equalsIgnoreCase(pinArr.get(k).getValue())){
						for(int o = 0 ; o < pinArr.get(k).getPins().size(); o++){
							vhdlTempPin = new VhdlPin(pinArr.get(k).getPins().get(o), defaultDatatype, "");//convert Pin to VhdlPin
							vhdlComponents.get(i).addVhdlPin(vhdlTempPin);
						}//end for (o)
					}//end if					
				}//end for (k)
			}//end for(p)	
			
			//Extract all nets from KiCad netlist section
			ArrayList<KiCadNet> netlist = KiCadParseNetlist.getNets(blockArr.get(KiCadParseNetlist.INDEX_NETS));
			
			//Assign nets to component pins
			for(int netIndex = 0; netIndex < netlist.size(); netIndex++){//go through all nets
				if(netlist.get(netIndex).getPinNumbers().size() > 1){
					for(int pinIndex = 0; pinIndex < netlist.get(netIndex).getPinNumbers().size(); pinIndex++){//go trough all pins connected to a net
						noRefferenceFound = true;
						tempPinNo = netlist.get(netIndex).getPinNumbers().get(pinIndex);
						
						//top level component
						for(int toplevelIndex = 0; toplevelIndex < vhdlTopComp.size(); toplevelIndex++){
							if(vhdlTopComp.get(toplevelIndex).getReference().equalsIgnoreCase(netlist.get(netIndex).getReferences().get(pinIndex))){
								tempPinIndex = vhdlTopComp.get(toplevelIndex).findVhdlPinByNo(tempPinNo);
								
								if(tempPinIndex < 0){
									InfoAndMessages.showError(KICAD_TO_VHDL_ERR_ID0, "Top level input/output pin does not have any pin name (field not defined). Check component " + vhdlTopComp.get(toplevelIndex).getReference());
								}//end if
								
								if(vhdlTopComp.get(toplevelIndex).getVhdlPin().getType().equals(VhdlPin.TYPE_IN) && !foundoutput){
									foundoutput = true;
								}else if(vhdlTopComp.get(toplevelIndex).getVhdlPin().getType().equals(VhdlPin.TYPE_IN) && foundoutput){
									InfoAndMessages.showError("Multiple outputs are connected to net number = " + netIndex + " check component " 
											+ vhdlTopComp.get(toplevelIndex).getReference()); 
								}//
								
								noRefferenceFound = false;
								vhdlTopComp.get(toplevelIndex).getVhdlPins().get(tempPinIndex).setHDLsigName("net_" + ((int)(netIndex+1)));
								
							}//end if
						}//end for (toplevelIndex)
						
						//all other components
						for(int componentIndex = 0; componentIndex < vhdlComponents.size(); componentIndex++){
							if(vhdlComponents.get(componentIndex).getReference().equalsIgnoreCase(netlist.get(netIndex).getReferences().get(pinIndex))){
							
								tempPinIndex = vhdlComponents.get(componentIndex).findVhdlPinByNo(tempPinNo);
								if(tempPinIndex < 0){//this can only happens if KiCad netlist is wrong		
									InfoAndMessages.showError( KICAD_TO_VHDL_ERR_ID1, "Component pin could not be found. Check component "  + vhdlComponents.get(componentIndex).getReference());
								}//end if
								
								noRefferenceFound = false;
								vhdlComponents.get(componentIndex).getVhdlPins().get(tempPinIndex).setHDLsigName("net_" + ((int)(netIndex+1)));
								vhdlComponents.get(componentIndex).getVhdlPins().get(tempPinIndex).setIsSignal(true);
								
								if(vhdlComponents.get(componentIndex).getVhdlPins().get(tempPinIndex).getType().equals(VhdlPin.TYPE_OUT) && !foundoutput){
									foundoutput = true;
								}else if(vhdlComponents.get(componentIndex).getVhdlPins().get(tempPinIndex).getType().equals(VhdlPin.TYPE_OUT) && foundoutput){
									InfoAndMessages.showError("Multiple outputs are connected to net number = " + netIndex + " check component " 
																+ vhdlComponents.get(componentIndex).getReference() + " at pin number " + tempPinIndex ); 
								}//
								
							}//end if
						}//end for (toplevelIndex)
						
						//Error when no component has been found that appeared in the on the current net but is not contained within the vhdl component array. In this case most probably the KiCad netlist file is not correct
						//This error should almost never appear
						if(noRefferenceFound){
							InfoAndMessages.showError(KICAD_TO_VHDL_ERR_ID2, "One or more components that are connected to net number" +  netIndex + " can not be found. Check the KiCad netlist for errors");
						}//end if
						noRefferenceFound = true;
					}//end for(pinIndex)
					foundoutput = false;
				}//end if
			}//end for(netIndex)
				
			//Rename all nets that are connected to top level pins
			for(int i = 0; i < vhdlTopComp.size(); i++){			
				tempString =vhdlTopComp.get(i).getVhdlPin().getHDLsigName();
				for(int compIndex = 0; compIndex < vhdlComponents.size(); compIndex++){
					for(int pinIndex = 0; pinIndex < vhdlComponents.get(compIndex).getVhdlPinCount(); pinIndex++){
						if(vhdlComponents.get(compIndex).getVhdlPins().get(pinIndex).getHDLsigName().equals(tempString)){//found a pin that is connected to current selected net
							vhdlComponents.get(compIndex).getVhdlPins().get(pinIndex).setHDLsigName(vhdlTopComp.get(i).findGenericByName(SIGNAL_NAME_FIELD).getValue());
							vhdlComponents.get(compIndex).getVhdlPins().get(pinIndex).setIsSignal(false);
						}//end if	
					}//end for(pinIndex)
			}//end for(i)
				
			//rename top level pin net name as well
			vhdlTopComp.get(i).getVhdlPin().setHDLsigName(vhdlTopComp.get(i).findGenericByName(SIGNAL_NAME_FIELD).getValue());
			}//end for(i)
						
			//Create VHDL file
			VhdlFile vhdlFile = new VhdlFile();
			ArrayList<VhdlComponent> tempCompArr = new ArrayList<VhdlComponent>();
			
			for(int i = 0; i < vhdlTopComp.size(); i++){
				tempCompArr.add((VhdlComponent)vhdlTopComp.get(i));
			}//end for(i)
			
			tempCompArr.addAll(vhdlComponents);
			vhdlFile.componentToVhdlFile(topLevelName, "_pkg", VhdlPath, upperCase, tempCompArr, showUnusedInputs);

	}//end netlistToVhdl
	
/**
 * convert KiCAd fields into VHDL generics
 *--all fields starting with GENERIC_START_TOKEN will be recognized as generic parameter
 * --all fields with the name SIGNAL_NAME_FIELD will be saved as inactive generics.
 * Inactive generics will not be print in VHDL code but can be used to save temporary data or additional 
 * infos about the component like datasheet locations etc. 
 * @param fields
 * @param datatype
 * @param reference is the reference of the component to create a unique generic parameter in the vhdl code file
 * @return
 */
public static ArrayList<VhdlGeneric> convertFieldToGeneric(ArrayList<KiCadField> fields, String datatype, String ref){
	String size;
	String name = null;
	String value;
	if(fields == null || fields.size() < 1)return null;
	
	ArrayList<VhdlGeneric> temp = new ArrayList<VhdlGeneric>();
	for(int i = 0; i < fields.size(); i++){
		if(fields.get(i).getName().startsWith(GENERIC_START_TOKEN) || fields.get(i).getName().startsWith(SIGNAL_NAME_FIELD)){
			value = fields.get(i).getValue(); 
			VhdlGeneric tempGen = new VhdlGeneric();

			String[] tempSize = VhdlComponent.getParameter(value, '[',']');
			if(tempSize[0] != null){
				size = tempSize[0];
				value = tempSize[1];
			} 
			else {
				size = "0";
			}//end if
			
			if(fields.get(i).getName().startsWith(GENERIC_START_TOKEN)){
				name = fields.get(i).getName().substring(GENERIC_START_TOKEN.length());
				value = fields.get(i).getValue();
				tempGen.isActive(true);
			}else{
				name = SIGNAL_NAME_FIELD;
				tempGen.isActive(false);
			}//end if
			
			tempGen.setGeneric(name, value, datatype, size);
			tempGen.setCompRef(ref);
			temp.add(tempGen);
			
		}//end if
		
	}//end for(i)
	
	return temp;
}//end converFieldToGeneric()
	
}//end class
