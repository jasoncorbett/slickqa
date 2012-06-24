package org.tcrun.slickij.api.data;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/23/12
 * Time: 6:47 PM
 */
public class ResultGroupSummary
{
    private static Map<String, Integer> statusOrderValues;
    static {
        statusOrderValues = new HashMap<String, Integer>();
        statusOrderValues.put(ResultStatus.PASS.toString(), 1);
        statusOrderValues.put(ResultStatus.FAIL.toString(), 2);
        statusOrderValues.put(ResultStatus.BROKEN_TEST.toString(), 3);
        statusOrderValues.put(ResultStatus.NOT_TESTED.toString(), 4);
        statusOrderValues.put(ResultStatus.SKIPPED.toString(), 5);
        statusOrderValues.put(ResultStatus.NO_RESULT.toString(), 6);
    }

    private Map<String, Long> resultsByStatus;
    private ResultQuery resultQuery;
    private List<String> statusListOrdered;
    private Integer totalTime;

    public ResultGroupSummary()
    {
        resultsByStatus = new HashMap<String, Long>();
    }

    public ResultQuery getResultQuery() {
        return resultQuery;
    }

    public void setResultQuery(ResultQuery resultQuery) {
        this.resultQuery = resultQuery;
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
        if(statusListOrdered == null || statusListOrdered.isEmpty())
        {
            this.statusListOrdered = new ArrayList<String>(getResultsByStatus().keySet());
            Collections.sort(statusListOrdered, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    Integer o1SortOrder = statusOrderValues.get(o1);
                    if (o1SortOrder == null)
                        o1SortOrder = 50;
                    Integer o2SortOrder = statusOrderValues.get(o2);
                    if (o2SortOrder == null)
                        o2SortOrder = 50;
                    return o1SortOrder.compareTo(o2SortOrder);
                }

            });

        }
        return statusListOrdered;
    }

    public Long getTotal()
    {
        Long retval = 0L;
        for(Long bystatus : getResultsByStatus().values())
            retval += bystatus;
        return retval;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }
}
