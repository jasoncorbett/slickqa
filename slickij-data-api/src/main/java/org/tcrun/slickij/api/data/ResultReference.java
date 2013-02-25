package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import static org.tcrun.slickij.api.data.CopyUtil.copyDateIfNotNull;

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
        copy.setRecorded(copyDateIfNotNull(getRecorded()));
        copy.setResultId(getResultObjectId());
        copy.setStatus(getStatus());

        return copy;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultReference that = (ResultReference) o;

        if (build != null ? !build.equals(that.build) : that.build != null) return false;
        if (recorded != null ? !recorded.equals(that.recorded) : that.recorded != null) return false;
        if (resultId != null ? !resultId.equals(that.resultId) : that.resultId != null) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = resultId != null ? resultId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (recorded != null ? recorded.hashCode() : 0);
        result = 31 * result + (build != null ? build.hashCode() : 0);
        return result;
    }
}
