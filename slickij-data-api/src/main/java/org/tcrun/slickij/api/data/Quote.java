package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Quotes are just funny sayings that can be displayed on slick.  They serve no purpose except to make people
 * laugh.
 * User: jcorbett
 * Date: 2/21/13
 * Time: 2:28 PM
 */
@Entity("quotes")
public class Quote
{
    @Id
    private ObjectId id;

    @Property
    private String imageUrl;

    @Property
    private String quote;

    @Property
    private String attributed;

    @JsonIgnore
    public ObjectId getObjectId()
    {
        return id;
    }

    public String getId()
    {
        return id == null ? null : id.toString();
    }

    public void setId(ObjectId id)
    {
        this.id = id;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public String getAttributed()
    {
        return attributed;
    }

    public void setAttributed(String attributed)
    {
        this.attributed = attributed;
    }
}
