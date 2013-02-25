package org.tcrun.slickij.api.events;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.Date;
import java.util.Map;

/**
 * User: jcorbett
 * Date: 11/29/12
 * Time: 11:33 AM
 */
public abstract class AbstractSlickEvent<T> implements SlickEvent
{
    protected SimpleRawMessage rawMessage;

    public AbstractSlickEvent()
    {
        rawMessage = new SimpleRawMessage();
        rawMessage.setContentType("application/json");
        rawMessage.setEncoding("UTF-8");
    }

    protected abstract TypeReference<T> getTypeReference();
    protected abstract Class<? extends T> getBodyClass();
    protected abstract T getBodyObject();
    protected abstract void setBodyObject(T instance);

    @Override
    public RawMessage toRawMessage() throws SlickEventException
    {
        ObjectMapper mapper = new ObjectMapper();
        rawMessage.setMessageTimestamp(new Date());
        try
        {
            rawMessage.setContent(mapper.writeValueAsBytes(getBodyObject()));
        } catch (Exception ex)
        {
            throw new SlickEventException("Unable to serialize to JSON the body object: " + ex.getMessage(), ex);
        }
        return rawMessage;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromRawMessage(RawMessage message) throws SlickEventException
    {
        this.rawMessage = new SimpleRawMessage(message);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            if(getTypeReference() != null)
            {
                setBodyObject((T) mapper.readValue(message.getContent(), getTypeReference()));
            } else
            {
                setBodyObject(mapper.readValue(message.getContent(), getBodyClass()));
            }
        } catch (Exception ex)
        {
            throw new SlickEventException("Unable to load body from message with topic [" + message.getTopic() + "]: " + ex.getMessage(), ex);
        }
    }

    public Date getMessageTimestamp()
    {
        return rawMessage.getMessageTimestamp();
    }

    public Map<String, String> getHeaders()
    {
        return rawMessage.getHeaders();
    }

    public void setHeader(String key, String value)
    {
        rawMessage.setHeader(key, value);
    }

    public String getHeader(String key)
    {
        return rawMessage.getHeader(key);
    }

    public void setTopic(String topic)
    {
        rawMessage.setTopic(topic);
    }

    public String getTopic() {
        return rawMessage.getTopic();
    }
}
