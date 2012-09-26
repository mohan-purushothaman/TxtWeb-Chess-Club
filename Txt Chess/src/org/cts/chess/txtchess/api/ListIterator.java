package org.cts.chess.txtchess.api;

import java.util.List;

/**
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 *
 * Common class to list Items to users in segments(support class for ListIteratorHandler.jsp)
 * It Segments total items and shows users only a part with next and previous links
 */

public abstract class ListIterator<E> {
private List<E> list;
private int startIndex;
private String header;
public static final int SEGMENT_COUNT=10;
private String callbackURL;
private String footer;
private String listString;
public ListIterator(String header,List<E> list, int startIndex,String callbackURL,String footer) {
	this.header=header;
	this.list = list;
	this.startIndex = startIndex;
	this.callbackURL=callbackURL;
	this.footer=footer;
	listString=createListString();
}

public abstract String getRowLink(E element);

public String getListString()
{
	return listString;
}

public String createListString()
{
	if(list.isEmpty())
	{
		return "None<br/>";
	}
	
	StringBuilder sb=new StringBuilder();
	int start=startIndex;
	int end=getSegmentEnd();
	
	for(E e:list.subList(start-1, end))
	{
		String link=getRowLink(e);
		if(link!=null){
		sb.append("<br/>").append(link).append("<br/>");
		}
	}
	sb.append(footer);
	return sb.toString();
}

public String getHeader()
{
	if(list.size()==0)
	{
		return header;
	}
	return header+" ("+ startIndex+" to "+(getSegmentEnd())+" of "+list.size()+")";
}

private int getSegmentEnd()
{
	return Math.min(startIndex+SEGMENT_COUNT-1, list.size());
}
public String getCallbackURLSegment()
{
	String result="";
	if(getSegmentEnd()<list.size())
	{
	result=result+"<a href='"+callbackURL+"startIndex="+(startIndex+SEGMENT_COUNT)+"'>Next ("+(getSegmentEnd()+1)+" to "+(Math.min(getSegmentEnd()+SEGMENT_COUNT,list.size()))+")</a><br/>";
	}
	if(startIndex-SEGMENT_COUNT>0)
	{
		result=result+("<a href='"+callbackURL+"startIndex="+(startIndex-SEGMENT_COUNT)+"'>Previous ("+(startIndex-SEGMENT_COUNT)+" to "+ startIndex+")<a/><br/>");
	}
	return result;
}
}
