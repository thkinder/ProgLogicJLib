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
 * Contains method to parse a KiCad Netlist file
 */
package com.proglogicjlib.kicad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.proglogicjlib.data.Component;
import com.proglogicjlib.data.Pin;
import com.proglogicjlib.misc.CloseProgramException;
import com.proglogicjlib.misc.InfoAndMessages;
import com.proglogicjlib.misc.WarningException;

public class KiCadParseNetlist {
	
	private static String NETLIST_NOT_FOUND_ID = "(KiCadParser:001)";
	private static String NETLIST_NOT_FOUND = NETLIST_NOT_FOUND_ID + "Netlist file could not be found";
	
	private static String COMPONENT_GENERIC_MISSING_CLOSING_BRACKET_ID = "(COMPS:001)";
	private static String COMPONENT_GENERIC_MISSING_CLOSING_BRACKET = COMPONENT_GENERIC_MISSING_CLOSING_BRACKET_ID + "Generic parameter is missing a closing bracket. Programm will be closed.";
	
	
	public static String FIELD = "(field ";
	public static String LIBPART = "(libpart ";
	public static String PART = "(part ";
	public static String NAME = "(name ";
	public static String PIN = "(pin ";
	public static String NUM = "(num ";
	public static String TYPE = "(type ";
	public static String NETS = "(nets ";
	public static String NET = "(net ";
	public static String NODE = "(node ";
	public static String REF = "(ref ";
	public static String COMP = "(comp ";
	public static String VALUE = "(value ";
	public static String SOURCE = "(source ";
	public static String SCH = ".sch";
	
	public static String INPUT = "input";
	public static String OUTPUT = "output";
	public static String INOUT = "BiDi";
	
	
	/**
	 * This Method takes a net list file and separates the the netlist content into blocks that are necessary
	 * the further processing. 
	 * <ol>
	 * <li>Block Index 0 = design</li>
	 * <li>Block Index 1 = components</li>
	 * <li>Block Index 2 = libparts</li>
	 * <li>Block Index 3 = libraries</li>
	 * <li>Block Index 4 = nets</li>
	 * </ol>
	 * @param file
	 * @return
	 */
	public static int INDEX_DESIGN = 0;
	public static int INDEX_COMPONENTS = 1;
	public static int INDEX_LIBPARTS = 2;
	public static int INDEX_LIBRARIES = 3;
	public static int INDEX_NETS = 4;
	
	/**
	 * This method extracts netlist parameter in the following blocks 
	 * <ol>
	 * <li>INDEX_DESIG: Contains the design block of the netlist file</li>
	 * <li>INDEX_COMPONENTS: Contains component block of the netlist file</li>
	 * <li>INDEX_DESIG: Contains libparts block of the netlist file</li>
	 * <li>INDEX_DESIG: Contains lirary block of the netlist file</li>
	 * <li>INDEX_DESIG: Contains the nets block of the netlist file</li>
	 * </ol>
	 * @param file KiCad netlist file "*.net"
	 * @return
	 * @throws CloseProgramException 
	 */
    public static ArrayList<Block> getBlocks(File file) throws CloseProgramException{
    	
		Scanner scanner = null;
		int open, close,res,start, end = 0;
		int lineNo = 0;
		boolean finished = false;
		boolean firstCall = true;
		Block block = new Block();
		ArrayList<Block> blockArr = new ArrayList<Block>();
		
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			InfoAndMessages.showError(NETLIST_NOT_FOUND);
			e.printStackTrace();
		}
		

		//initialize variables
		lineNo = 0;
		String line = "";
		String tempString = "";
		block.setText("");
		
		//start block extraction. Count open and close brackets and determine with that the beginning and the end of a block.
		//The first line of the net list is always ignored.
		while (scanner.hasNextLine()) {
			if(firstCall){
				line = scanner.nextLine();
				lineNo = 1; //always start from second line
				firstCall = false;
			}//end if
			
			//extract blocks
			start = lineNo;
			res = 0;
			tempString = "";
			while(!finished && scanner.hasNextLine()){
				
				line = scanner.nextLine();
				tempString = tempString + line;
				
				open = countChar(line, '(') + res;
				close = countChar(line, ')');
				res = open - close;

				if(res <= 0){
					finished = true;
					end = lineNo;
				}
				
				lineNo++;
			}//end while
			finished = false;
			block.setEnd(end);
			block.setStart(start);
			block.setText(tempString);
			blockArr.add(block);
			block = new Block();
			
		}//end while
		scanner.close();
		return blockArr;
    }//end getBlocks()
    
	
	/**
	 * Count the occurrence of a char in a string
	 * @param line String for the search
	 * @param symbol char to search for
	 * @return count
	 */
	public static int countChar(String line, char symbol){
		int counter = 0;
		for( int i = 0; i < line.length(); i++ ) {
		    if( line.charAt(i) == symbol ) {
		        counter++;
		    }//end if
		}//end for(i)
		return counter;	
	}//end countChar()
	
	/**
	 * 
	 * @param designBlock Design attribute block of KiCad netlist file
	 * @return schematic name extracted from attribute design( (source PATH/SchemName.sch). Returns only SchemName, if not possible return null
	 */
	public static String getSchematicName(Block designBlock){
		String line = designBlock.getText();
		int startIndex = 0;
		int endIndex = 0;
		
		startIndex = line.indexOf(SOURCE);
		
		if(startIndex < 0) return null;
		 
		endIndex = findClosingBracket(startIndex, line);
		String temp = line.substring(startIndex, endIndex);
		
		startIndex = temp.lastIndexOf("\\");
		if(startIndex < 0){
			startIndex = temp.lastIndexOf("/");
			if(startIndex < 0 )return null;
		}//end if
		
		endIndex = temp.indexOf(SCH, startIndex);
		
		if(endIndex < 0) return null;
		
		return temp.substring(startIndex+1, endIndex);
	}//end getSchematicName()
	
	/**
	 * Find the next closing bracket from the start index
	 * @param startIndex
	 * @param line
	 * @return
	 */
	public static int findClosingBracket(int startIndex, String line){
		for(int i = startIndex; i < line.length(); i++){
			if(line.charAt(i)== ')'){//found closing bracket to the field
				return i;
			}
		}
		return -1;	
    }//end findClosingBracket()


	/**
	 * This method extracts the reference,value and field key words out of the net list component block section.
	 * @param block
	 * @return null = error; != null ok!
	 */
	public static ArrayList<KiCadDataHelper> processComponentsBlock(Block componentsBlock){
		ArrayList<KiCadDataHelper> retList = new ArrayList<KiCadDataHelper>();
		KiCadDataHelper helper = new KiCadDataHelper();
		KiCadField field = new KiCadField();
		
		int index = 0;
		int endIndex = 0;
		int indexName= 0;
		int indexComp = 0;
		int indexCloseComp = 0;
		String line = componentsBlock.getText();
		String ref = "";
		String value = "";
		
		int indexField =  0;

		String fieldName = "";
		String fieldValue = "";

		indexComp = line.indexOf(COMP);
		
		while(indexComp >= 0){//find all (comp definitions
			helper = new KiCadDataHelper();//make sure that always a new object is created
			
			indexCloseComp = findBlockEnd(indexComp, line); //get the end of the current comp definition
			//get the reference value and add it to component object
			index = line.indexOf(REF, indexComp);
			if(index < 0) return null;	//reference not found
		    endIndex = findBlockEnd(index, line);//line.indexOf(')', index);
			ref = line.substring(index+REF.length(), endIndex);
			ref = ref.replace('"', ' ').trim();
			helper.setRefference(ref);
			
			//get the value attribute
			index = line.indexOf(VALUE, endIndex+1);
			if(index < 0) return null;	//value not found
			endIndex = findBlockEnd(index, line);
			value = line.substring(index+VALUE.length(), endIndex);
			value = value.replace('"', ' ').trim();
			helper.setValue(value);

			//extract all field values. Those fields can contain generic parameters for instance
			indexField = line.indexOf(FIELD, endIndex);
			while(indexField >= 0 && indexField < indexCloseComp){			
				//name
				indexName = line.indexOf(NAME, indexField);
				endIndex = findBlockEnd(indexName, line);//findClosingBracket(indexName, line);
				fieldName = line.substring(indexName+NAME.length(), endIndex).replaceAll("\"", "").trim();//name attribute of current selected field
				
				//value
				indexName = endIndex;
				endIndex = findBlockEnd(indexField, line );
				fieldValue = line.substring(indexName+2, endIndex).trim();//.replaceAll("\"", "").trim();//value attribute of current selected field
				int tempLenght = fieldValue.length();
				fieldValue = fieldValue.replaceAll("^\"", "");
				
				if(tempLenght > fieldValue.length()){//remove last "
					fieldValue = fieldValue.replaceAll("\"$", "");
				}//end if
				
				field.setName(fieldName);
				field.setValue(fieldValue);
				helper.addField(field);
				indexField = line.indexOf(FIELD, endIndex+2);
				field = new KiCadField();
				
			}//end while
			
			indexComp = line.indexOf(COMP, indexComp+1);
			retList.add(helper);
			
		}//end while indexComp
		
		return retList;
	}//end extractComponents()
	
	/**
	 * This methods finds the end of the block from a specific start index
	 * @param startIndex
	 * @param line
	 * @return
	 */
	public static int findBlockEnd(int startIndex, String line){
		int retVal = -1;
		int open = 0, close = 0, res = 0;
		boolean foundOpening = false;
		
		if(startIndex+1 >= line.length())return retVal;
		
		for(int i = startIndex; i < line.length(); i++){
			if(line.charAt(i)== '('){
				if(!foundOpening) open++;
				
			}else if(line.charAt(i)==')'){
				if(!foundOpening) close++;
			}else if(line.charAt(i)=='"'){//do not count ( ) in text field that contain " " 
				if(foundOpening)foundOpening = false;
				else foundOpening = true;
			}//end if
			res = open - close;
			if(res == 0){
				retVal = i;
				return retVal;
			}//end if
		}//end for

		return retVal;
	}//end findBlockEnd()

	

	/**
	 * This method extracts the pin information from the netlist block libparts
	 * and saves the results in the local array list of components. It also sets up the size of
	 * the pins that are defined as vectors. 
	 * @param libpart libpart section of the KiCad netlist
	 * @param upperCase use upper or lower case vhld keywords
	 * @throws CloseProgramException 
	 * @throws WarningException 
	 */	

	public static ArrayList<KiCadLibparts> getPins(Block libpart, boolean upperCase) throws CloseProgramException {

		//first extract all field variables
		String line = libpart.getText();
		String temp = "";
		int indexLibPart =  0;
		int indexPart =  0;
		int indexName =  0;
		int indexPin =  0;
		int indexClose = 0;
		int indexCloseLibPart = 0;

		Pin pin = new Pin();
		int pinNo = 0;
		String pinName = "";
		ArrayList<KiCadLibparts> libparts = new ArrayList<KiCadLibparts>();
		ArrayList<Pin> pins = new ArrayList<Pin>();
		String value = "";

		

		indexLibPart = line.indexOf(LIBPART); 
		while(indexLibPart >= 0){//Pins and field of one component
			//extract part1
			indexCloseLibPart = findBlockEnd(indexLibPart, line);
			indexPart = line.indexOf(PART,indexLibPart);
			if(indexPart >= 0){
				indexClose = findBlockEnd(indexPart, line);
				
				//Get reference value
				temp = line.substring(indexPart+PART.length(), indexClose).replace('"', ' ').trim();//temp contains here the component name to which the following information belong to
				value = temp;
				
					//find pins
					pin = new Pin();
					indexPin = line.indexOf(PIN, indexPart);//find first pin after libparts block has been opened
					while(indexPin >= 0 && indexPin <= indexCloseLibPart ){//extract all pin components
						//number
						indexName = line.indexOf(NUM, indexPin);//find first pin number
						indexClose = findBlockEnd(indexName, line);
						
						try {
							pinNo = Integer.parseInt(line.substring(indexName+NUM.length(), indexClose));
						} catch (NumberFormatException e) {
							pinNo = -1;
						}
			
						//name
						indexName = line.indexOf(NAME, indexClose);
						indexClose = findBlockEnd(indexName, line);
						//temp = line.substring(indexName+NAME.length(), indexClose);//name attribute of current selected field
						pinName = line.substring(indexName+NAME.length(), indexClose);
						pinName = pinName.replaceAll("\"", "").trim();
						
						//extract datatype of signal
						//String[] datatype = Component.getParameter(pinName, '(', ')');
						
						
						//if(datatype[0] != null && datatype[0].equals("err")){
						//	InfoAndMessages.showError(COMPONENT_GENERIC_MISSING_CLOSING_BRACKET + " See Pin =" + pinName);
						//}
						
						
						String[] pinSizeArr = Component.getParameter(pinName, '[', ']');
						if(pinSizeArr[0] != null && pinSizeArr[0].equals("err")){
							InfoAndMessages.showError(COMPONENT_GENERIC_MISSING_CLOSING_BRACKET + " See Pin =" + pinName);
						}

						pinName = pinSizeArr[1]; //get the name without size values
						
						if(pinSizeArr[0] != null){//pin is a vector because it has a size defined
							//pin.setIsVector(true);
							pin.setSize(pinSizeArr[0]);
						}else{
							pin.setSize("0");
						}//end if
						
						//type
						indexName = line.indexOf(TYPE, indexClose);
						indexClose = findBlockEnd(indexName, line);
						temp = line.substring(indexName+TYPE.length(), indexClose);//value attribute of current selected field
						
						//save pin in components that all have the same name
						pin.setName(pinName);
						pin.setNo(pinNo);
						
						if(temp.equals(INPUT)){
							pin.setType(Pin.TYPE_IN);
						}else if(temp.equals(OUTPUT)){
							pin.setType(Pin.TYPE_OUT);
						}else if(temp.equals(INOUT)){
							pin.setType(Pin.TYPE_INOUT);
						}else{
							pin.setType(Pin.UKNOWN);
						}//end if
							
						pins.add(pin);
						indexPin = line.indexOf(PIN, indexClose+1);
						pin = new Pin();
						
					}//end while
			}//end if
			
			//search for another components libpart
			indexLibPart = line.indexOf(LIBPART, indexClose+1); 
			libparts.add(new KiCadLibparts(value, pins));
			pins = new ArrayList<Pin>();
		}//end while
		return libparts;
	}//end setpinField(Block libpart )

	/**
	 * This method extracts all nets from the KiCad File and returns an Array list of KiCadNet objects containing all net infos
	 * @param nets
	 * @return
	 */
	public static ArrayList<KiCadNet> getNets(Block nets){
		int indexNode = 0;
		int indexOpen = 0;
		int indexClose = 0;
		int indexNet = 0;
		int indexEndNet = 0;
	
		int pinNo = -1;
		
		String temp = "";
		String reference = "";
		String line = nets.getText();
		
		ArrayList<KiCadNet> netList = new ArrayList<KiCadNet>();
		KiCadNet tempNet = new KiCadNet();
		
		//net
		indexNet = line.indexOf(NET);
		while(indexNet >= 0){//iterate over all nets

			indexEndNet = findBlockEnd(indexNet, line);
			indexNode = line.indexOf(NODE, indexNet);
			
			//node processing
			while(indexNode >= 0 && indexNode < indexEndNet ){//Iterate over all nodes of a net
				

				//get reference 
				indexOpen = line.indexOf(REF, indexNode);
				indexClose = findClosingBracket(indexOpen, line);
				reference = line.substring(indexOpen+REF.length(), indexClose);

				
				//get pin number 
				indexOpen = line.indexOf(PIN, indexClose);
				indexClose = findClosingBracket(indexOpen, line);
				temp = line.substring(indexOpen + PIN.length(), indexClose);
				pinNo = Integer.parseInt(temp);
				
				tempNet.addReferencePin(reference, pinNo);
				indexNode = line.indexOf(NODE, indexClose);		
					
			}//end while
			
			netList.add(tempNet);
			tempNet = new KiCadNet();
			indexNet = line.indexOf(NET, indexEndNet); 
		}//end while
		
		return netList;
	}//end getNets()
	
	

	
	
}//end class
