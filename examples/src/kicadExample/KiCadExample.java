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
 * Example project that transforms a KiCad netlist into VHDL code
 */
package kicadExample;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.proglogicjlib.kicad.KiCadToVhdl;
import com.proglogicjlib.misc.CloseProgramException;
import com.proglogicjlib.misc.WarningException;
import com.proglogicjlib.vhdl.VhdlKeywords;

public class KiCadExample {

	public static void main(String[] args) {
		
		for(int i = 0; i < args.length; i++){
			JOptionPane.showMessageDialog(null, "param== " + args[i]);
		}
		
		KiCadToVhdl converter  = new KiCadToVhdl();
		
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("netlist", "net");
		fc.setFileFilter(filter);
		
		if(fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION){
			JOptionPane.showMessageDialog(null, "Could not open KiCad netlist file");
		}else{
			try {
				converter.netlistToVhdl(fc.getSelectedFile(), "c:\\vhdlTest", false, true, VhdlKeywords.TYPE_STD_LOGIC, VhdlKeywords.TYPE_INTEGER);
			} catch (CloseProgramException e) {//If this happens something went wrong and file has not been created correctly
				e.printStackTrace();
			} catch (WarningException e) {//If this happens something went wrong and file has not been created correctly
				e.printStackTrace();
			}
		}//end if
		
	}//end test

}//end class
