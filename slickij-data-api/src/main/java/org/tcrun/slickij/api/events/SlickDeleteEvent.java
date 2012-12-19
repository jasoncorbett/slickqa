package org.tcrun.slickij.api.events;

import org.codehaus.jackson.type.TypeReference;

/**
 * User: jcorbett
 * Date: 11/29/12
 * Time: 4:37 PM
 */
public class SlickDeleteEvent<T> extends AbstractSlickEvent<T>
{
    private T body;
    private Class<? extends T> bodyClass;

    @SuppressWarnings("unchecked")
    public SlickDeleteEvent(T instance)
    {
        super();
        setTopic("delete." + instance.getClass().getSimpleName());
        this.bodyClass = (Class<? extends T>) instance.getClass();
        body = instance;
    }

    public SlickDeleteEvent(RawMessage message, Class<? extends T> bodyClass) throws SlickEventException
    {
        super();
        this.bodyClass = bodyClass;
        loadFromRawMessage(message);
    }

    @Override
    protected TypeReference<T> getTypeReference()
    {
        return null;
    }

    @Override
    protected Class<? extends T> getBodyClass()
    {
        return bodyClass;
    }

    @Override
    protected T getBodyObject()
    {
        return body;
    }

    @Override
    protected void setBodyObject(T instance)
    {
        body = instance;
    }

    public T getDeletedObject()
    {
        return body;
    }
}
