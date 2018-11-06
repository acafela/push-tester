package push.tester.utils;

public class ArgsException extends Exception{
	private char errorArgumentId = '\0';
	private String errorParameter = null;
	private ErrorCode errorCode = ErrorCode.OK;
	
	public ArgsException() {}
	
	public ArgsException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	public ArgsException(ErrorCode errorCode, String errorParameter) {
		this.errorCode = errorCode;
		this.errorParameter = errorParameter;
	}
	
	public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
		this.errorCode = errorCode;
		this.errorParameter = errorParameter;
		this.errorArgumentId = errorArgumentId;
	}
	
	public char getErrorArgumentId() {
		return errorArgumentId;
	}

	public void setErrorArgumentId(char errorArgumentId) {
		this.errorArgumentId = errorArgumentId;
	}

	public String getErrorParameter() {
		return errorParameter;
	}

	public void setErrorParameter(String errorParameter) {
		this.errorParameter = errorParameter;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String errorMessage() {
		switch(errorCode) {
		case OK:
			return "TILT: Should not get here.";
		case UNEXPECTED_ARGUMENT:
			return String.format("Argument -%c unexpected.", errorArgumentId);
		case MISSING_STRING:
			return String.format("Could not find string parameter for -%c.", errorArgumentId);
		case INVALID_INTEGER:
			return String.format("Argument -%c expects an integer but was '%s'.", errorArgumentId, errorParameter);
		case MISSING_INTEGER:
			return String.format("Could not find integer parameter for -%c.", errorArgumentId);
		case INVALID_DOUBLE:
			return String.format("Argument -%c expects a double but was '%s'.", errorArgumentId, errorParameter);					
		case MISSING_DOUBLE:
			return String.format("Could not find double parameter for -%c", errorArgumentId);
		case INVALID_SCHEMA_NAME:
			return String.format("'%c' is not a valid schema name.", errorArgumentId);
		case INVALID_SCHEMA_FORMAT:
			return String.format("'%s' is not a valid schema format.", errorParameter);
		}
		return "";
	}

}
