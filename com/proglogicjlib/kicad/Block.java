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
 * Data type that is used to store partial block from the netlist into smaller
 * peaces to make the processing easier and the code more readable. 
 */
package com.proglogicjlib.kicad;

public class Block {
	
	private int start; 
	private int end;
	private String blockText;
	
	/**
	 * Set the line number of the original document where this block has been found
	 * @param val
	 */
	public void setStart(int val){
		this.start = val;
	}
	
	/**
	 * Set the block text content
	 * @param val
	 */
	public void setText(String val){
		this.blockText = val;
	}
	
	/**
	 * Set the line number of the original document where this block has been finished
	 * @param val
	 */
	public void setEnd(int val){
		this.end = val;
	}
	
	/**
	 * Return the start line of the block in the original file
	 * @return
	 */
	public int getStart(){
		return this.start;
	}
	
    /**
     * Get the text content of the block
     * @return
     */
	public String getText(){
		return this.blockText;
	}
	
	/**
	 * Return the end line of the block in the original file
	 * @return
	 */
	public int getEnd(){
		return this.end;
	}

}//end class
