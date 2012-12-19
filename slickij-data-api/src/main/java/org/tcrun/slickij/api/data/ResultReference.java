package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author jcorbett
 */
public class ResultReference implements Serializable, Copyable<ResultReference>
{
	@Property
	private ObjectId resultId;

	@Property
	private ResultStatus status;

	@Property
	private Date recorded;

	@Embedded
	private BuildReference build;

	public BuildReference getBuild()
	{
		return build;
	}

	public void setBuild(BuildReference build)
	{
		this.build = build;
	}

	public Date getRecorded()
	{
		return recorded;
	}

	public void setRecorded(Date recorded)
	{
		this.recorded = recorded;
	}

	@JsonIgnore
	public ObjectId getResultObjectId()
	{
		return resultId;
	}

	public String getResultId()
	{
		if(resultId == null)
			return null;
		else
			return resultId.toString();
	}

	public void setResultId(ObjectId resultId)
	{
		this.resultId = resultId;
	}

	public ResultStatus getStatus()
	{
		return status;
	}

	public void setStatus(ResultStatus status)
	{
		this.status = status;
	}

    @Override
    public ResultReference createCopy()
    {
        ResultReference copy = new ResultReference();

        copy.setBuild(getBuild().createCopy());
        copy.setRecorded(new Date(getRecorded().getTime()));
        copy.setResultId(getResultObjectId());
        copy.setStatus(getStatus());

        return copy;
    }
}
