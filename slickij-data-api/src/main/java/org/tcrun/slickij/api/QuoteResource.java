package org.tcrun.slickij.api;

import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.InvalidDataError;
import org.tcrun.slickij.api.data.Quote;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource (REST Endpoint) for getting, updating and deleting quotes.
 * User: jcorbett
 * Date: 2/21/13
 * Time: 2:33 PM
 */
@Path("/quotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface QuoteResource
{
    @GET
    public List<Quote> getQuotes();

    @GET
    @Path("/{id}")
    public Quote getQuote(@PathParam("id") ObjectId quoteId);

    @GET
    @Path("/random")
    public Quote getRandomQuote();

    @POST
    public Quote createQuote(Quote quote);

    @PUT
    @Path("/{id}")
    public Quote updateQuote(@PathParam("id") ObjectId quoteId, Quote update);

    @DELETE
    @Path("/{id}")
    public Quote deleteQuote(@PathParam("id") ObjectId quoteId);
}
