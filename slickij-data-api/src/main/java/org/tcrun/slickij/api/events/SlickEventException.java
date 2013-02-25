package org.tcrun.slickij.api.events;

/**
 * An exception that is used for error handling during the event publishing and response processes.
 * User: jcorbett
 * Date: 10/30/12
 * Time: 2:52 PM
 */
public class SlickEventException extends Exception
{
    public SlickEventException(String message)
    {
        super(message);
    }

    public SlickEventException(String message, Exception inner)
    {
        super(message, inner);
    }
}
