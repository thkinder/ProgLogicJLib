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
 * This class contains methods for creating vhdl files
 * 
 */
package com.proglogicjlib.vhdl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.proglogicjlib.misc.WarningException;


public class VhdlFile {

	public static String VHDL_HEADER_COPYRIGHT = "-----------------------------------------------------------------------------------------\n"
												+ "--Information and Copyright\n"
												+ "-----------------------------------------------------------------------------------------\n"
												+ "-- License agreement: (BSD 3 License)\n"
												+ "-- Copyright (c) 2013, Thomas Kinder (info@thinkingsand.com), ThinkingSand\n"
												+ "-- All rights reserved.\n"
												+ "\n"
												+ "-- Redistribution and use in source and binary forms, with or without modification, are \n"
												+ "-- permitted provided that the following conditions are met:\n"
												+ "-- \n"
												+ "-- * Redistributions of source code must retain the above copyright notice, this list of \n"
												+ "--  conditions and the following disclaimer.\n"
												+ "--\n"
												+ "-- * Redistributions in binary form must reproduce the above copyright notice, this list\n"
												+ "--   of conditions and the following disclaimer in the documentation and/or other \n"
												+ "--   materials provided with the distribution. \n"
												+ "--\n"
												+ "-- * Neither the name of the <ORGANIZATION> nor the names of its contributors may be \n"
												+ "--   used to endorse or promote products derived from this software without specific \n"
												+ "--   prior written permission.\n"
												+ "-- \n"
												+ "-- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY \n"
												+ "-- EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES \n"
												+ "-- OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT \n"
												+ "-- SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, \n"
												+ "-- INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED \n"
												+ "-- TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR \n"
												+ "-- BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN \n"
												+ "-- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN \n"
												+ "-- ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH \n"
												+ "-- DAMAGE.\n"
												+ " \n";

	public static String VHDL_TEMPLATE_PART_A;
	private static String VHDL_TEMPLATE_PART_A_PATH = "templates/vhdl_template_part_a.txt";

	public static String VHDL_TEMPLATE_PART_B;
	private static String VHDL_TEMPLATE_PART_B_PATH = "templates/vhdl_template_part_b.txt";
	
	public static String VHDL_TEMPLATE_PART_C;
	private static String VHDL_TEMPLATE_PART_C_PATH = "templates/vhdl_template_part_c.txt";
	
	
	
	/**
	 * Create a string containing the commented section design TODO : Read template out of file instead of using a static design
	 * @param name
	 * @param level
	 * @return
	 */
	public static String getCommentSection(String name, int level){
		String temp = VHDLmanipulation.whiteSpaceCreator(level) + "-----------------------------------------------------------------------------------------\n";
	    temp = temp + VHDLmanipulation.whiteSpaceCreator(level) +"-- " + name + "\n";
		temp = temp + VHDLmanipulation.whiteSpaceCreator(level) + "-----------------------------------------------------------------------------------------\n";
        return temp;
	}//
	
	/**
	 * Constructor for object initialization and import of template files
	 */
	public VhdlFile(){
		//initialize arrays
		VhdlFile.VHDL_TEMPLATE_PART_A = readFileConvertToString(new File(VHDL_TEMPLATE_PART_A_PATH));
		VhdlFile.VHDL_TEMPLATE_PART_B = readFileConvertToString(new File(VHDL_TEMPLATE_PART_B_PATH));
		VhdlFile.VHDL_TEMPLATE_PART_C = readFileConvertToString(new File(VHDL_TEMPLATE_PART_C_PATH));

	}
	

	
	/**
	 * Convert data into a top level vhdl file. 
	 * @param entity     Data of entity
	 * @param entityName Name of entity
	 * @param path Path of file were result shall be saved in
	 * @param upperCase true = all vhdl keywords printed upper case
	 * @param components  components that are instantiated in the vhdl file, if not existing use null. Set to null when to print component vhdl files and not the top level file
	 * @param showUnusedInputs
	 * @throws WarningException 
	 */
	public String componentToVhdlFile( String entityName, String pkgSuffix,  String path, boolean upperCase, ArrayList<VhdlComponent> vhdlComponents, boolean showUnusedInputs) throws WarningException{
		
		ArrayList<VhdlData> entity = VHDLmanipulation.getTopLevelEntity(vhdlComponents, entityName, upperCase, false, 0);
		ArrayList<VhdlData> component = VHDLmanipulation.getTopLevelEntity(vhdlComponents, entityName, upperCase, true, 0);
		
		//Create library code  VHDL file; all VhdlComponents that are defined as non inline component must need a package declaration
        ArrayList<VhdlData> libraries = new ArrayList<VhdlData>();
        for(int i = 0; i < vhdlComponents.size(); i++){
        	if(!vhdlComponents.get(i).getIsInlineComp()){//add library when it is not inline code
        		libraries.add(vhdlComponents.get(i).getVhdlPackageName());
        	}//end if
        }//end for(i)
        
        //remove libraries that might appear multiple times because vhdlComponents list contains multiple objects of one type
        ArrayList<VhdlData> resultLibs = new ArrayList<VhdlData>();
        Iterator<VhdlData> iterator = libraries.iterator();

        while(iterator.hasNext()){
        	VhdlData o = (VhdlData) iterator.next();
        	if(!resultLibs.contains(o)){resultLibs.add(o);}//end if
        }//end while

        
        //Get all signals for VHDL file
		 ArrayList<VhdlData> tempSignals = new ArrayList<VhdlData>();
		for(int i = 0; i < vhdlComponents.size(); i++){
			tempSignals.addAll(vhdlComponents.get(i).getVhdlSignalCode());	
		}//end for (i)
		
		
        //remove libraries that might appear multiple times because vhdlComponents list contains multiple objects of one type
		ArrayList<VhdlData> signals = new ArrayList<VhdlData>();
        Iterator<VhdlData> itter = tempSignals.iterator();

        while(itter.hasNext()){
        	VhdlData o = (VhdlData) itter.next();
        	if(!signals.contains(o)){signals.add(o);}//end if
        }//end while
        
        //align signal code nicely
        VHDLmanipulation manip = new VHDLmanipulation();
      	manip.setMaxCodeLength(VHDLmanipulation.findMaxLenghtCode(signals));
		signals = manip.alignCodeBlock(signals);
		
		File file = new File(path);
		boolean createFile = false;
		
		if(!file.isDirectory()){
			file.mkdirs();
		}
		
		file = new File(path+"/"+  entityName+".vhd");
		//Check if file exists
		if(file.exists()){
			int type = JOptionPane.showConfirmDialog(null, "File \"" + entityName+".vhd" + "\" already exists. Do you want to overwrite the file?");
			if(type != JOptionPane.YES_OPTION){//close program
				JOptionPane.showMessageDialog(null, "File has not been saved!");
				createFile = false;
				return null;
			}else{
				createFile = true;
			}//end if NO selection
		}else{
			createFile = true;
		}
		
		if(createFile){
			try {
				Writer output = null;
				ArrayList<VhdlData> vhdlData = entity;
				output = new BufferedWriter(new FileWriter(file));
				
				//Add header and copyright statements
				output.write(VhdlFile.VHDL_HEADER_COPYRIGHT);
				
				//add VHDL_TEMPLATE_PART_A
				output.write(VhdlFile.VHDL_TEMPLATE_PART_A);
				
				/*-------------------------------------------------------------*/
				/*ADD package here*/
				/*-------------------------------------------------------------*/
				output.write(VhdlKeywords.getKeyword(VhdlKeywords.KEY_PACKAGEC_ID, upperCase) + " " + entityName + pkgSuffix.trim() + " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_IS_ID, upperCase)+ "\n");
				for(int i = 0; i < component.size(); i++){
					output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2)  + component.get(i).getCode() + "\n");	
				}//end for
				output.write(VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, upperCase) + " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_PACKAGEC_ID, upperCase)+ ";\n");
				
				
					
				/*-------------------------------------------------------------*/
				/*ADD Libraries here*/
				/*-------------------------------------------------------------*/
				output.write(VhdlFile.getCommentSection("Libraries", VhdlKeywords.LEVEL_1 ));
				output.write(VhdlFile.VHDL_TEMPLATE_PART_B);
				output.write("\n");
				output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_LIBRARY_ID, upperCase) + " work;\n" );
				
				if(vhdlComponents != null){
					for(int i = 0; i < resultLibs.size(); i++){
						output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_USE_ID, upperCase) + " work." +
								resultLibs.get(i).getCode() +  "." + VhdlKeywords.getKeyword(VhdlKeywords.KEY_ALL_ID, upperCase) + ";\n");
						
						
						/*if(!components.getComponents().get(components.getLibraryNames().get(i)).getIsTopLevel()){
							output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1) + VhdlKeywords.getKeyword(VhdlKeywords.KEY_USE_ID, upperCase) + " work." +
									 components.getComponents().get(components.getLibraryNames().get(i)).getVhdlPackageName().getCode() + "." + VhdlKeywords.getKeyword(VhdlKeywords.KEY_ALL_ID, upperCase) + ";\n");
						}*/
					}//end for(i)
					output.write("\n");
				}//end if component != null
				
				
				/*-------------------------------------------------------------*/
				/*ADD Entity here*/
				/*-------------------------------------------------------------*/
				output.write(VhdlFile.getCommentSection("Entity", VhdlKeywords.LEVEL_1 ));
				
				
				
				for(int i = 0; i < entity.size(); i++){
					output.write(entity.get(i).getCode() + "\n");	
				}//end for
			
				
				/*-------------------------------------------------------------*/
				//add architecture
				/*-------------------------------------------------------------*/
				output.write("\n"+VhdlFile.VHDL_TEMPLATE_PART_C);
				
				output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_1)+ VhdlKeywords.getKeyword(VhdlKeywords.KEY_ARCHITECTURE_ID, upperCase) 
						     + " arch " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_OF_ID, upperCase) + " " + entityName+ " " + VhdlKeywords.getKeyword(VhdlKeywords.KEY_IS_ID, upperCase));
				output.write("\n\n");
				
				/*-------------------------------------------------------------*/
				/*add signals*/
				/*-------------------------------------------------------------*/
				vhdlData = new ArrayList<VhdlData>();
				if(vhdlComponents != null){
					for(int i = 0; i < signals.size(); i++){
						output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + signals.get(i).getCode() + "\n");
					}//end for (i)
					output.write("\n");
				}//end if component != null
				
				output.write(VhdlKeywords.getKeyword(VhdlKeywords.KEY_BEGIN_ID, upperCase)+ "\n");
					
				if(vhdlComponents != null){
					/*-------------------------------------------------------------*/
					/*add component instantiations here*/
					/*-------------------------------------------------------------*/
					for(int i = 0; i < vhdlComponents.size(); i++){
						vhdlData = vhdlComponents.get(i).getVhdlComponentIntantiation(showUnusedInputs);
						if(vhdlData != null){
							output.write("\n");
							output.write(VhdlFile.getCommentSection("Component " + vhdlComponents.get(i).getReference(), VhdlKeywords.LEVEL_2 ));
						
							for(int k = 0; k < vhdlData.size(); k++){
								output.write(VHDLmanipulation.whiteSpaceCreator(VhdlKeywords.LEVEL_2) + vhdlData.get(k).getCode() +  vhdlData.get(k).getComment() + "\n");	
							}//end for
						}//end if is TopLevel
					}//end for (i)
				}//end if component != null
				
				output.write("\n");
				output.write(VhdlKeywords.getKeyword(VhdlKeywords.KEY_END_ID, upperCase)+ " arch;\n");
	
				/*FILE END*/
				output.close();
				return file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}//end createVhdlFile(

	
	/**
	 * This method reads the specified file and return its content as a string.
	 * @param file
	 * @return
	 */
	public static String readFileConvertToString(File file){
		String retVal = "";

		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			
			while((line = br.readLine()) != null){
				retVal += line + "\n";	
			}//end while
			
			br.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return retVal;
		
	}//end readFileConverterToString()
	
	/**
	 * This method reads the specified file and return its content as a string.
	 * @param file
	 * @return
	 */
	public static ArrayList<VhdlData> readFileConvertToVhdlData(File file, boolean removeEmptyLines, boolean removeComments){
		ArrayList<VhdlData> retVal = new ArrayList<VhdlData>();

		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			int lineCount = 0;
			while((line = br.readLine()) != null){
				
				//retVal += line + "\n";	
				
				VhdlData data = new VhdlData();
				int indexComment = line.indexOf("--");
				
				if(indexComment >= 0){
					if(!removeComments)data.setComment(VHDLmanipulation.removeMultiWhiteSpace(line.substring(indexComment).trim()), lineCount);
					data.setCode(VHDLmanipulation.removeMultiWhiteSpace(line.substring(0, indexComment).trim()), lineCount);
				}else{
					if(!removeComments)data.setComment("", lineCount);
					data.setCode(VHDLmanipulation.removeMultiWhiteSpace(line.trim()), lineCount);
				}//end if
				
				if(removeEmptyLines){
					if(data.getCode() != null && !data.getCode().equals(" ") && !data.getCode().equals("") ){//only add non empty lines
						retVal.add(data);	
						lineCount++;
					}//end if
					
				}else{
					retVal.add(data);
					lineCount++;
				}//end if

			}//end while
			
			br.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return retVal;
		
	}//end readFileConverterToString()

	
	

}//end class
