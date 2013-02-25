package org.tcrun.slickij.api.events;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 11/29/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRawMessage  implements RawMessage
{
    private String topic;
    private String contentType;
    private String encoding;
    private Date messageTimestamp;
    private byte[] content;
    private Map<String, String> headers;

    public SimpleRawMessage()
    {
        content = new byte[0];
        headers = new HashMap<String, String>();
    }

    public SimpleRawMessage(String topic, String contentType, String encoding, Date messageTimestamp, byte[] content, Map<String, String> headers)
    {
        this.topic = topic;
        this.contentType = contentType;
        this.encoding = encoding;
        this.messageTimestamp = messageTimestamp;
        this.content = content;
        this.headers = headers;
    }

    public SimpleRawMessage(RawMessage message)
    {
        this.topic = message.getTopic();
        this.contentType = message.getContentType();
        this.encoding = message.getEncoding();
        this.messageTimestamp = message.getMessageTimestamp();
        this.content = Arrays.copyOf(message.getContent(), message.getContent().length);
        this.headers = new HashMap<String, String>(message.getHeaders());
    }

    @Override
    public String getTopic()
    {
        return topic;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public String getEncoding()
    {
        return encoding;
    }

    @Override
    public Date getMessageTimestamp()
    {
        return messageTimestamp;
    }

    @Override
    public byte[] getContent()
    {
        return content;
    }

    @Override
    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setMessageTimestamp(Date messageTimestamp)
    {
        this.messageTimestamp = messageTimestamp;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public void setHeader(String key, String value)
    {
        if(headers == null)
        {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    public String getHeader(String key)
    {
        if(headers != null && headers.containsKey(key))
        {
            return headers.get(key);
        } else
        {
            return null;
        }
    }
}
