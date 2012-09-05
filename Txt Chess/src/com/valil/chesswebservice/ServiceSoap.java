
package com.valil.chesswebservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ServiceSoap", targetNamespace = "http://www.valil.com/ChessWebService")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ServiceSoap {


    /**
     * Get the next move according to the board configuration, a move which is very possible to provoke draw by repetition and search depth. Returns a null string if the operation fails.
     * 
     * @param searchDepth
     * @param boardFEN
     * @param repetitiveMoveCAN
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetNextMove", action = "http://www.valil.com/ChessWebService/GetNextMove")
    @WebResult(name = "GetNextMoveResult", targetNamespace = "http://www.valil.com/ChessWebService")
    @RequestWrapper(localName = "GetNextMove", targetNamespace = "http://www.valil.com/ChessWebService", className = "com.valil.chesswebservice.GetNextMove")
    @ResponseWrapper(localName = "GetNextMoveResponse", targetNamespace = "http://www.valil.com/ChessWebService", className = "com.valil.chesswebservice.GetNextMoveResponse")
    public String getNextMove(
        @WebParam(name = "boardFEN", targetNamespace = "http://www.valil.com/ChessWebService")
        String boardFEN,
        @WebParam(name = "repetitiveMoveCAN", targetNamespace = "http://www.valil.com/ChessWebService")
        String repetitiveMoveCAN,
        @WebParam(name = "searchDepth", targetNamespace = "http://www.valil.com/ChessWebService")
        int searchDepth);

}