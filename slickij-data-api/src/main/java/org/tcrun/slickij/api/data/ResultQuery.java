package org.tcrun.slickij.api.data;

import com.mongodb.DBObject;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/23/12
 * Time: 6:52 PM
 */
public interface ResultQuery
{
    public String getDescription();
    public void addToDBObject(DBObject obj);
}
