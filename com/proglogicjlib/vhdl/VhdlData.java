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
package com.proglogicjlib.vhdl;
/**
 * This class represents a line of a VHDL code file
 * @author Thomas Kinder, thomas.kinder@thinkingsand.com
 *
 */
public class VhdlData {
	private String code;
	private Integer codeIndex; //TODO : is this attribute really necessary?
	private String comment;
	private Integer commentIndex; 
	
	public VhdlData(){
		this.code = "";
		this.comment = "";
		this.codeIndex = -1;
		this.commentIndex = -1;
	}
	
	/**
	 * The the code content of this object
	 * @param codeLine VHDL code line
	 * @param index line where this VHDL line is staying in the original source code
	 */
	public void setCode(String codeLine, int index){
		this.code = codeLine;
		this.codeIndex=index;
	} //end setCode()
	
	/**
	 * Only set the code of the object
	 * @param codeLine
	 */
	public void setCode(String codeLine){ 
		this.code = codeLine;
	} //end setCode()
	
	/**
	 * The the comment content of this object
	 * @param commentLine VHDL comment line
	 * @param index line where this VHDL line is staying in the original source code
	 */
	public void setComment(String commentLine, int index){
		this.comment=commentLine;
		this.commentIndex=index;	
	}//end setComment()
	
	/**
	 * Just set the comment data of this object
	 * @param commentLine VHDL comment line
	 */
	public void setComment(String commentLine){
		this.comment=commentLine;	
	}//end setComment()
	
	/**
	 * This methods returns the code of this object
	 * @return
	 */
	public String getCode(){
		return this.code;
	}//end getCode()
	
	/**
	 * This method returns the codeIndex of this object
	 * @return
	 */
	public int getCodeIndex(){
		return this.codeIndex;
	}//end getCodeIndex()
	
	/**
	 * This method returns the comment of this object
	 * @return
	 */
	public String getComment(){
		return this.comment;
	}//end getComment()
	
	/**
	 * This method returns the commentIndex of this object
	 * @return
	 */
	public int getCommentIndex(){
		return this.commentIndex;
	}//end getCommentIndex()
	
	@Override
	public boolean equals(Object o){
		if(o == this)return false;
		
		if(!(o instanceof VhdlData))return false;
		
		VhdlData data = (VhdlData)o;
		return this.getCode().equals(data.getCode());
		
		
		
		
	}//end equals()
	
}//end class
