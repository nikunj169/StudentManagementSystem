package sms;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The enum that represents the selected language
 * 
 * @author Artiom
 *
 */
enum Language {
	ENG, RU, RO
}

/**
 * The class that handles internationalization and language changes
 * 
 * @author Artiom
 *
 */
public class Translator {
	/**
	 * HashMap that holds tags as keys and messages as values
	 */
	private static HashMap<String, String> messages;
	/**
	 * The variable that holds currently selected language
	 */
	private static Language language;
