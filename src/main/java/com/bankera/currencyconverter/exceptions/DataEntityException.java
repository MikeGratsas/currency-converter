package com.bankera.currencyconverter.exceptions;

public class DataEntityException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6812686656525140683L;

	private final Long id;

	public DataEntityException(String message, Long id) {
        super(message + ": id = " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
