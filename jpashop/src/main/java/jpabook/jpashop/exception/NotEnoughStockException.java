package jpabook.jpashop.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NotEnoughStockException extends RuntimeException {
	public NotEnoughStockException() {
		super();
	}

	public NotEnoughStockException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotEnoughStockException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughStockException(String message) {
		super(message);
	}

	public NotEnoughStockException(Throwable cause) {
		super(cause);
	}

	
}
