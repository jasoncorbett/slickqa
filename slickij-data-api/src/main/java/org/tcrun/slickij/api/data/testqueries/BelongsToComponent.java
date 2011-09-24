package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.bson.types.ObjectId;
import org.tcrun.slickij.api.data.Testcase;

/**
 *
 * @author jcorbett
 */
public class BelongsToComponent implements TestcaseQuery
{
	private ObjectId componentId;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		return original.criteria("component.id").equal(componentId);
	}

	@Override
	public String getQueryDescription()
	{
		return "references component with id '" + componentId.toString() + "'.";
	}

	public ObjectId getComponentId()
	{
		return componentId;
	}

	public void setComponentId(ObjectId componentId)
	{
		this.componentId = componentId;
	}
}
