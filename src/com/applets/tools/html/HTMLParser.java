package com.applets.tools.html;

//TODO
public class HTMLParser {

	private static HTMLParser instance = null;

	// private HtmlCleaner htmlCleaner = new HtmlCleaner();

	public static HTMLParser getInstance() {
		if (HTMLParser.instance == null) {
			HTMLParser.instance = new HTMLParser();
		}
		return HTMLParser.instance;
	}

	private HTMLParser() {
	}

	public String parse(final String htmlToParse) {
		// TagNode rootNode = htmlCleaner.clean(htmlToParse);
		// TagNode[] nodes = rootNode.getAllElements(true);
		// for (TagNode tagNode : nodes) {
		// System.out.println(tagNode.toString());
		// }
		return "";
	}

}
