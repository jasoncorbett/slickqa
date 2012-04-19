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
    public void getIdTest()
    {
        assertNotNull("The string version of the id should not be null", project.getId());
        assertEquals("The string version of the id should equal getObjectId().toString()", project.getObjectId().toString(), project.getId());
        project = new Project();
        assertNull("The string version of the id should be null when the id is null.", project.getId());
    }

    @Test
    public void findReleaseTest() throws Exception
    {
        // first try to find a release with no releases initialized.
        assertNull("The result of trying to find a release when no releases are present should be null.", project.findRelease(ObjectId.get().toString()));

        Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("A Release");

        project.addRelease(release);
        assertSame("The release we just added should be the same as what is returned from findRelease.", release, project.findRelease(release.getId()));
    }

    @Test
    public void findReleaseByNameTest() throws Exception
    {
        // first try to find a release with no releases initialized.
        assertNull("The result of trying to find a release when no releases are present should be null.", project.findReleaseByName("Doesn't Matter"));

        Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("A Release");

        project.addRelease(release);
        assertSame("The release we just added should be the same as what is returned from findRelease.", release, project.findReleaseByName(release.getName()));
    }

    @Test
    public void addReleaseTest() throws Exception
    {
        // try to add a release when there are no releases initialized.
        Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("Foo");
        project.addRelease(release);
        assertNotNull("After adding a release, releases shouldn't be null.", project.getReleases());
        assertEquals("There should be exactly 1 release in the project as only 1 has been added.", 1, project.getReleases().size());
        assertSame("The release we added should be the same as the 1 and only release in the project's releases list.", release, project.getReleases().get(0));
    }

	@Test
	public void createReferenceTest()
	{
		ProjectReference ref = project.createReference();
		assertNotNull("The reference created by the project should not be null.", ref);
		assertEquals("The name in the reference should be the same as the name in the project.", project.getName(), ref.getName());
		assertEquals("The id of the object should be the same in the reference", project.getObjectId(), ref.getObjectId());
        assertEquals("The id of the object should be the same in the reference", project.getId(), ref.getId());
	}
}
