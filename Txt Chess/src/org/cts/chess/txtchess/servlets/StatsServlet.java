package org.cts.chess.txtchess.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.TxtWebServlet;

@SuppressWarnings("serial")
public class StatsServlet extends TxtWebServlet {

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash,
			String message) throws Exception {
		setJspPath("/jsp/Stats.jsp");
		}

		
	
}
