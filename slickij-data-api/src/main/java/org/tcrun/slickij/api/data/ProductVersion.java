package org.tcrun.slickij.api.data;

import java.io.Serializable;

/**
 *
 * @author jcorbett
 */
public class ProductVersion implements Serializable
{
	private String productName;
	private String versionString;

	public ProductVersion(String productName, String versionString)
	{
		this.productName = productName;
		this.versionString = versionString;
	}

	public ProductVersion()
	{
		this.productName = null;
		this.versionString = null;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getVersionString()
	{
		return versionString;
	}

	public void setVersionString(String versionString)
	{
		this.versionString = versionString;
	}

}