package org.tcrun.slickij.api.events;

import java.util.Date;
import java.util.Map;

/**
 * This interface describes a raw message used by slick to send events.  Raw Messages are the base type of what gets
 * sent by the MessageBus.
 *
 * User: jcorbett
 * Date: 10/30/12
 * Time: 2:33 PM
 */
public interface RawMessage
{
    /**
     * The header key for the event's data class name.  This should a full class name (with package).  The topic
     * will usually have the shortened class name, however this may be necessary for being absolutely sure when
     * deserializing the data.  Note that the data class could be a wrapper for embedded data (as is the case with
     * update events).
     */
    public static String HEADER_KEY_EVENT_CLASS_NAME = "slick.dataClassName";

    /**
     * The topic for the message is also called the "routing key" in AMQP language.  In the case of slick
     * we will use a pattern of action.datatype.  A few good examples would be:
     * <ul>
     *     <li>create.project</li>
     *     <li>update.testrun</li>
     *     <li>notify.result</li>
     * </ul>
     *
     * @return The topic for the message.
     */
    public String getTopic();

    /**
     * A MIME content type for the encoding of the message.  Typically in slick we will use JSON (applicaion/json), but
     * this property is here in case we decide to change that for any reason.
     *
     * @return the MIME content type the message uses for encoding.
     */
    public String getContentType();

    /**
     * The content encoding of the message.  As bytes are used for the body of the message we need to specify what
     * kind of encoding is used.  Usually we will use UTF-8 with JSON, but this method is available in case you need
     * to customize a message with a different encoding.
     *
     * @return A string describing the content encoding, like UTF8.
     */
    public String getEncoding();

    /**
     * Get a timestamp for when the message is sent (or composed).  This property should not be depended on for timing
     * information as it could get set when the message is composed, or when it was sent.  It could also be null.
     *
     * @return A date time object set around the time the message is being sent.
     */
    public Date getMessageTimestamp();

    /**
     * The content of the message in bytes.  This is because the content does not have to be plain text.
     *
     * @return The bytes containing the message of the event.  Usually serialized data.
     */
    public byte[] getContent();

    /**
     * Headers (key => value properties) for the event.  A typical use would be to contain any additional meta-data about
     * the content that is needed.  There are several standard headers that should normally be set.  These headers are
     * defined in this interface as static strings that start with the name HEADER_KEY_ at the beginning of the variable
     * name.
     *
     * @return A map of key->value properties to set in the header of the message.
     */
    public Map<String, String> getHeaders();
}
