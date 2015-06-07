package com.rawpixil.eclipse.launchpad.internal.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;

public final class XML {

	private static final String XML_VERSION_ATTR = "version";
	private static final String XML_VERSION_VALUE = "1.0";

	public static Document newDocument() throws CoreException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw Exceptions.coreException("Failed to create XML document: " + e.getMessage());
		}
	}

	public static Document read(File file) throws CoreException {
		if (!file.exists()) {
			throw Exceptions.coreException("File not found '" + file.getAbsolutePath() + "'");
		}
		if (file.length() == 0) {
			throw Exceptions.coreException("File contains no data '" + file.getAbsolutePath() + "'");
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(inputStream);
		} catch (Exception e) {
			throw Exceptions.coreException("Failed to load XML document: " + e.getMessage());
		} finally {
			safeClose(inputStream);
		}
	}

	public static void write(Document document, File file) throws CoreException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			Source source = new DOMSource(document);
			Result result = new StreamResult(out);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(XML_VERSION_ATTR, XML_VERSION_VALUE);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			out.flush();
		} catch (Exception e) {
			throw Exceptions.coreException("Failed to save state.");
		} finally {
			XML.safeClose(out);
		}
	}

	private static void safeClose(Closeable closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	private XML() {
	}

}
