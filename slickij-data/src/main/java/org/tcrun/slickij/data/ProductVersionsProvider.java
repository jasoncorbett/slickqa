package org.tcrun.slickij.data;

import com.google.inject.Provider;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcrun.slickij.api.data.ProductVersion;

/**
 *
 * @author jcorbett
 */
public class ProductVersionsProvider implements Provider<ProductVersionMap>
{
	private static Logger logger = LoggerFactory.getLogger(ProductVersionsProvider.class);

	@Override
	public ProductVersionMap get()
	{
		ProductVersionMap retval = new ProductVersionMap();
		try
		{
			Enumeration<URL> versions = this.getClass().getClassLoader().getResources("product.version");
			while (versions.hasMoreElements())
			{
				URL version = versions.nextElement();
				logger.debug("Found product version resource '{}'.", version);
				Properties versionProperties = new Properties();
				versionProperties.load(version.openStream());
				if (versionProperties.containsKey("product.name") && versionProperties.containsKey("product.version"))
				{
						ProductVersion productversion = new ProductVersion(versionProperties.getProperty("product.name"),
						                                                   versionProperties.getProperty("product.version"));
						retval.put(productversion.getProductName(), productversion);
				}
				
			}
		} catch (Exception e)
		{
			System.out.println("Exception caught in loading versions: " + e.getMessage());
			e.printStackTrace(System.out);
		}
		return retval;
	}
	
}
