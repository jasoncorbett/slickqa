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
	
	@Test
	public void findBuildByNameTest() throws Exception
    {
        // first try to find a build with no builds initialized.
	Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("A Release");
	release.postLoad(); // Ensure the build list is valid

        project.addRelease(release);
        assertNull("The result of trying to find a build when no builds are present should be null.", project.findBuildByName(release.getId(), "Doesn't Matter"));

	Build build = new Build();
	build.setId(ObjectId.get());
	build.setName("A Build");
	
	release.getBuilds().add(build);
        assertSame("The build we just added should be the same as what is returned from findBuildByName.", build, project.findBuildByName(release.getId(), build.getName()));
    }

    @Test
    public void activateReleaseTest() throws Exception
    {
        Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("BaldEagle");
        project.addRelease(release);
        project.deactivateRelease(release);
        project.activateRelease(release);
        assertNotNull("After activating a release, releases shouldn't be null.", project.getReleases());
        assertEquals("There should be exactly 0 inactive releases in the project since the only release has been activated.", 0, project.getInactiveReleases().size());
        assertEquals("There should be exactly 1 release in the project as only 1 has been added.", 1, project.getReleases().size());
        assertSame("The release we added should be the same as the 1 and only release in the project's releases list.", release, project.getReleases().get(0));
    }
    
    @Test
    public void deactivateReleaseTest() throws Exception
    {
        Release release = new Release();
        release.setId(ObjectId.get());
        release.setName("BaldOne");
        project.addRelease(release);
        project.deactivateRelease(release);
        assertNotNull("After deactivating a release, inactiveReleases shouldn't be null.", project.getInactiveReleases());
        assertEquals("There should be exactly 0 release in the project since the only release has been deactivated.", 0, project.getReleases().size());
        assertEquals("There should be exactly 1 inactive release in the project as only 1 has been added.", 1, project.getInactiveReleases().size());
        assertSame("The release we added should be the same as the 1 and only release in the project's inactiveReleases list.", release, project.getInactiveReleases().get(0));
    }
}
