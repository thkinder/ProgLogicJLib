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
package com.proglogicjlib.kicad;

import java.util.ArrayList;

public class KiCadDataHelper {
	private String value;
	private String refference;
	private ArrayList<KiCadField> fields;
	
	public KiCadDataHelper(){
		this.fields = new ArrayList<KiCadField>();
	}
	
	public void setValue(String value){this.value = value;}
	public void setRefference(String ref){this.refference = ref;}
	public void addField(KiCadField field){this.fields.add(field);}
	
	public String getValue(){return this.value;}
	public String getRefference(){return this.refference;}
	public ArrayList<KiCadField> getFields(){return this.fields;}
	
	public boolean hasFields(){
		if(this.fields.size() > 0){ return true;}
		else{return false;}
	}//end has fields
	
	/**
	 * This method goes trough all fields contained in the object and returns the one 
	 * that has the same name than the passed argument
	 * @param name name of the field that shall be returned
	 * @return field with the desired name or null if field does not exist
	 */
	public KiCadField getFieldByName(String name){
		for(int i = 0; i < this.fields.size(); i++){
			if(this.fields.get(i).getName().equalsIgnoreCase(name)){return this.fields.get(i);}
		}//end for
		
		return null;
	}//end getFieldByName()

}//end class
