package org.tcrun.slickij.data;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.QuoteResource;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.Quote;
import org.tcrun.slickij.api.data.dao.QuoteDAO;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Implementation of the Quote REST Endpoint.
 * User: jcorbett
 * Date: 2/21/13
 * Time: 3:10 PM
 */
public class QuoteResourceImpl implements QuoteResource
{
    private QuoteDAO qdao;

    @Inject
    public QuoteResourceImpl(QuoteDAO qdao)
    {
        this.qdao = qdao;
    }

    @Override
    public List<Quote> getQuotes()
    {
        return qdao.find().asList();
    }

    @Override
    public Quote getQuote(@PathParam("id") ObjectId quoteId)
    {
        return qdao.get(quoteId);
    }

    @Override
    public Quote getRandomQuote()
    {
        return qdao.getRandomQuote();
    }

    @Override
    public Quote createQuote(Quote quote)
    {
        if(quote.getQuote() == null)
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        qdao.save(quote);

        return quote;
    }

    @Override
    public Quote updateQuote(@PathParam("id") ObjectId quoteId, Quote update)
    {
        Quote q = getQuote(quoteId);
        if(q != null)
        {
            if(update.getImageUrl() != null)
                q.setImageUrl(update.getImageUrl());
            if(update.getQuote() != null)
                q.setQuote(update.getQuote());
            if(update.getAttributed() != null)
                q.setAttributed(update.getAttributed());
            qdao.save(q);
            return q;
        }
        return null;
    }

    @Override
    public Quote deleteQuote(@PathParam("id") ObjectId quoteId)
    {
        Quote q = getQuote(quoteId);
        if(q != null)
        {
            qdao.delete(q);
        }
        return q;
    }
}
