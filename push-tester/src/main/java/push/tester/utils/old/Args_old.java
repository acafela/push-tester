package push.tester.utils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Args {
	private String schema;
	private String[] args;
	private boolean valid;
	private Set<Character> unexpectedArguments = new TreeSet<>();
	private Map<Character, Boolean> booleanArgs = new HashMap<>();
	private Map<Character, String> stringArgs = new HashMap<>();
	private Set<Character> argsFound = new HashSet<Character>();
//	private int numberOfArguments = 0;
	private int currentArgument;
	private char errorArgument;
	
	enum ErrorCode {
		OK, MISSING_STRING;
	}
	
	private ErrorCode errorCode = ErrorCode.OK;
	
	public Args(String schema, String[] args) throws ParseException {
		this.schema = schema;
		this.args = args;
		valid = parse();
	}
	
	public boolean isValid() {
		return valid;
	}

	private boolean parse() throws ParseException {
		if(schema.length() == 0 && args.length ==0) {
			return true;
		}
		parseSchema();
		parseArguments();
		return valid;
	}

	private boolean parseSchema() throws ParseException {
		for(String element : schema.split(",")) {
			if(element.length() > 0) {
				String trimmedElement = element.trim();
				parseSchemaElement(trimmedElement);				
			}
		}
		return true;
	}
	
	private void parseSchemaElement(String element) throws ParseException {
		char elementId = element.charAt(0);
		String elementTail = element.substring(1);
		validateSchemaElementId(elementId);
		if(isBooleanSchemaElement(elementTail)) {
			parseBooleanSchemaElement(elementId);
		} else if(isStringSchemaElement(elementTail)) {
			parseStringSchemaElement(elementId);
		}
	}

	private void validateSchemaElementId(char elementId) throws ParseException {
		if(!Character.isLetter(elementId)) {
			throw new ParseException("Bad character : " + elementId + " in Args format : " + schema, 0);
		}
	}
	private boolean isBooleanSchemaElement(String elementTail) {
		return elementTail.length() == 0;
	}
	private void parseBooleanSchemaElement(char elementId) {
		booleanArgs.put(elementId, false);
		
	}
	private boolean isStringSchemaElement(String elementTail) {
		return elementTail.equals("*");
	}
	private void parseStringSchemaElement(char elementId) {
		stringArgs.put(elementId, "");
	}
	
	private boolean parseArguments() {
		for(currentArgument = 0; currentArgument < args.length; currentArgument++) {
			String arg = args[currentArgument];
			parseArgument(arg);
		}
		return true;
	}

	private void parseArgument(String arg) {
		if(arg.startsWith("-")) {
			parseElements(arg);
		}
	}

	private void parseElements(String arg) {
		for(int i=1; i<arg.length(); i++) {
			parseElement(arg.charAt(i));
		}
	}

	private void parseElement(char argChar) {
		if(setArgument(argChar)) {
			argsFound.add(argChar);
		} else {
			unexpectedArguments.add(argChar);
			valid = false;
		}
	}


	private boolean setArgument(char argChar) {
		boolean set = true;
		if(isBoolean(argChar)) {
			setBooleanArg(argChar, true);
		} else if(isString(argChar)) {
			setStringArg(argChar);
		} else {
			set = false;
		}
		return set;
	}

	private boolean isBoolean(char argChar) {
		return booleanArgs.containsKey(argChar);
	}

	private void setBooleanArg(char argChar, boolean value) {
		booleanArgs.put(argChar, value);
	}
	
	private boolean isString(char argChar) {
		return stringArgs.containsKey(argChar);
	}
	
	private void setStringArg(char argChar) {
		currentArgument++;
		try {
			stringArgs.put(argChar, args[currentArgument]);
		} catch (ArrayIndexOutOfBoundsException e) {
			valid = false;
			errorArgument = argChar;
			errorCode = ErrorCode.MISSING_STRING;
		}
	}
	
	public int cardinality() {
		return argsFound.size();
	}
	
	public String usage() {
		if(unexpectedArguments.size() > 0) {
			return unexpectedArgumentMessage();
		} else {
			return "";
		}
	}

	private String unexpectedArgumentMessage() {
		StringBuilder message = new StringBuilder("Argument(s) - ");
		for(char c : unexpectedArguments) {
			message.append(c);
		}
		message.append(" unexpected.");
		
		return message.toString();
	}
	
	public boolean getBoolean(char arg) {
		return falseIfNull(booleanArgs.get(arg));
	}

	private boolean falseIfNull(Boolean b) {
		return b == null ? false : b;
	}
	
	public String getString(char arg) {
		return blankIfNull(stringArgs.get(arg));
	}

	private String blankIfNull(String s) {		
		return s == null  ? "" : s;
	}
}
