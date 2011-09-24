package org.tcrun.slickij.webgroup.plans;

import org.tcrun.slickij.api.data.TestplanRunParameters;

/**
 *
 * @author jcorbett
 */
public class TestplanRunParametersWithCombinedBuildRelease extends TestplanRunParameters
{
	private BuildReleaseReferenceCombo buildRelease;

	public BuildReleaseReferenceCombo getBuildRelease()
	{
		return buildRelease;
	}

	public void setBuildRelease(BuildReleaseReferenceCombo buildRelease)
	{
		this.buildRelease = buildRelease;
		this.setBuild(buildRelease.getBuild());
		this.setRelease(buildRelease.getRelease());
	}
}
