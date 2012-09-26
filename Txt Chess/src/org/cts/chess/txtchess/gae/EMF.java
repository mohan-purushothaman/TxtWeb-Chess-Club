package org.cts.chess.txtchess.gae;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * Entity manager creator factory and destroyer factory
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 */
public final class EMF implements ServletContextListener {
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactionConnection");

	public static EntityManagerFactory get() {
		return emfInstance;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		get().close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}
}