package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Quote;
import org.tcrun.slickij.api.data.dao.QuoteDAO;

/**
 * Implementation of Quote DAO.
 * User: jcorbett
 * Date: 2/21/13
 * Time: 2:47 PM
 */
public class QuoteDAOImpl extends BasicDAO<Quote, ObjectId> implements QuoteDAO
{
    private RandomDataGenerator random;

    @Inject
    public QuoteDAOImpl(Datastore ds)
    {
        super(Quote.class, ds);
        random = new RandomDataGenerator();
    }

    @Override
    public Quote getRandomQuote()
    {
        long count = this.count();
        long rand = random.nextLong(0L, count);
        if(rand == count)
            rand = count - 1;
        return ds.createQuery(Quote.class).offset((int)rand).get();
    }
}
