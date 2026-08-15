/**
 * 
 */
package com.salesmanager.core.business.exception;

/**
 * Custom exception class for conversion-related errors.
 * 
 * Represents an error during the conversion process.
 * This class extends the base Exception class.
 * It provides multiple constructors to create instances
 * with a custom message and an optional cause.
 * 
 * @version 1.0
 * @since 2.0
 * 
 * @see java.lang.Exception
 * 
 * Author: Umesh A
 */
public class ConversionException extends Exception
{
  private static final long serialVersionUID = 687400310032876603L;
  
  public ConversionException(final String msg, final Throwable cause)
  {
      super(msg, cause);
  }

  public ConversionException(final String msg)
  {
      super(msg);
  }
  
  public ConversionException(Throwable t)
  {
      super(t);
  }
}