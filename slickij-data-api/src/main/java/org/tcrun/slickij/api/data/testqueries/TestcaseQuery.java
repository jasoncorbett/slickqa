/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcrun.slickij.api.data.testqueries;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.tcrun.slickij.api.data.Testcase;
import static org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import static org.codehaus.jackson.annotate.JsonTypeInfo.As;

/**
 *
 * @author jcorbett
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="className")
public interface TestcaseQuery
{
	public Criteria toMorphiaQuery(Query<Testcase> original);

	public String getQueryDescription();
}
