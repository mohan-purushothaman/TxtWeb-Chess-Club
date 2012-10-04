Package details

1)com.valil.chesswebservice  
	Contains auto generated classes for the WSDL web service present at http://www.valil.com/ChessWebService/Service.asmx?WSDL
	
2)org.cts.chess.engine
	Contains core classes of the chess engine
	
3)org.cts.chess.txtchess.api
  Contains Basic and core functionality classes of TxtChess 
	1)TxtWebApiUtil.java
		Util class which encapsulates TxtWeb API calls and return the response XML as
  		properties which is easily accessible using property names (as per XML hierarchy) 
  			
	2) TxtWebServlet.java
		Basic servlet which encapsulates functionalities of a TxtWeb service, All other servlets extend from this service.
		
	3)ParameterHandlerServlet.java
		A Abstract extension of TxtWebServlet, this servlet can detect and parse complex message parameters by using ParamerDetector
	
	4)ListIterator.java
		It Segments total items and shows users only a part with next and previous links (Used for listing games,challenges,moves and history)
	
4)org.cts.chess.txtchess.gae
	Classes related to Google App Engine functionalities and Entities for DB.
	
5)org.cts.chess.txtchess.servlets
	Contains servlets (all are subclasses of org.cts.chess.txtchess.api.TxtWebServlet) for each TxtChess functionality.
	

	