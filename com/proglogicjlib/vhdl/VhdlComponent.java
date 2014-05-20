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
 * This class contains all information that a VHDL file needs. It is an child class that
 * is extended from Component class
 */
package com.proglogicjlib.vhdl;

import java.util.ArrayList;

import com.proglogicjlib.data.Component;
import com.proglogicjlib.misc.CloseProgramException;
import com.proglogicjlib.misc.WarningException;


public class VhdlComponent extends Component {
	//Boolean attributes
	private boolean upperCase; /** Indicates if VHDL keywords shall be printed in upper or lower case letters.*/
	private boolean isInlineComponent;
	private boolean isTopLevel = false; /**Some components need to be marked as top level components for VHDL translation. This information is not contained in KiCad schematics. Special components can be designed to allow the connection of Schematic pins to physical FPGA pins in the VHDL code that will be generated */

	//String attributes
	private String pkgSuffix; /**This suffix string is used later in the VHDL code generation for defining package declarations.*/	
	
	//Dynamic List objects
	private ArrayList<VhdlPin> vhdlPins;
	private ArrayList<VhdlGeneric> vhdlGenerics;
	
	//Method to initialize objects variables. 
	private void initVariablesConstructor(){
		this.upperCase = false;
		this.isInlineComponent = false;
		this.isTopLevel = false;
		this.vhdlPins = new  ArrayList<VhdlPin>();
		this.vhdlGenerics = new ArrayList<VhdlGeneric>();
		this.pkgSuffix = "_pkg";
	}
	
	public VhdlComponent(){
		this.initVariablesConstructor();
	}//end constructor
	
	public VhdlComponent(String ref, String val, ArrayList<VhdlGeneric> fields){
		this.initVariablesConstructor();
		super.setValue(val);
		super.setReference(ref);
	}//end constructor

	public VhdlComponent(String ref, String val) {
		this.initVariablesConstructor();
		super.setValue(val);
		super.setReference(ref);
	}//end constructor

	/*//////////////////////////////////////////////////////////////////////////////////////////////////////
	 * Miscellaneous
	 *//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Set up if the component is an inline code component or shall be
	 * inserted via a VHDL package
	 * @param val true = inline code , false = use package
	 */
	public void setIsInlineComp(boolean val){
		this.isInlineComponent = val;
	}//end setIsInstantiatedByComponent()
	
	/**
	 * @return true = is inline code, false has component instantiation
	 */
	public boolean getIsInlineComp(){
		return this.isInlineComponent;
	}//end getIsInlineComp()
	
	/**
	 * Set up this component as a top level component. Only necessary if component shall be connected to VHDL
	 * physical pins. 
	 * @param isTopLevel
	 */
	public void setIsTopLevelComponent(boolean isTopLevel){this.isTopLevel = isTopLevel;}
	
	/**
	 * Get top level status value
	 * @return
	 */
	public boolean getIsTopLevel(){return this.isTopLevel;}
	
	/**
	 * Return the package suffix. This can be used in VHDL code generation to create the package name
	 * @return
	 */
	public String getPkgSuffix(){return this.pkgSuffix;}
	
	/**
	 * Set the package suffix of the component
	 * @param pkgSuffix
	 */
	public void setPkgSuffix(String pkgSuffix){this.pkgSuffix = pkgSuffix;}

	/**
	 * This method returns the library instantiation that must be put before the entity definition in the VHDL source code that shall be generated. 
	 * @return
	 */
	public VhdlData getVhdlPackageName(){
		VhdlData retVal = new VhdlData();
		String temp = this.getValue() + this.getPkgSuffix();
		retVal.setCode(temp);		
		return retVal;
	}//end getVhdlPackageName()

	/**
	 * Used to select if the current components VHDL data shall be printed in lower case or upper case letters
	 * @param upperCase
	 */
	public void setUpperCase(boolean upperCase){
		this.upperCase = upperCase;
	}//end setUpperCase()
	
	/**
	 * Find out if the vhdl code of this component shall be printed in upper or lower case letters.
	 * @return
	 */
	public boolean getUpperCase(){
		return this.upperCase;
	}//end getUpperCase()
	
	/**
	 * This method searches through the objects array list that contains all the pins of this object.
	 * @param pin number you want to look for
	 * @return Return the array list index of the pin if the pin number has been found. Otherwise return -1
	 */
	public int findVhdlPinByNo(int no){
		for(int i = 0; i < this.vhdlPins.size(); i++){
			if(this.vhdlPins.get(i).getNo() == no)return i;
		}
		return -1;
	}//end findVhdlPinByNo()
	
	/**
	 * Find a generic parameter by its name. 
	 * @param name
	 * @return generic object if it was found else null
	 */
	public VhdlGeneric findGenericByName(String name){
		for(int i = 0; i < this.vhdlGenerics.size(); i++){
			if(this.vhdlGenerics.get(i).getName().equalsIgnoreCase(name)){
				return this.vhdlGenerics.get(i);
			}//end if
		}//end for(i)
		return null;
	}//end for findGenericByName()
	
	/**
	 * Set fields and vhdlPin lists of this component. All array entries will be overwritten
	 * @param fields
	 * @param vhdlPins
	 */
	public void setGenericsPins(ArrayList<VhdlGeneric> vhdlGenerics, ArrayList<VhdlPin> vhdlPins){
	    this.vhdlGenerics = vhdlGenerics;
		this.vhdlPins = vhdlPins;
	}//end setFieldsPins()
	
	/*//////////////////////////////////////////////////////////////////////////////////////////////////////
	 * VHDL Data methods used to create VHDL code
	 *//////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is used to get the entity or component data for a VHDL file. It will return all 
	 * lines for the VHDL entity as an array list of VhdlData objects.
	 * @param upperCase True = print all VHDL keyword in upper case letters, false = print all VHDL keyword in lower case letters
	 * @param getComponent true = get component code for VHDL file, false = get entity code for VHDL file
	 * @param leadingWhiteSpaces The number of whitespace that will be put in front of every line of the entity/component to align code appropriately. 
	 * @return Return the entity/component data that contain all VHDL code lines generated.
	 * @throws WarningException 
	 * @throws CloseProgramException
	 */
	public ArrayList<VhdlData> getVhdlEntity(boolean getComponent, int leadingWhiteSpaces) throws WarningException /*throws CloseProgramException*/{
		ArrayList<VhdlData> generics = new ArrayList<VhdlData>();
		ArrayList<VhdlData> ports = new ArrayList<VhdlData>();
		
		ports = VhdlConstructor.createPorts(this.upperCase, this, this.getReference());	
		generics = VhdlConstructor.createGenerics(this.vhdlGenerics, this.upperCase);
		
		return VhdlConstructor.createEntity(this.getValue(), generics, ports, leadingWhiteSpaces, this.upperCase, getComponent);
	}//end getVhdlEntity()
	
	/**
	 * get the component instantiation code for VHDL source file generation
	 * @param upperCase True = print all VHDL keyword in upper case letters, false = print all VHDL keyword in lower case letters
	 * @param showUnusedInputs  true = print inputs in VHDL file even though they have not been assigned to any net. , false = do not display those inputs
	 * @return
	 */
	public ArrayList<VhdlData> getVhdlComponentIntantiation(boolean showUnusedInputs){
		return VhdlConstructor.createComponentInstantiation(this, this.upperCase, showUnusedInputs); 
	}//end getVhdlComponentInstantiation()
	
	/**
	 * Returns all signal definitions for a VHDL component
	 * @param upperCase
	 * @return
	 * @throws WarningException 
	 */
	public ArrayList<VhdlData> getVhdlSignalCode() throws WarningException{
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();
		VhdlData vhdlData = new VhdlData();
		String temp = "";
		int size = 0;
		
		//go trough all components and search there for pins that are set as signals. Those pins must be saved and returned
		for(int k = 0; k < this.getVhdlPins().size(); k++){
			if(this.vhdlPins.get(k).getIsSignal()){//add signal to return value
				vhdlData = new VhdlData();
				temp = VhdlKeywords.getKeyword(VhdlKeywords.KEY_SIGNAL_ID, this.upperCase) + " " + this.vhdlPins.get(k).getHDLsigName() + " : ";
				
				if(this.getVhdlPins().get(k).getIsVector()){
					try {
						size = Integer.parseInt(this.getVhdlPins().get(k).getSize()); 
						size--;
						temp = temp + VhdlKeywords.getDataType(this.vhdlPins.get(k).getDatatype(), upperCase, true, "VhdlComponent::")  + "(" + size + " " +  VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, this.upperCase) + " 0) := (" + VhdlKeywords.getKeyword(VhdlKeywords.KEY_OTHERS_ID, this.upperCase) + " => '0');";
					} catch (NumberFormatException e) {
						temp = temp + VhdlKeywords.getDataType(this.vhdlPins.get(k).getDatatype(), upperCase, true, "VhdlComponent::")  + "(" + this.getReference() + "_" + this.getVhdlPins().get(k).getSize() + " " +  VhdlKeywords.getKeyword(VhdlKeywords.KEY_DOWNTO_ID, this.upperCase) + " 0) := (" + VhdlKeywords.getKeyword(VhdlKeywords.KEY_OTHERS_ID, this.upperCase) + " => '0');";
					}//en try
					
				}else{
					temp = temp +VhdlKeywords.getDataType(this.vhdlPins.get(k).getDatatype(), upperCase, false, "VhdlComponent::")  + " := '0';";		
				}//end if
				
				vhdlData.setCode(temp, retVal.size());
				retVal.add(vhdlData);	
			}//end if
		}//end for (k)

		
		//align code properly
		//VHDLmanipulation manip = new VHDLmanipulation();
		//manip.setMaxCodeLength(VHDLmanipulation.findMaxLenghtCode(retVal));
		//retVal = manip.alignCodeBlock(retVal);
		
		return retVal;
	}//end getVhdlSignalCode()
	
	/*//////////////////////////////////////////////////////////////////////////////////////////////////////
	 * Pin parameter methods
	 *//////////////////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<VhdlPin> getVhdlPins() {return this.vhdlPins;}
	public int getVhdlPinCount() {return this.vhdlPins.size();}
	
	/**
	 * This method returns all ids from all pins that are defined as top level pins. All those pins must be 
	 * copied into the entity definition. 
	 * @return all pins that are defined as top level pins
	 */
	public ArrayList<Integer> getVhdlToplevelPinIds(){
		ArrayList<Integer> temp =  new ArrayList<Integer>();
		
		for(int i = 0; i < this.getVhdlPinCount(); i++){
			if(this.vhdlPins.get(i).getIsTopLevel()){
				temp.add(i);
			}//end if
		}//end for(i)
		return temp;
	}//end findPinIdByNo()
	
	/**
	 * This method returns all ids from all pins that are defined as top level pins. All those pins must be 
	 * copied into the entity definition. 
	 * @return all pins that are defined as top level pins
	 */
	public ArrayList<VhdlPin> getVhdlToplevelPins(){
		ArrayList<VhdlPin> temp =  new ArrayList<VhdlPin>();
		
		for(int i = 0; i < this.getVhdlPinCount(); i++){
			if(this.vhdlPins.get(i).getIsTopLevel()){
				temp.add(this.vhdlPins.get(i));
			}//end if
		}//end for(i)
		return temp;
	}//end findPinIdByNo()
	
	
	/**
	 * Disable all pins from the components signal list by setting all pin isSignal attributes to false;
	 */
	public void clearAllSignals(){
		for(int i = 0; i < this.getVhdlPins().size(); i++){
			this.vhdlPins.get(i).setIsSignal(false);
		}//end for(i)
	}//end clearAllSignal()
	
	/**
	 * Add a complete list of vhdl pins to the object. All currently assigned pins will
	 * be deleted first. Method assures that all vhdlPin elements get a new ID for this component
	 * @param vhdlPin
	 */
	public void addVhdlPin(ArrayList<VhdlPin> vhdlPin) {
		this.vhdlPins = new ArrayList<VhdlPin>();
		//Make sure that all pins become a unique id 
		for(int i = 0; i < vhdlPin.size(); i++){
			this.vhdlPins.add(new VhdlPin(vhdlPin.get(i)));
		}//end for(i)
	}//end addVhdlPin()
	
	/**
	 * Add a single vhdlPin to the vhdlComponent
	 * @param vhdlPin
	 */
	public void addVhdlPin(VhdlPin vhdlPin) {
		this.vhdlPins.add(vhdlPin);
	}//end addVhdlPin()
	
	
	/**
	 * Return the index values of those pins that belong to this component that are signals that need to
	 * be defined in VHDL
	 * @return
	 */
	public ArrayList<Integer> getVhdlsignalPinIDs(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i = 0; i < this.getVhdlPinCount(); i++){
			if(this.vhdlPins.get(i).getIsSignal()){
				temp.add(i);
			}//end if			
		}//end for(i)
		
		return temp;
	}//end getAllPinIndexHDLSignal()
	
	
	/*//////////////////////////////////////////////////////////////////////////////////////////////////////
	 * Generic parameter methods
	 *//////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Get the list of all field that have been assigned to this component.
	 * @return
	 */
	public ArrayList<VhdlGeneric> getVhdlGenerics(){return this.vhdlGenerics;}
	public int getVhdlGenericsCount(){return this.vhdlGenerics.size();};
	
	public void addVhdlGenerics(ArrayList<VhdlGeneric> generics){
		this.getVhdlGenerics().addAll(generics);
	}//end addVhdlGenerics
	/**
	 * Returns if current component has generic parameters.
	 * @return
	 */
	public boolean hasVhdlGenerics(){
		if(this.getVhdlGenericsCount() > 0){return true;}
		else {return false;}
	}//end hasVhdlGenerics()
	
}//end class
