package org.tcrun.slickij.core;

import com.google.code.morphia.Morphia;

/**
 *
 * @author jcorbett
 */
public interface DataPluginModule
{
	public void setMorphiaInstance(Morphia morphia);
}
