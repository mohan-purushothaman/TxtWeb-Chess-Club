package org.cts.chess.txtchess.servlets;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.TxtWebServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.GamesArchive;

@SuppressWarnings("serial")
public class ShowArchiveServlet extends TxtWebServlet {

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash, String message)
			throws Exception {

		EntityManager manager = EMF.get().createEntityManager();
		try {
			String gameId = request.getParameter("gameId");
			GamesArchive game = manager.find(GamesArchive.class, Long.parseLong(gameId));
			if (game == null) {
				throw new Exception("Game doesn't exist");
			}
			request.setAttribute("__archive", game);
			setJspPath("/jsp/ShowArchive.jsp");
		} finally {
			manager.close();
		}

	}
}
