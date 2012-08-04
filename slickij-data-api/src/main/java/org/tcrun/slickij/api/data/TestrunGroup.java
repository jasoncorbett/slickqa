package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A testrun group is a group of testruns (duh).  It's a reporting element that allows you to create a report
 * based on several testruns together.
 * User: jcorbett
 * Date: 8/3/12
 * Time: 1:18 PM
 */
@Entity("testrungroups")
public class TestrunGroup
{
    @Id
    private ObjectId id;

    @Property
    private String name;

    @Property
    private Date created;

    @Reference
    private List<Testrun> testruns;

    public ObjectId getId()
    {
        return id;
    }

    public void setId(ObjectId id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public List<Testrun> getTestruns()
    {
        return testruns;
    }

    public void setTestruns(List<Testrun> testruns)
    {
        this.testruns = testruns;
    }

    public TestRunSummary getGroupSummary()
    {
        TestRunSummary summary = new TestRunSummary();
        if(testruns != null)
        {
            for(Testrun run : testruns)
            {
                for(String status : run.getSummary().getStatusListOrdered())
                {
                    summary.getResultsByStatus().put(status, summary.getResultsByStatus().get(status) +
                                                             run.getSummary().getResultsByStatus().get(status));
                }
            }
        }
        return summary;
    }

    public void validate() throws InvalidDataError
    {
        if(name == null || name.isEmpty())
            throw new InvalidDataError("TestrunGroup", "name", "name cannot be null or empty.");
        if(testruns == null)
            testruns = new ArrayList<Testrun>();
        if(created == null)
            created = new Date();
    }

    @PostLoad
    public void onLoad()
    {
        if(testruns == null)
            testruns = new ArrayList<Testrun>();
    }
}
