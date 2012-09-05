package org.cts.chess.txtchess.api;

import java.util.List;


/**
 *
 * @author Mohan
 */
@SuppressWarnings("serial")
public class ParameterNotPresentException extends Exception{
    private Parameter parameter;
    private String partialOutput="";
    public ParameterNotPresentException(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getPartialOutput() {
    	return partialOutput;
	}
    
	public void setPartialOutput(List<String> output) {	
		partialOutput="";
		for(String s:output)
		{
			partialOutput = partialOutput+" "+s;
		}
	}
    
}
