package org.cts.chess.txtchess.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * Util class which encapsulates TxtWeb API calls and return the response XML as
 * properties which is easily accessible using property names (as per XML hierarchy) 
 * 
 * For example if txtweb replies 
 * 
 <txtWeb> 
 <status> <code>0</code>
 <message>success</message> 
 </status> 
 <url>url123</url>
 </txtWeb>
 * 
 * The returned properties will contain following (key = value) pairs
 * 
 * txtWeb.status.code = 0
 * txtWeb.status.message = success
 * txtWeb.url = url123
 * 
 * 
 * @author Mohan Purushothaman <mohan.purushothaman.88@gmail.com>
 * 
 */

public class TxtWebApiUtil {
	public static final String APP_KEY = "c4a08eef-25b2-411d-8600-4573481762a7";
	public static final String PUBLISHER_KEY = "6c67576d-6e51-4880-b959-55eaf8e1a1da";
	public static final String PUSH_MSG_URL = "http://api.txtweb.com/v1/push";
	public static final String VERIFY_MSG_URL = "http://api.txtweb.com/v3/verify";

	public static final String TXTWEB_VERIFY_PARAM = "txtweb-verifyid";
	public static final String TXTWEB_MOBILE_PARAM = "txtweb-mobile";
	public static final String TXTWEB_MESSAGE_PARAM = "txtweb-message";
	public static final String TXTWEB_PROTOCOL_PARAM = "txtweb-protocol";

	public static Properties pushMessage(String mobileHash, String htmlMessage) {
		
		if (mobileHash.startsWith("ai-"))
			return new Properties();
		try {
			Properties p = new Properties();
			p.setProperty("txtweb-message",
					URLEncoder.encode(htmlMessage, "UTF-8"));
			p.setProperty("txtweb-mobile",
					URLEncoder.encode(mobileHash, "UTF-8"));
			p.setProperty("txtweb-pubkey",
					URLEncoder.encode(PUBLISHER_KEY, "UTF-8"));
			return getResponseAsProperties(PUSH_MSG_URL, p);
		} catch (Exception e) {
			e.printStackTrace();
			return new Properties();
		}
	}

	public static Properties getResponseAsProperties(String URL, Properties p)
			throws Exception {
		Document document = getDocument(URL, p);
		Properties properties = parseToProperties(document);
		Logger.getLogger(TxtWebApiUtil.class.getName()).info(
				properties.toString());
		return properties;
	}

	private static Properties parseToProperties(Document document) {
		document.normalize();
		Properties properties = new Properties();
		Element element = document.getDocumentElement();
		parse(element.getChildNodes(), element.getTagName(), properties);
		return properties;
	}

	private static void parse(NodeList nodeList, String parentString,
			Properties properties) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (n instanceof Element) {
				Element element = (Element) n;
				parse(element.getChildNodes(),
						parentString + '.' + element.getTagName(), properties);
			} else {
				String value = n.getTextContent();
				if (n.getNodeType() == Node.TEXT_NODE
						&& value.trim().length() != 0)
					properties.put(parentString, value);
			}
		}
	}

	public static Document getDocument(String URL, Properties properties)
			throws SAXException, IOException, ParserConfigurationException {
		URL url = new URL(URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");

		StringBuilder sb = new StringBuilder();
		for (String key : properties.stringPropertyNames()) {
			sb.append((key + '=' + properties.getProperty(key) + '&'));
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream());
		out.write(sb.toString());
		out.close();

		InputStream stream = connection.getInputStream();
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(stream);

	}
/**
 * Verify whether this request came from TxtWeb server
 * 
 */
	public static boolean isAuthenticatedRequest(HttpServletRequest request)
			throws Exception {

		try {
			String mobileHash = request.getParameter(TXTWEB_MOBILE_PARAM);
			String message = request.getParameter(TXTWEB_MESSAGE_PARAM);
			String verifyId = request.getParameter(TXTWEB_VERIFY_PARAM);
			String protocol = request.getParameter(TXTWEB_PROTOCOL_PARAM);

			if (mobileHash == null || verifyId == null || protocol == null) {
				return false;
			}
			String params = TXTWEB_MOBILE_PARAM
					+ '='
					+ URLEncoder.encode(mobileHash, "UTF-8")
					+ '&'
					+ (message != null ? (TXTWEB_MESSAGE_PARAM + '='
							+ URLEncoder.encode(message, "UTF-8") + '&') : "")
					+ TXTWEB_PROTOCOL_PARAM + '='
					+ URLEncoder.encode(protocol, "UTF-8") + '&'
					+ TXTWEB_VERIFY_PARAM + '='
					+ URLEncoder.encode(verifyId, "UTF-8");
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new URL(VERIFY_MSG_URL + '?' + params).openStream());
			Properties properties = parseToProperties(doc);
			Logger.getLogger(TxtWebApiUtil.class.getName()).info(
					properties.toString());
			String value = properties.getProperty("txtWeb.status.code");
			return "0".equals(value);
		} catch (Exception e) {
			throw new Exception("Unable to verify your request with txtweb", e);
		}
	}
}
