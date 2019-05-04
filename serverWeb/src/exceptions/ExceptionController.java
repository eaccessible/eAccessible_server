package exceptions;

public class ExceptionController extends Exception {

	private int errorCode;
	private String errorMessage;

	public ExceptionController() {
		super();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	//crear atribut amb el seu getter i setter

}
