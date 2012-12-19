package org.tcrun.slickij.api.events;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 3:01 PM
 */
public class SlickUpdateEventTests
{
    public void VerifyRawMessage(String before, String after, RawMessage message) throws Exception
    {
        assertThat(message.getContent()).isEqualTo((new ObjectMapper()).writeValueAsBytes(new UpdateWrapper<String>(before, after)));
        assertThat(message.getTopic()).isEqualTo("update.String");
        assertThat(message.getMessageTimestamp()).isNotNull();
        assertThat(message.getContentType()).isEqualTo("application/json");
        assertThat(message.getEncoding()).isEqualTo("UTF-8");
    }

    @Test
    public void VerifyUpdateEvent() throws Exception
    {
        String before = "Gabe & Anna";
        String after = "Gabe an' Anna";
        SlickUpdateEvent<String> stringUpdateEvent = new SlickUpdateEvent<String>(before, after);
        assertThat(stringUpdateEvent.getTopic()).isNotNull().isEqualTo("update.String");
        assertThat(stringUpdateEvent.getHeaders()).isNotNull();
        RawMessage message = stringUpdateEvent.toRawMessage();
        VerifyRawMessage(before, after, message);
    }

    @Test
    public void VerifyUpdateEventFromRawMessage() throws Exception
    {
        String before = "Gabe & Anna";
        String after = "Gabe an' Anna";
        RawMessage message = new SlickUpdateEvent<String>(before, after).toRawMessage();

        SlickUpdateEvent<String> slickUpdateEvent = new SlickUpdateEvent<String>(message, new TypeReference<UpdateWrapper<String>>() {});
        assertThat(slickUpdateEvent.getBefore()).isEqualTo(before);
        assertThat(slickUpdateEvent.getAfter()).isEqualTo(after);
        assertThat(slickUpdateEvent.getTopic()).isEqualTo("update.String");
        assertThat(slickUpdateEvent.getMessageTimestamp()).isNotNull();

    }

    @Test
    public void VerifyUpdateEventWithoutBefore() throws Exception
    {
        String after = "Gabe an' Anna";
        RawMessage message = new SlickUpdateEvent<String>(null, after).toRawMessage();

        SlickUpdateEvent<String> slickUpdateEvent = new SlickUpdateEvent<String>(message, new TypeReference<UpdateWrapper<String>>() {});
        assertThat(slickUpdateEvent.getBefore()).isNull();
        assertThat(slickUpdateEvent.getAfter()).isEqualTo(after);
        assertThat(slickUpdateEvent.getTopic()).isEqualTo("update.String");
        assertThat(slickUpdateEvent.getMessageTimestamp()).isNotNull();

    }
}
