package org.cts.chess.txtchess.api;
import java.util.List;

import javax.servlet.http.*;

/**
 *  A Abstract extension of TxtWebServlet, this servlet can detect and parse complex message parameters by using ParamerDetector
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */

@SuppressWarnings("serial")
public abstract class ParameterHandlerServlet extends TxtWebServlet {
	
	private ParameterDetector paramDetector;
	public final void process(HttpServletRequest request, HttpServletResponse response,String mobileHash,String message) throws Exception
	{
		String old=request.getParameter("__old");
		message=(old==null?"":old)+" "+(message==null?"":message);
		message=message.trim();
		if("help".equalsIgnoreCase(message))
		{
			request.setAttribute("__help", getParameterDetector());
			request.setAttribute("__serviceName", getServiceName());
			request.setAttribute("__example", getExamples());
			setJspPath("/jsp/common/HelpMessageHandler.jsp");
			return;
		}
		else{
		try{
		process(request, response, mobileHash,getParameterDetector().detectParameters(message));
		}
		catch(ParameterNotPresentException ex)
		{
			setJspPath("/jsp/common/InputHandler.jsp");
			request.setAttribute("__old", ex.getPartialOutput());
			request.setAttribute("__endPoint", getServiceEndPoint());
			request.setAttribute("__paramName", ex.getParameter().getName());
			request.setAttribute("__message", getMissingMessage(ex.getParameter()));	
		}
		}
	}

	public final ParameterDetector getParameterDetector()
	{
		if(paramDetector==null)
		{
			paramDetector=createParameterDetector();
		}
		return paramDetector;
	}
	
	public abstract ParameterDetector createParameterDetector();
	
	public abstract void process(HttpServletRequest request, HttpServletResponse response,String mobileHash,List<String> message) throws Exception;
	
	public String getMissingMessage(Parameter missingParameter)
	{
		//return null;
		return "Please provide valid "+ missingParameter.getName()+"<br/>"+missingParameter.getHelpText();
	}
	
	public abstract String getServiceEndPoint();
	
	public abstract String getServiceName();
	
	public abstract String getExamples();
}
