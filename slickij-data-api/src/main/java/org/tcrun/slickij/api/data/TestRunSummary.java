package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jcorbett
 */
public class TestRunSummary implements Serializable, Copyable<TestRunSummary>
{
	private static final Map<String, Integer> statusOrderValues;
	static {
		statusOrderValues = new HashMap<String, Integer>();
		statusOrderValues.put(ResultStatus.PASS.toString(), new Integer(1));
		statusOrderValues.put(ResultStatus.FAIL.toString(), new Integer(2));
		statusOrderValues.put(ResultStatus.BROKEN_TEST.toString(), new Integer(3));
		statusOrderValues.put(ResultStatus.NOT_TESTED.toString(), new Integer(4));
		statusOrderValues.put(ResultStatus.SKIPPED.toString(), new Integer(5));
		statusOrderValues.put(ResultStatus.NO_RESULT.toString(), new Integer(6));
	}

    @Property
	private Map<String, Long> resultsByStatus;

    private int totalTime;

	public TestRunSummary()
	{
		resultsByStatus = new HashMap<String, Long>();
        for(ResultStatus status : ResultStatus.values())
        {
            resultsByStatus.put(status.toString(), 0L);
        }
	}

	public Map<String, Long> getResultsByStatus()
	{
		return resultsByStatus;
	}

	public void setResultsByStatus(Map<String, Long> resultsByStatus)
	{
		this.resultsByStatus = resultsByStatus;
	}

	public List<String> getStatusListOrdered()
	{
        List<String> statusListOrdered = new ArrayList<String>();
        for(String key : resultsByStatus.keySet())
        {
            if(resultsByStatus.get(key) > 0)
                statusListOrdered.add(key);
        }

        Collections.sort(statusListOrdered, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2)
            {
            Integer o1SortOrder = statusOrderValues.get(o1);
            if(o1SortOrder == null)
                o1SortOrder = 50;
            Integer o2SortOrder = statusOrderValues.get(o2);
            if(o2SortOrder == null)
                o2SortOrder = 50;
            return o1SortOrder.compareTo(o2SortOrder);
            }

        });

		return statusListOrdered;
	}

    public void setStatusListOrdered(List<String> val)
    {
        // do nothing, just here to avoid json serialization errors
    }

	public Long getTotal()
	{
		Long retval = 0L;
		for(Long bystatus : getResultsByStatus().values())
			retval += bystatus;
		return retval;
	}

    public void setTotal(Long val)
    {
        // do nothing, just here to avoid json serialization errors
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public TestRunSummary createCopy()
    {
        TestRunSummary copy = new TestRunSummary();

        copy.setTotalTime(totalTime);
        copy.setResultsByStatus(new HashMap<String, Long>(resultsByStatus));

        return copy;
    }
}
