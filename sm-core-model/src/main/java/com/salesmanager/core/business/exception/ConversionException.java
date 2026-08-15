/**
 * 
 */
package com.salesmanager.core.business.exception;

/**
 * Represents an exception that occurs during the conversion process.
 * A detailed message and cause can be provided to describe the specific conversion problem.
 * This exception extends from Java's base Exception class.
 * It is typically used to wrap any lower-level exceptions encountered during conversion.
 * Author: Umesh A
 */
public class ConversionException extends Exception {
  private static final long serialVersionUID = 687400310032876603L;
  
  public ConversionException(final String msg, final Throwable cause) {
      super(msg, cause);
  }

  public ConversionException(final String msg) {
      super(msg);
  }
  
  public ConversionException(Throwable t) {
      super(t);
  }
}