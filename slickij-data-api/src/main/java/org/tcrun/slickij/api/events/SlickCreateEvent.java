package org.tcrun.slickij.api.events;

import org.codehaus.jackson.type.TypeReference;

/**
 * User: jcorbett
 * Date: 11/29/12
 * Time: 1:17 PM
 */
public class SlickCreateEvent<T> extends AbstractSlickEvent<T>
{
    private T instance;
    private Class<? extends T> bodyClass;

    @SuppressWarnings("unchecked")
    public SlickCreateEvent(T instance)
    {
        super();
        setTopic("create." + instance.getClass().getSimpleName());
        this.instance = instance;
        bodyClass = (Class<? extends T>) instance.getClass();
    }

    public SlickCreateEvent(RawMessage message, Class<? extends T> bodyClass) throws SlickEventException
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
    protected Class<? extends T> getBodyClass() {
        return bodyClass;
    }

    @Override
    protected T getBodyObject()
    {
        return instance;
    }

    @Override
    protected void setBodyObject(T instance)
    {
        this.instance = instance;
    }

    public T getCreatedObject()
    {
        return instance;
    }
}
