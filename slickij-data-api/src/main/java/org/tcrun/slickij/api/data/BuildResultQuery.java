package org.tcrun.slickij.api.data;

import com.mongodb.DBObject;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/23/12
 * Time: 7:19 PM
 */
public class BuildResultQuery implements ResultQuery
{
    private BuildReference buildRef;

    public BuildResultQuery(BuildReference reference)
    {
        this.buildRef = reference;
    }

    @Override
    public String getDescription()
    {
        String name = "?";
        if(buildRef != null && buildRef.getName() != null)
            name = buildRef.getName();
        return "Results for Build " + name;
    }

    @Override
    public void addToDBObject(DBObject obj)
    {
        obj.put("build.buildId", buildRef.getBuildObjectId());
    }
}
