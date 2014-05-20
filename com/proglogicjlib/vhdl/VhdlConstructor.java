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
 * This class contains method to create VHLD constructs like entities, components,
 * component instantiations etc
 */
package com.proglogicjlib.vhdl;

import java.util.ArrayList;

import com.proglogicjlib.data.Pin;
import com.proglogicjlib.misc.WarningException;



public class VhdlConstructor {

	
	/*--------------------------------------------------------------------------------------------------------------------
	 * Method for entity and component
	 *-------------------------------------------------------------------------------------------------------------------/	
	/**
	 * This method creates a entity that can be printed into an VHDL file
	 * @param entityName Name of the entity
	 * @param generics ArrayList that contains all generic parameters, can be generated with createGenerics()
	 * @param ports ArrayList that contains all ports, can be generated with createPorts()
	 * @param leadingWhiteSpaces Sett the number of leading white spaces. 
	 * @param upperCase True = print VHDL keywords in upper case letter, false print them in lower case
	 * @param getComponent true = returns the component code, false returns the entity code
	 * @return
	 */
	public static ArrayList<VhdlData> createEntity(String entityName, ArrayList<VhdlData> generics, ArrayList<VhdlData> ports, int leadingWhiteSpaces, boolean upperCase, boolean getComponent){
		ArrayList<VhdlData> retVal  = new ArrayList<VhdlData>();
		
		VhdlData vhdlData = new VhdlData();
		String temp = "";
		
		//Start Entity
		vhdlData = new VhdlData();
		if(getComponent){
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_COMPONENT_ID, upperCase)
					+ " " + entityName + " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_IS_ID, upperCase);
		}else{
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces)  + VhdlKeywords.getKeyword(VhdlKeywords.KEY_ENTITY_ID, upperCase)
					+ " " + entityName + " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_IS_ID, upperCase);
		}
		vhdlData.setCode(temp, 1);
		retVal.add(vhdlData);
		
		//add generics
		for(int i = 0; i < generics.size(); i++){
			vhdlData = new VhdlData();
			vhdlData = generics.get(i);
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces)  +  vhdlData.getCode();
			vhdlData.setCode(temp);
			retVal.add(vhdlData);
		}//end for (i)
		
		//add an empty line between generics and ports, but only if there is at least one generic defined
		if(generics.size() > 0){
			vhdlData = new VhdlData();
			vhdlData.setCode("", 1);
			retVal.add(vhdlData);
		}
		
		//ad ports
		for(int i = 0; i < ports.size(); i++){
			vhdlData = new VhdlData();
			vhdlData = ports.get(i);
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces)  + vhdlData.getCode();
			vhdlData.setCode(temp);
			retVal.add(vhdlData);
		}//end for (i)
		
		//end entity
		vhdlData = new VhdlData();
		if(getComponent){
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces)  + VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, upperCase)
					+ " "+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_COMPONENT_ID, upperCase) + ";";
			vhdlData.setCode(temp);
		}else{
			temp = VHDLmanipulation.whiteSpaceCreator(leadingWhiteSpaces)  + VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, upperCase)
					+ " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_ENTITY_ID, upperCase) +";";
			vhdlData.setCode(temp);
		}
		retVal.add(vhdlData);
		
		return retVal;
	}//end createEntity()
	
	/**
	 * Create the components instantiation out of a component object
	 * @param component
	 * @param upperCase
	 * @param showUnusedInputs
	 * @return
	 */
	public static ArrayList<VhdlData> createComponentInstantiation(VhdlComponent component, boolean upperCase, boolean showUnusedInputs){
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		VhdlData tempData = new VhdlData();
		String line = new String();
		String tempString = "";
		
		//set up reference and name of component instantiation
		tempData.setCode(component.getReference() + " : " + component.getValue(), 0);		
		retVal.add(tempData);
		tempData = new VhdlData();
		
		//set up generic map portion of VHDL code
		if(component.hasVhdlGenerics()){//display generics only if some are available
			tempData = new VhdlData();
			line =  VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1)+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_GENERIC_ID, upperCase) + " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_MAP_ID, upperCase) + " (";
			tempData.setCode(line,1);
			retVal.add(tempData);
			
			for(int i = 0; i < component.getVhdlGenericsCount(); i++){//process generics and add them to code
				tempString = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + component.getVhdlGenerics().get(i).getName() + " => " + component.getReference() + "_" +component.getVhdlGenerics().get(i).getName() +  ",";
				tempData = new VhdlData();
				tempData.setCode(tempString,1);
				retVal.add(tempData);

			}//end for (i)
			
			tempData.setCode(tempData.getCode().replaceAll(",$", ""),1);
			retVal.set(retVal.size()-1, tempData);

			tempData = new VhdlData();
			line =  VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1)+ ")";
			tempData.setCode(line,1);
			retVal.add(tempData);	
		}//end if
		
		//set up port map
		tempData = new VhdlData();
		line =  VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1)+VhdlKeywords.getKeyword(VhdlKeywords.KEY_PORTMAP_ID, upperCase) + "( ";
		tempData.setCode(line, 1);	
		retVal.add(tempData);

		//add port signal
		for(int i = 0; i < component.getVhdlPinCount(); i++){
			tempData = new VhdlData();//clean data object
			
			if(component.getVhdlPins().get(i).getHDLsigName() != null && !component.getVhdlPins().get(i).getHDLsigName().equals("")){//current pin is connected to a signal
				tempData.setCode( VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + component.getVhdlPins().get(i).getName() + " => " + component.getVhdlPins().get(i).getHDLsigName() + ",", retVal.size());
				tempData.setComment("",-1);
				retVal.add(tempData);

			}else{//current pin is not used it will be put into a comment; if the pin is an output the "open" keyword will be used
				if(component.getVhdlPins().get(i).getType() == Pin.TYPE_OUT){//use open keyword
					tempData.setCode(  VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + component.getVhdlPins().get(i).getName() + " => " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_OPEN_ID, upperCase) + ",", retVal.size());
					tempData.setComment("", -1);
					retVal.add(tempData);
					
				}else{//if input, put signal into a comment
					if(showUnusedInputs){
						tempData.setComment(  VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2)+"--" + component.getVhdlPins().get(i).getName() + " => ,", retVal.size());
						tempData.setCode("", -1);
						retVal.add(tempData);
					}else{
						tempData.setComment("", -1);
						tempData.setCode("", -1);
						//retVal.add(tempData);
					}//end if unused inputs	
				}//end if
			}//end if
		}//end for
		
		//remove the ',' from the last signal object
		for(int k = retVal.size()-1; k >= 0 ; k--){
			if(retVal.get(k).getCodeIndex() >= 0){
				tempData = retVal.get(k);
				tempData.setCode(tempData.getCode().replaceAll(",", "" ), k);
				break;
			}//end if
		}//end for
		
		tempData = new VhdlData();//clean data object		
		tempData.setCode( VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1)+ ");", retVal.size());
		retVal.add(tempData);
		
		//align code properly
		VHDLmanipulation mani = new VHDLmanipulation();
		retVal = mani.lineUpKeyword(retVal, "=>", "", false, 0);
		
		return retVal;	
	}


	/*--------------------------------------------------------------------------------------------------------------------
	 * Method for generics
	 *-------------------------------------------------------------------------------------------------------------------/
   
    /**
     * Returns the generic code of an ArrayList of field objects that contain the generic parameter of an object
     * This can be used to create the generic code lines for a component class object
     * @param fields
     * @param upperCase
     * @return
     */
	public static ArrayList<VhdlData> createGenerics(ArrayList<VhdlGeneric> generics, Boolean upperCase) throws WarningException{
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		boolean foundGeneric = false;
		String temp = "";
		VhdlData vhdlData = new VhdlData();

		
		for(int i = 0; i < generics.size() ; i++){//go trough all fields. As soon as the first generic parameter has been found add code lines
			
			
			if(generics.get(i).isActive()){
				if(!foundGeneric){//only executed for the first generic
					temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + VhdlKeywords.getKeyword( VhdlKeywords.KEY_GENERIC_ID, upperCase) + "(";
					vhdlData.setCode(temp,  retVal.size());
					retVal.add(vhdlData);
					vhdlData = new VhdlData();
					foundGeneric = true;
				}//end if
				
				boolean isVector = generics.get(i).isVector();
				String type = VhdlKeywords.getDataType(generics.get(i).getDatatype(), upperCase, isVector, "VhdlConstructor::createGenerics::");
				String size = generics.get(i).getSize();
				String value = generics.get(i).getValue();
				
				vhdlData = new VhdlData();
				temp = createGenericLine(VhdlKeywords.LEVEL_3, generics.get(i).getUniqueName(), value, type, size, upperCase, isVector, true);
				vhdlData.setCode(temp,1);	
				retVal.add(vhdlData);
			}//end if

		}//end for(i)
		
        if(foundGeneric){
			vhdlData.setCode(vhdlData.getCode().replaceAll(";$", ""));//remove the semicolon from the last entry
		
			vhdlData = new VhdlData();
			temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2)+ ");";
			vhdlData.setCode(temp, 1);
			retVal.add(vhdlData);
			
			foundGeneric = false;
        }
		
		VHDLmanipulation manip = new VHDLmanipulation();
		retVal = manip.alignCodeBlock(retVal);
		
		
		return retVal;
	}
	
	/**
	 * Create a generic parameter String that can be inserted into a VHLD entity under the generic section of it. It does not contain the 
	 * start and the end of the generic, it simply creates one line of a generic parameter for VHDL files. 
	 * @param level
	 * @param name
	 * @param value
	 * @param type
	 * @param size
	 * @param upperCase
	 * @param isVector
	 * @param addSemicolon
	 * @return
	 */
	private static String createGenericLine(int level, String name, String value, String type, String size,  boolean upperCase, boolean isVector, boolean addSemicolon ){
		String retVal = "";
		if(isVector){
			if(addSemicolon){
				retVal = VHDLmanipulation.whiteSpaceCreator(level) + name + " : " + type + "(" + size + " " +VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, upperCase) + " 0) := " + value + ";";	
			}else{
				retVal = VHDLmanipulation.whiteSpaceCreator(level) + name + " : " + type + "(" + size + VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, upperCase) + " ) := " + value;	
			}//end if
		}else{
			if(addSemicolon){
				retVal = VHDLmanipulation.whiteSpaceCreator(level) + name + " : " + type + " := " + value + ";";	
			}else{
				retVal = VHDLmanipulation.whiteSpaceCreator(level) + name + " : " + type + " := " + value;
			}//end if
		}//end if vector
		return retVal;
	}//end createGenericLine()
	
	/*--------------------------------------------------------------------------------------------------------------------
	 * Method for ports
	 *-------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Converts an array list of pin data into a VhdlData structure that contains the vhdl code for 
	 * the port declaration
	 * @param upperCase
	 * @param pins
	 * @param reference Component name used for error message generation
	 * @return
	 */
	public static ArrayList<VhdlData> createPorts(boolean upperCase, VhdlComponent component, String reference) {
		String temp = "";
		String direction = "";
		int size = 0;
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		VhdlData vhdlData = new VhdlData();
		ArrayList<VhdlPin> pins = component.getVhdlPins();
		
		temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_PORT_ID, upperCase) + "(";
		vhdlData.setCode(temp,1);
		retVal.add(vhdlData);
		vhdlData = new VhdlData();
		temp ="";
		
		for(int i = 0; i < pins.size(); i++){
			direction = pins.get(i).getType();
			if(direction.contains("in"))direction = direction + " ";
			
			if(pins.get(i).getIsVector() && pins.get(i).getSize() != null && pins.get(i).getSize() != ""){//current pin is a vector
				try {
					size = Integer.parseInt(pins.get(i).getSize()); 
					size--;
					temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_3) + pins.get(i).getHDLsigName() + " : " + direction + " " +
							pins.get(i).getVhdlDatatype(upperCase) + "(" + size 
							+ " " +VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, upperCase) + " 0) := (" + VhdlKeywords.getKeyword(VhdlKeywords.KEY_OTHERS_ID, upperCase)+" => '0');";
				
				} catch (NumberFormatException e) {//found either a generic in size or something different
							temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_3) + pins.get(i).getHDLsigName() + " : " + direction + " " + pins.get(i).getVhdlDatatype(upperCase) + "(" + pins.get(i).getSize() 
									+ " " +VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, upperCase) + " 0) := ("+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_OTHERS_ID, upperCase)+ " => '0');";
				}//end try-catch
				
			}else{
				temp = VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_3) + pins.get(i).getHDLsigName() + " : " + direction + " " + pins.get(i).getVhdlDatatype(upperCase) + " := '0';";	
			}//end if
			
			if(i == pins.size()-1) temp = temp.replaceAll(";", "");
			vhdlData.setCode(temp,retVal.size());
			retVal.add(vhdlData);
			vhdlData = new VhdlData();
		}//end for
		
		vhdlData.setCode(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + ");");
		
		//align code
		VHDLmanipulation manip = new VHDLmanipulation();
		retVal = manip.alignCodeBlock(retVal);
		retVal.add(vhdlData);
		
		return retVal;
	}//end getPorts()
	
	
	
}//end class
