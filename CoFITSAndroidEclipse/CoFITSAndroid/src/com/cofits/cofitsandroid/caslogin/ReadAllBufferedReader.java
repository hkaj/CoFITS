package com.cofits.cofitsandroid.caslogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ReadAllBufferedReader extends BufferedReader {

	public ReadAllBufferedReader(Reader in) {
		super(in);
	}

	public String readAll() throws IOException {
		StringBuilder s = new StringBuilder();

		String line = readLine();
		while (line != null) {
			s.append(line);
			s.append('\n');
			line = readLine();
		} ;

		return s.toString();
	}
}