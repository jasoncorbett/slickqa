package org.tcrun.slickij.api.events;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 3:01 PM
 */
public class SlickDeleteEventTests
{
    public void VerifyRawMessage(String body, RawMessage message) throws Exception
    {
        assertThat(message.getContent()).isEqualTo((new ObjectMapper()).writeValueAsBytes(body));
        assertThat(message.getTopic()).isEqualTo("delete.String");
        assertThat(message.getMessageTimestamp()).isNotNull();
        assertThat(message.getContentType()).isEqualTo("application/json");
        assertThat(message.getEncoding()).isEqualTo("UTF-8");
    }

    @Test
    public void VerifyDeleteEvent() throws Exception
    {
        String body = "Gabe & Anna";
        SlickDeleteEvent<String> stringCreateEvent = new SlickDeleteEvent<String>(body);
        assertThat(stringCreateEvent.getTopic()).isNotNull().isEqualTo("delete.String");
        assertThat(stringCreateEvent.getHeaders()).isNotNull();
        RawMessage message = stringCreateEvent.toRawMessage();
        VerifyRawMessage(body, message);
    }

    @Test
    public void VerifyDeleteEventFromRawMessage() throws Exception
    {
        String body = "Gabe & Anna";
        RawMessage message = new SlickDeleteEvent<String>(body).toRawMessage();

        SlickDeleteEvent<String> slickDeleteEvent = new SlickDeleteEvent<String>(message, String.class);
        assertThat(slickDeleteEvent.getBodyObject()).isEqualTo(body);
        assertThat(slickDeleteEvent.getDeletedObject()).isEqualTo(body);
        assertThat(slickDeleteEvent.getTopic()).isEqualTo("delete.String");
        assertThat(slickDeleteEvent.getMessageTimestamp()).isNotNull();

    }
}
