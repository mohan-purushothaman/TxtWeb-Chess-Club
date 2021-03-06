package org.cts.chess.txtchess.api;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.cts.chess.txtchess.servlets.TxtChessUtil;


/**
 * A detector which helps to parse parameters to txtweb service (Support class for ParameterHandlerServlet)
 *
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 */
public class ParameterDetector {
    Parameter[] parameters;

    public ParameterDetector(Parameter... parameters) throws RuntimeException {
        this.parameters = parameters;
        boolean canWarn=false;
        for (Parameter parameter : parameters) {
            if(parameter.isOptional())
            {
                canWarn=true;
            }
            else
            {
                if(canWarn) {
                	//a necessary precondition 
                	//because we can't be sure whether current param is optional or nonoptional without this condition
                    throw new RuntimeException("Optional parameters shouldn't come before NonOptional Perameters");
                }
            }
        }
    }

   public List<String> detectParameters(String input) throws ParameterNotPresentException
   {
       input=input.trim().replaceAll("  ", " ");
       Queue<String> inputQueue=new LinkedList<String>(Arrays.asList(input.split(" ")));
       List<String> output=new LinkedList<String>();
       for (Parameter parameter : parameters) {
           String peek=inputQueue.peek();
           try{
        	
           String out=parameter.detectParam(peek);
           
           output.add(out);
           if(out.equals(peek))
           {
               inputQueue.remove();
           }
           }
           catch(ParameterNotPresentException e)
           {
        	   e.setPartialOutput(output);
        	   throw e;
           }
       }
       return output;
   }
   
   public String getDescription()
   {
	   StringBuilder sb=new StringBuilder();
	   for(Parameter p:parameters)
	   {
		   sb.append('[').append(p.getName()).append(']').append(' ');
	   }
	   return sb.toString();
   }
   
   public List<Parameter> getParams()
   {
	   return Arrays.asList(parameters);
   }
   
   public static final ParameterDetector USERNAME_DETECTOR=new ParameterDetector(
			new Parameter("User Name", null, false, new Condition() {

				@Override
				public boolean validate(String input) {
					return TxtChessUtil.validateUserName(input);
				}
			},
					"[alphanumerals or hypen or underscore] (6 to 15 lowercase characters)"));
}
