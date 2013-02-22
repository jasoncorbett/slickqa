package org.tcrun.slickij.api.data.dao;

import com.google.code.morphia.dao.DAO;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Quote;

/**
 * Morphia DAO to access quotes stored in mongodb.
 * User: jcorbett
 * Date: 2/21/13
 * Time: 2:39 PM
 */
public interface QuoteDAO extends DAO<Quote, ObjectId>
{
    public Quote getRandomQuote();
}
