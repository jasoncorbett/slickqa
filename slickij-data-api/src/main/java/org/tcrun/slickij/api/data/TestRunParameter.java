package org.tcrun.slickij.api.data;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author jcorbett
 */
public class TestRunParameter implements Serializable
{
	private Map<String, String> fulFilledRequirements;

	private String automationTool;

	private String hostname;

	public String getAutomationTool()
	{
		return automationTool;
	}

	public void setAutomationTool(String automationTool)
	{
		this.automationTool = automationTool;
	}

	public Map<String, String> getFulFilledRequirements()
	{
		return fulFilledRequirements;
	}

	public void setFulFilledRequirements(Map<String, String> fulFilledRequirements)
	{
		this.fulFilledRequirements = fulFilledRequirements;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}


}
