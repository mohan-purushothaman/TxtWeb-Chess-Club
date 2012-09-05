package org.cts.chess.txtchess.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class TxtWebApiUtil {
	public static final String APP_KEY="c4a08eef-25b2-411d-8600-4573481762a7";
	public static final String PUBLISHER_KEY="6c67576d-6e51-4880-b959-55eaf8e1a1da";
	public static final String PUSH_MSG_URL="http://api.txtweb.com/v1/push";
public static Properties pushMessage(String mobileHash,String htmlMessage)
{
	if(mobileHash.startsWith("ai-"))
		return new Properties();
	//mobileHash="85c8079f-d984-4970-8d86-47ba58099e49"; //used for testing purpose hash of my mobile
	try{
	//String params ="txtweb-message="+URLEncoder.encode(htmlMessage,"UTF-8")
      //      +"&txtweb-mobile="+URLEncoder.encode(mobileHash,"UTF-8")
      //    +"&txtweb-pubkey="+URLEncoder.encode(PUBLISHER_KEY,"UTF-8");
       Properties p=new Properties();
       p.setProperty("txtweb-message", URLEncoder.encode(htmlMessage,"UTF-8"));
       p.setProperty("txtweb-mobile", URLEncoder.encode(mobileHash,"UTF-8"));
       p.setProperty("txtweb-pubkey", URLEncoder.encode(PUBLISHER_KEY,"UTF-8"));
    return getResponseAsProperties(PUSH_MSG_URL, p);
	}
	catch(Exception e)
	{
e.printStackTrace();
return new Properties();
	}
}

public static Properties getResponseAsProperties(String URL,Properties p) throws Exception 
{
	Document document =getDocument(URL,p);

    return parseToProperties(document);
}

private static Properties parseToProperties(Document document) {
    document.normalize();
    Properties properties = new Properties();
    Element element = document.getDocumentElement();
    parse(element.getChildNodes(), element.getTagName(), properties);
    return properties;
}

private static void parse(NodeList nodeList, String parentString, Properties properties) {
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node n = nodeList.item(i);
        if (n instanceof Element) {
            Element element = (Element) n;
            parse(element.getChildNodes(), parentString + '.' + element.getTagName(), properties);
        }
        else
        {
        	String value=n.getTextContent();
            if(n.getNodeType()==Node.TEXT_NODE&&value.trim().length()!=0)
            properties.put(parentString,value);
        }
    }
}



public static Document getDocument(String URL, Properties properties) throws SAXException, IOException, ParserConfigurationException
{
	URL url = new URL(URL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setRequestMethod("POST");
    
    StringBuilder sb=new StringBuilder();
    for(String key:properties.stringPropertyNames())
    {
    	sb.append((key+'='+properties.getProperty(key)+'&'));
    }
    if(sb.length()>0)
    {
    	sb.deleteCharAt(sb.length()-1);
    }
    OutputStreamWriter out=new OutputStreamWriter(connection.getOutputStream());
    out.write(sb.toString());
    out.close();
    
    InputStream stream=connection.getInputStream();
    Logger.getLogger(TxtWebApiUtil.class.getName()).info("\n"+connection.getResponseCode()+"\n");
	BufferedReader br=new BufferedReader(new InputStreamReader(stream));
	sb.setLength(0);
	String s;
	while((s=br.readLine())!=null)
	{
		sb.append(s);
	}
	Logger.getLogger(TxtWebApiUtil.class.getName()).info(sb.toString());
	
	return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new StringInputStream(sb.toString()));
	
}
}
