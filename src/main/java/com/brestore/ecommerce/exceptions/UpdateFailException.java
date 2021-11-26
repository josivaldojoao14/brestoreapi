package com.brestore.ecommerce.exceptions;

public class UpdateFailException extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdateFailException(String msg) {
        super(msg);
    }
}
