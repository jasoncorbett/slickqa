package org.tcrun.slickij.api.data;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import static org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import static org.codehaus.jackson.annotate.JsonTypeInfo.As;

/**
 *
 * @author jcorbett
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="className")
public interface DataExtension<T>
{
	public String getId();

	public String getName();

	public String getSummary();

	public void setParent(T parent);

	public DataExtension update(DataExtension update);

	public void validate() throws InvalidDataError;
}
