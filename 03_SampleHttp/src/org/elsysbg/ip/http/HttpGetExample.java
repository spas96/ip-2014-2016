package org.elsysbg.ip.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpGetExample {

	private static final String HTTP_METHOD_GET = "GET";
	private static final int HTTP_PORT = 80;
	private static final String PROTOCOL_VERSION = "HTTP/1.1";

	public static void main(String[] args) throws UnknownHostException, IOException {
		final HttpGetExample example = new HttpGetExample();
		final CharacterHttpResponse response = example.createRequest("example.com",
			HTTP_METHOD_GET, "/index.html");
		
		System.out.println(new String(response.getStatusLine()));
		for (HttpHeader next : response.getHeaders()) {
			System.out.printf("%s: %s\n", next.getName(), next.getValue());
		}
		System.out.println(new String(response.getBody()));
// or using for
//		for (char next : response.getBody()) {
//			System.out.print(next);
//		}
	}
	
	public CharacterHttpResponse createRequest(String host, String method,
			String path)
			throws UnknownHostException, IOException {
		final Socket clientSocket = new Socket(host, HTTP_PORT);
		try {
			final InputStream inputStream = clientSocket .getInputStream();
			final OutputStream outputStream = clientSocket.getOutputStream();
			
			final InputStreamReader inputStreamReader =
				new InputStreamReader(inputStream);
			final BufferedReader in = new BufferedReader(inputStreamReader);
			final PrintWriter out = new PrintWriter(outputStream);

			writeRequest(out, host, method, path);
			out.flush();
			
			return parseResponse(in);
		} finally {
			// we should ALWAYS close sockets!
			clientSocket.close();
		}
		
		
	}

	private CharacterHttpResponse parseResponse(BufferedReader in) throws IOException {
		final CharacterHttpResponse result = new CharacterHttpResponse();
		
		//reading status line
		result.setStatusLine(in.readLine());
		
		// reading headers until new line - this is marker for end of headers
		String next;
		while(!(next = in.readLine()).equals("")) {
			result.getHeaders().add(HttpHeader.createFromHeaderLine(next));
		}
		
		// TODO chunked transfer-encoding is not supported!

		// reading body - we already know how many bytes the body is
		// (from the content-length header line)
		in.read(result.getBody());
		return result;
	}

	private void writeRequest(PrintWriter out, String host,
			String method, String path) {
		out.printf("%s %s %s\n", method, path, PROTOCOL_VERSION);
		out.printf("Host: %s\n", host);
		out.printf("\n");
	}

}
