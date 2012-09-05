
package com.valil.chesswebservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="boardFEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="repetitiveMoveCAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="searchDepth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "boardFEN",
    "repetitiveMoveCAN",
    "searchDepth"
})
@XmlRootElement(name = "GetNextMove")
public class GetNextMove {

    protected String boardFEN;
    protected String repetitiveMoveCAN;
    protected int searchDepth;

    /**
     * Gets the value of the boardFEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoardFEN() {
        return boardFEN;
    }

    /**
     * Sets the value of the boardFEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoardFEN(String value) {
        this.boardFEN = value;
    }

    /**
     * Gets the value of the repetitiveMoveCAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepetitiveMoveCAN() {
        return repetitiveMoveCAN;
    }

    /**
     * Sets the value of the repetitiveMoveCAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepetitiveMoveCAN(String value) {
        this.repetitiveMoveCAN = value;
    }

    /**
     * Gets the value of the searchDepth property.
     * 
     */
    public int getSearchDepth() {
        return searchDepth;
    }

    /**
     * Sets the value of the searchDepth property.
     * 
     */
    public void setSearchDepth(int value) {
        this.searchDepth = value;
    }

}
