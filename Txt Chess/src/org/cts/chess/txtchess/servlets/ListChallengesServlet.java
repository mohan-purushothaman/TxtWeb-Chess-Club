package org.cts.chess.txtchess.servlets;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cts.chess.txtchess.api.Condition;
import org.cts.chess.txtchess.api.Parameter;
import org.cts.chess.txtchess.api.ParameterDetector;
import org.cts.chess.txtchess.api.ParameterHandlerServlet;
import org.cts.chess.txtchess.gae.EMF;
import org.cts.chess.txtchess.gae.db.Challenge;
import org.cts.chess.txtchess.gae.db.DB_Util;
import org.cts.chess.txtchess.gae.db.ChessUser;

@SuppressWarnings("serial")
public class ListChallengesServlet extends ParameterHandlerServlet {

	@Override
	public ParameterDetector createParameterDetector() {
		return new ParameterDetector(
				new Parameter("'all' or EMPTY", null, true,
						Condition.DEFAULT,
						"all to see open challenges or empty to see challenges specific to you"));
	}

	@Override
	public void process(HttpServletRequest request,
			HttpServletResponse response, String mobileHash,
			List<String> message) {
		final EntityManager manager = EMF.get().createEntityManager();
		try {
			ChessUser user = manager.find(ChessUser.class, mobileHash);
			String option = message.get(0);
			boolean listAll = "all".equalsIgnoreCase(option);
			List<Challenge> challenges = DB_Util.getChallenges(manager,
					user.getMobileHash(), user.getRating(), listAll);
			/*String startIndexString = request.getParameter("startIndex");
			ListIterator<Challenge> challengeIterator = new ListIterator<Challenge>(
					listAll ? "Open Challenges" : "Challenges Specific To You",
					challenges, startIndexString == null ? 1
							: Integer.parseInt(startIndexString),
					"/listChallenges?txtweb-message=" + option + "&",
					"<br/><br/>Reply, '[option] reject' to reject the challenge") {

				@Override
				public String getRowLink(Challenge challenge) {
					ChessUser opponent = manager.find(ChessUser.class,
							challenge.getCreatedBy());

					return "<a href='./acceptChallenge?challengeId="
							+ challenge.getId() + "'> Play as "
							+ challenge.getOpponentColor() + " vs "
							+ opponent.getUserName() + "("
							+ opponent.getRating() + ")</a>";
				}
			};

			request.setAttribute("__listIterator", challengeIterator);
			setJspPath("/jsp/common/ListIteratorHandler.jsp");
			*/

			request.setAttribute("__message", listAll ? "Open Challenges" : "Challenges Specific To You");
			request.setAttribute("__challenges", challenges);
			setJspPath("/jsp/ListChallenges.jsp");
		} finally {
			manager.close();
		}
	}

	@Override
	public String getServiceEndPoint() {
		return "listChallenges";
	}

	@Override
	public String getServiceName() {
		return "@chezz.challenges";
	}

	@Override
	public String getExamples() {
		return "@chezz.challenges all (list all open challenges available to you)<br/>"
		+"@chezz.challenges (list challenges made by other users specifically to you)";
	}

}
