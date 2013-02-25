package org.tcrun.slickij.api.events;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.*;

import static org.fest.assertions.api.Assertions.*;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 3:01 PM
 */
public class SlickCreateEventTests
{
    public void VerifyRawMessage(String body, RawMessage message) throws Exception
    {
        assertThat(message.getContent()).isEqualTo((new ObjectMapper()).writeValueAsBytes(body));
        assertThat(message.getTopic()).isEqualTo("create.String");
        assertThat(message.getMessageTimestamp()).isNotNull();
        assertThat(message.getContentType()).isEqualTo("application/json");
        assertThat(message.getEncoding()).isEqualTo("UTF-8");
    }

    @Test
    public void VerifyCreateEvent() throws Exception
    {
        String body = "Gabe & Anna";
        SlickCreateEvent<String> stringCreateEvent = new SlickCreateEvent<String>(body);
        assertThat(stringCreateEvent.getTopic()).isNotNull().isEqualTo("create.String");
        assertThat(stringCreateEvent.getHeaders()).isNotNull();
        RawMessage message = stringCreateEvent.toRawMessage();
        VerifyRawMessage(body, message);
    }

    @Test
    public void VerifyCreateEventFromRawMessage() throws Exception
    {
        String body = "Gabe & Anna";
        RawMessage message = new SlickCreateEvent<String>(body).toRawMessage();

        SlickCreateEvent<String> slickCreateEvent = new SlickCreateEvent<String>(message, String.class);
        assertThat(slickCreateEvent.getBodyObject()).isEqualTo(body);
        assertThat(slickCreateEvent.getCreatedObject()).isEqualTo(body);
        assertThat(slickCreateEvent.getTopic()).isEqualTo("create.String");
        assertThat(slickCreateEvent.getMessageTimestamp()).isNotNull();

    }
}
