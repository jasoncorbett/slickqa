package org.tcrun.slickij.data;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.tcrun.slickij.api.VersionResource;
import org.tcrun.slickij.api.data.ProductVersion;

/**
 *
 * @author jcorbett
 */
public class VersionResourceImpl implements VersionResource
{

	@Inject
	ProductVersionMap versions;
	

	@Override
	public List<ProductVersion> getProductVersions()
	{
		return new ArrayList<ProductVersion>(versions.values());
	}

	@Override
	public ProductVersion getVersionOfProductWithName(String productname)
	{
		if(!versions.containsKey(productname))
			throw new NotFoundError(ProductVersion.class, productname);
		return versions.get(productname);
	}
	
}
