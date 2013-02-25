package org.tcrun.slickij.api.events;

/**
 * User: jcorbett
 * Date: 11/29/12
 * Time: 10:47 AM
 */
public interface SlickEvent
{
    public RawMessage toRawMessage() throws SlickEventException;
    public void loadFromRawMessage(RawMessage message) throws SlickEventException;
}
