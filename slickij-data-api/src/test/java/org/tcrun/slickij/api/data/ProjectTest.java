package org.tcrun.slickij.api.data;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jcorbett
 */
public class ProjectTest
{
	private Project project;

	@Before
	public void setup()
	{
		project = new Project();
		project.setName("Foo Project");
		project.setId(ObjectId.get());
	}

	@Test
	public void createReferenceTest()
	{
		ProjectReference ref = project.createReference();
		assertNotNull("The reference created by the project should not be null.", ref);
		assertEquals("The name in the reference should be the same as the name in the project.", project.getName(), ref.getName());
		assertEquals("The id of the object should be the same in the reference", project.getObjectId(), ref.getObjectId());
	}
}
