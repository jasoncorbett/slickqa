package org.tcrun.slickij.webgroup.plans;

import java.io.Serializable;
import org.tcrun.slickij.api.data.BuildReference;
import org.tcrun.slickij.api.data.ReleaseReference;

/**
 *
 * @author jcorbett
 */
class BuildReleaseReferenceCombo implements Serializable
{
	private BuildReference build;
	private ReleaseReference release;

	public BuildReleaseReferenceCombo(BuildReference build, ReleaseReference release)
	{
		this.build = build;
		this.release = release;
	}

	public BuildReference getBuild()
	{
		return build;
	}

	public void setBuild(BuildReference build)
	{
		this.build = build;
	}

	public ReleaseReference getRelease()
	{
		return release;
	}

	public void setRelease(ReleaseReference release)
	{
		this.release = release;
	}

	public String getId()
	{
		return release.getReleaseId() + "." + build.getBuildId();
	}

	public String getName()
	{
		return release.getName() + " Build " + build.getName();
	}
	
}
