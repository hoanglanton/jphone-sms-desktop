package it.flaminiandrea.jphonesms.export.html;

import it.flaminiandrea.jphonesms.domain.Data;
import it.flaminiandrea.jphonesms.domain.Entry;

import java.util.List;

public class HtmlCodeCreator {
	private String lineSeparator = System.getProperties().getProperty("line.separator");

	private String createHeadCode(String pathToStyle) {
		return
		"<head>\n"+lineSeparator+
		"<title>jPhone SMS Desktop</title>"+lineSeparator+
		"<meta http-equiv=\"content-type\" content=\"text/html;charset=iso-8859-1\" />"+lineSeparator+
		"<link rel=\"stylesheet\" href=\""+pathToStyle+"style.css\" type=\"text/css\" />"+lineSeparator+
		"<link rel=\"stylesheet\" type=\"text/css\" href=\""+pathToStyle+"sms-visualization-style.css\"/>"+lineSeparator+
		"</head>";
	}

	private String createDivLogoCode() {
		return 
		"<div id=\"logo\">"+lineSeparator+
		"<h1><a>RedSoft</a></h1>"+lineSeparator+
		"</div>";
	}

	private String createDivIntroCode() {
		return 
		"<div id=\"intro\">"+lineSeparator+
		"</div>";
	}

	public String createMessagesPageCode(List<String> nameList, List<Entry> entries) {
		String result = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			this.createHeadCode("../") + lineSeparator +
			"<body>" + lineSeparator +
			"<div id=\"content\">" + lineSeparator +
			this.createDivLogoCode() + lineSeparator +
			this.createDivIntroCode() + lineSeparator +
			this.createDivLeftCode(nameList,"../","") + lineSeparator +
			this.createDivRightCode(entries) + lineSeparator +
			"</div>" + lineSeparator +
			"</body>" + lineSeparator;
		return result;
	}

	private String createDivRightCode(List<Entry> entries) {
		String result = 
			"<div id=\"right\">" + lineSeparator +
			"<h2>SMS from: " + entries.get(0).getName() + "</h2>";
		for (Entry entry : entries) {
			String direction ="rcvd";
			if(entry.getFlags().equals("Sent to:")) {
				direction = "sent";
			}
			result +=
				"<table id=\"" + direction + "-rounded-corner\">" + lineSeparator +
				"<thead>" + lineSeparator +
				"<tr>" + lineSeparator +
				"<th class=\"rounded-top-left\"></th>" + lineSeparator +
				"<th class=\"rounded-top-center\"></th>" + lineSeparator +
				"<th class=\"rounded-top-right\"></th>" + lineSeparator +
				"</tr>" + lineSeparator +
				"<tfoot>" + lineSeparator + 
				"<tr>" + lineSeparator +
				"<td class=\"rounded-foot-left\"></td>" + lineSeparator +
				"<td class=\"rounded-foot-center\"></td>" + lineSeparator +
				"<td class=\"rounded-foot-right\"> </td>" + lineSeparator +
				"</tr>" + lineSeparator +
				"</tfoot>" + lineSeparator +
				"<tbody>" + lineSeparator +
				"<tr>" + lineSeparator +
				"<td class=\"rounded-middle-left\"></td>" + lineSeparator +
				"<td class=\"rounded-middle-center\">" +entry.toHtmlString()+ "</td>" + lineSeparator +
				"<td class=\"rounded-middle-right\"></td>" + lineSeparator +
				"</tr>" + lineSeparator +
				"</tbody>" + lineSeparator +
				"</table>" + lineSeparator;
		}
		result +=
			"</div>" + lineSeparator;
		return result;
	}

	private String createDivLeftCode(List<String> nameList, String pathToIndex, String pathToMessages) {
		String result = 
			"<div id=\"left\">" + lineSeparator +
			"<a href=\""+pathToIndex+"index.html\"><b>jPhone SMS Desktop Stats</b></a><br /><br />" + lineSeparator +
			"<h2>Senders:</h2>" + lineSeparator +
			"<ul id=\"leftmenu\">";
		for (String name : nameList) {
			String fileName = name.replace(" ", "").replace("'", "").replace("à", "a").toLowerCase();
			result += 
				"<li>" + lineSeparator +
				"<a href=\"" + pathToMessages + fileName + ".html\">" + name + "</a>" + lineSeparator +
				"</li>" + lineSeparator;
		}
		result +=
			"</ul>" + lineSeparator +
			"</div>";
		return result;
	}

	public String createIndexCode(List<String> nameList, Data data, String pathToImage) {
		String result = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" + lineSeparator +
			"<html>" + lineSeparator +
			this.createHeadCode("") + lineSeparator +
			"<body>" + lineSeparator +
			"<div id=\"content\">" + lineSeparator +
			this.createDivLogoCode() + lineSeparator +
			this.createDivIntroCode() + lineSeparator +
			this.createDivLeftCode(nameList,"","messages/") + lineSeparator +
			this.createDivRightCode(nameList, data, pathToImage) + lineSeparator +
			"</div>" + lineSeparator +
			"</body>" + lineSeparator;
		return result;
	}

	private String createDivRightCode(List<String> nameList, Data data, String pathToImage) {
		List<Entry> list = data.getEntriesByNameAndByReverseDate();
		String result = "<div id=\"right\">" + lineSeparator;
		result += 
			"<img src=\""+pathToImage+"\" /><br /><br />" +
			"<h1>Total Number of Messages: " + list.size() + "</h1>" + lineSeparator;
		return result + lineSeparator + "</div>";
	}

}
