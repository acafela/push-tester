package push.tester.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Args {
	private Map<Character, ArgumentMarshaler> marshalers;
	private Set<Character> argsFound;
	private ListIterator<String> currentArgument;
	
	public Args(String schema, String[] args) throws ArgsException {
		marshalers = new HashMap<>();
		argsFound = new HashSet<>();
		
		parseSchema(schema);
		parseArgumentStrings(Arrays.asList(args));
	}


	private void parseSchema(String schema) throws ArgsException {
		for(String element : schema.split(",")) {
			if(element.length() > 0) {
				parseSchemaElement(element.trim());
			}
		}
	}

	private void parseSchemaElement(String element) throws ArgsException {
		char elementId = element.charAt(0);
		String elementTail = element.substring(1);
		validateSchemaElementId(elementId);
		if(elementTail.length() == 0) {
			marshalers.put(elementId, new BooleanArgumentMarshaler());
		} else if(elementTail.equals("*")) {
			marshalers.put(elementId, new StringArgumentMarshaler());
		} else if(elementTail.equals("#")) {
			marshalers.put(elementId, new IntegerArgumentMarshaler());
		} else {
			throw new ArgsException(ErrorCode.INVALID_SCHEMA_FORMAT, elementId, elementTail);
		}
	}

	private void validateSchemaElementId(char elementId) throws ArgsException {
		if(!Character.isLetter(elementId)) {
			throw new ArgsException(ErrorCode.INVALID_SCHEMA_NAME, elementId, null);
		}
	}
	
	private void parseArgumentStrings(List<String> args) throws ArgsException {
		for(currentArgument = args.listIterator(); currentArgument.hasNext();) {
			String argString = currentArgument.next();
			if(argString.startsWith("-")) {
				parseArgumentCharacters(argString.substring(1));
			} else {
				currentArgument.previous();
				break;
			}
		}
	}


	private void parseArgumentCharacters(String argChars) throws ArgsException{
		for(int i = 0; i < argChars.length(); i++) {
			parseArgumentCharacter(argChars.charAt(i));
		}
	}


	private void parseArgumentCharacter(char argChar) throws ArgsException {
		ArgumentMarshaler m = marshalers.get(argChar);
		if(m == null) {
			throw new ArgsException(ErrorCode.UNEXPECTED_ARGUMENT, argChar, null);
		} else {
			argsFound.add(argChar);
			try {
				m.set(currentArgument);
			} catch (ArgsException e) {
				e.setErrorArgumentId(argChar);
			}
		}
	}
	
	public boolean has(char arg) {
		return argsFound.contains(arg);
	}
	
	public int nextArgument() {
		return currentArgument.nextIndex();
	}
	
	public boolean getBoolean(char arg) {
		return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
	}
	
	public String getString(char arg) {
		return StringArgumentMarshaler.getValue(marshalers.get(arg));
	}
	
	public int getInt(char arg) {
		return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
	}
	
}
