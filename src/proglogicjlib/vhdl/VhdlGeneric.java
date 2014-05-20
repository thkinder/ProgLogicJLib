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
 * Contains information and methods about VHDL gneric parameter that each 
 * VHDL component can have
 */
package com.proglogicjlib.vhdl;

public class VhdlGeneric {
	private  String name;
	private String value;
	private String datatype;
	private String size;
	private String compReference;
	private boolean isActive;
	
	public VhdlGeneric(){
		this.name = VhdlGeneric.class.getSimpleName() + ":: name not set";
		this.value =VhdlGeneric.class.getSimpleName() + ":: value not set";
		this.datatype = VhdlGeneric.class.getSimpleName() + ":: dataType not set";
		this.size = VhdlGeneric.class.getSimpleName() + ":: size not set";
		this.isActive = true;
	}//end constructor
	
	public VhdlGeneric(String name, String value, String datatype, String size){
		this.name = name;
		this.value = value;
		this.datatype = datatype;
		this.size = size;
		this.isActive = true;
	}//end constructor
	

	public void setGeneric(String name, String value, String datatype, String size){
		this.name = name;
		this.value = value;
		this.datatype = datatype;
		this.size = size;
	}//end setGeneric()

	public void setSize(String size){this.size = size;}
	public void setDatatype(String datatype){this.datatype = datatype;}
	public void setName(String name){this.name = name;}
	public void isActive(boolean isActive){this.isActive = isActive;}
	
	public String getName(){
		return this.name;
	}

    public String getUniqueName(){
    	if(this.compReference != null && this.compReference != ""){
			return this.compReference + "_" + this.name;
		}else{
			return null;
		}//end if
    }
	public String getValue(){return this.value;}
	public String getDatatype(){return this.datatype;}
	public String getSize(){return this.size;}
	public boolean isActive(){return this.isActive;}
	public void setCompRef(String ref){
		this.compReference = ref;
	}
	
	/**
	 * Indicates if the generic parameter is a vector. 
	 * A generic is a vector type if its size is greater than 0
	 * @return
	 */
	public boolean isVector(){
		int temp = 0;

		if(this.size == null || this.size.equals(""))return false;
		
		try {
			temp = Integer.parseInt(this.size);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			temp = 0;
			e.printStackTrace();
		}//end try
		
		if(temp >= 1) return true;
		else return false;		
	}//end isVector()
	
	
	
}//end class


