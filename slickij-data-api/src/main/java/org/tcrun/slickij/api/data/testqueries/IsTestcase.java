package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.tcrun.slickij.api.data.Testcase;
import org.tcrun.slickij.api.data.TestcaseReference;

/**
 *
 * @author jcorbett
 */
public class IsTestcase implements TestcaseQuery
{
	@Embedded
	private TestcaseReference ref;

	@Override
	public Criteria toMorphiaQuery(Query<Testcase> original)
	{
		if(ref.getTestcaseId() != null)
			return original.criteria("id").equal(ref.getActualId());
		else if(ref.getAutomationKey() != null)
			return original.criteria("automationKey").equal(ref.getAutomationKey());
		else if(ref.getAutomationId() != null)
			return original.criteria("automationId").equal(ref.getAutomationId());
		else if(ref.getName() != null)
			return original.criteria("name").equal(ref.getName());
		else
			return null;
	}

	@Override
	public String getQueryDescription()
	{
		String retval = "Is testcase with ";

		if(ref.getTestcaseId() != null)
			retval += "id='" + ref.getTestcaseId() + "'";
		else if(ref.getAutomationKey() != null)
			retval += "automationKey='" + ref.getAutomationKey() + "'";
		else if(ref.getAutomationId() != null)
			retval += "automationId='" + ref.getAutomationId() + "'";
		else if(ref.getName() != null)
			retval += "name='" + ref.getName() + "'";
		else
			retval += "unknown";

		return retval;
	}

	public TestcaseReference getRef()
	{
		return ref;
	}

	public void setRef(TestcaseReference ref)
	{
		this.ref = ref;
	}

    @Override
    public TestcaseQuery createCopy()
    {
        IsTestcase copy = new IsTestcase();
        copy.setRef(ref.createCopy());
        return copy;
    }
}
