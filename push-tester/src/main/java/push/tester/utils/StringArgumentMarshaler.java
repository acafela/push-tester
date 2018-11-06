package push.tester.utils;

import java.util.Iterator;

public class StringArgumentMarshaler implements ArgumentMarshaler{
	
	private String stringValue = "";

	@Override
	public void set(Iterator<String> currentArgument) throws ArgsException {
		try {
			stringValue = currentArgument.next();
		} catch (Exception e) {
			throw new ArgsException(ErrorCode.MISSING_STRING);
		}
	}
	
	public static String getValue(ArgumentMarshaler am) {
		if(am != null && am instanceof StringArgumentMarshaler) {
			return ( (StringArgumentMarshaler)am ).stringValue;
		} else {
			return "";
		}
	}

}
