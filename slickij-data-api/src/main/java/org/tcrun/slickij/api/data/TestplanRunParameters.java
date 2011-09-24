package org.tcrun.slickij.api.data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jcorbett
 */
public class TestplanRunParameters implements Serializable
{
	private ConfigurationReference config;

	private ReleaseReference release;

	private BuildReference build;

	private List<ConfigurationOverride> overrides;

	public BuildReference getBuild()
	{
		return build;
	}

	public void setBuild(BuildReference build)
	{
		this.build = build;
	}

	public ConfigurationReference getConfig()
	{
		return config;
	}

	public void setConfig(ConfigurationReference config)
	{
		this.config = config;
	}

	public List<ConfigurationOverride> getOverrides()
	{
		return overrides;
	}

	public void setOverrides(List<ConfigurationOverride> overrides)
	{
		this.overrides = overrides;
	}

	public ReleaseReference getRelease()
	{
		return release;
	}

	public void setRelease(ReleaseReference release)
	{
		this.release = release;
	}

}
