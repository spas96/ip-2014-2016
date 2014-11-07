package org.elsysbg.ip.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeader {
	private final String name;
	private final String value;
	
	public static HttpHeader createFromHeaderLine(String headerLine) {
		final Pattern p = Pattern.compile("([^:]+):\\s?(.+)");
		final Matcher m = p.matcher(headerLine);
		if (!m.matches()) {
			throw new IllegalArgumentException(
					 "Header line must be properly formatted, not " + headerLine);
		}
		return new HttpHeader(m.group(1), m.group(2));
// or simply		
//		final String[] splitted = headerLine.split("(: )|(:)", 2);
//		if (splitted.length != 2) {
//			throw new IllegalArgumentException(
//				"Header line must be properly formatted, not " + headerLine);
//		}
//		return new HttpHeader(splitted[0], splitted[1]);
	}

	public HttpHeader(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException(
				"Name or value is not set: " + name + ", " + value);
		}
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
}
