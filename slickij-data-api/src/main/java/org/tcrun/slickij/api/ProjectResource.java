package org.tcrun.slickij.api;

import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Release;
import org.tcrun.slickij.api.data.Build;
import org.tcrun.slickij.api.data.Component;
import org.tcrun.slickij.api.data.DataDrivenPropertyType;
import org.tcrun.slickij.api.data.DataExtension;

/**
 *
 * @author jcorbett
 */
@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectResource
{
	@Produces("application/json")
	@GET
	public List<Project> getAllMatchingProjects(@Context UriInfo uriInfo);

	@Path("/{id}")
	@Produces("application/json")
	@GET
	public Project getProjectById(@PathParam("id") String id);

	@Path("/byname/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Project getProjectByName(@PathParam("name") String name);

	@Path("/{id}")
	@Produces("application/json")
	@DELETE
	public void deleteProjectById(@PathParam("id") String id);

	@Path("/{id}/name")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public Project changeProjectName(@PathParam("id") String id, String name);

	@Path("/{id}/description")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public Project changeProjectDescription(@PathParam("id") String id, String description);

	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public Project createNewProject(Project project);

	@Path("/{projectid}/attributes")
	@Produces("application/json")
	@GET
	public Map<String, String> getAttributes(@PathParam("projectid") String projectId);

	@Path("/{projectid}/attributes")
	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public Map<String, String> addAttributes(@PathParam("projectid") String projectId, Map<String, String> attributes);

	@Path("/{projectid}/attributes/{attributeName}")
	@Produces("application/json")
	@DELETE
	public Map<String, String> deleteAttribute(@PathParam("projectid") String projectid, @PathParam("attributeName") String attributeName);

	@Path("/{projectid}/releases")
	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public Release addRelease(@PathParam("projectid") String projectId, Release release);

	@Path("/{projectid}/releases")
	@Produces("application/json")
	@GET
	public List<Release> getReleases(@PathParam("projectid") String projectId);

	@Path("/{projectid}/releases/{releaseid}")
	@Produces("application/json")
	@GET
	public Release getRelease(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId);

	@Path("/{projectid}/releases/byname/{releasename}")
	@Produces("application/json")
	@GET
	public Release getReleaseByName(@PathParam("projectid") String projectId, @PathParam("releasename") String releaseName);

	@Path("/{projectid}/releases/default")
	@Produces("application/json")
	@GET
	public Release getDefaultRelease(@PathParam("projectid") String projectId);

	@Path("/{projectid}/setdefaultrelease/{releaseid}")
	@Produces("application/json")
	@GET
	public Release setDefaultRelease(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId);

	/**
	 * Update a release by providing the project id, the release id, and a Release object with the changed properties.
	 * You cannot update builds this way, you should use the updateBuild method to change builds.
	 * @param projectId The id of the project you want to update the release in.
	 * @param releaseId The id of the release to update
	 * @param release The changed properties you wish to set.
	 * @return The updated release object.
	 */
	@Path("/{projectid}/releases/{releaseid}")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public Release updateRelease(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, Release release);

	@Path("/{projectid}/releases/{releaseid}")
	@Produces("application/json")
	@DELETE
	public List<Release> deleteRelease(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId);

	@Path("/{projectid}/releases/{releaseid}/builds")
	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public Build addBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, Build build);

	@Path("/{projectid}/releases/{releaseid}/builds")
	@Produces("application/json")
	@GET
	public List<Build> getBuilds(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId);

	@Path("/{projectid}/releases/{releaseid}/builds/{buildid}")
	@Produces("application/json")
	@GET
	public Build getBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, @PathParam("buildid") String buildId);

	@Path("/{projectid}/releases/{releaseid}/builds/byname/{buildname}")
	@Produces("application/json")
	@GET
	public Build getBuildByName(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, @PathParam("buildname") String buildName);

	@Path("/{projectid}/releases/{releaseid}/builds/default")
	@Produces("application/json")
	@GET
	public Build getDefaultBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId);

	@Path("/{projectid}/releases/{releaseid}/setdefaultbuild/{buildid}")
	@Produces("application/json")
	@GET
	public Build setDefaultBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, @PathParam("buildid") String buildId);

	@Path("/{projectid}/releases/{releaseid}/builds/{buildid}")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public Build updateBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, @PathParam("buildid") String buildId, Build build);

	@Path("/{projectid}/releases/{releaseid}/builds/{buildid}")
	@Produces("application/json")
	@DELETE
	public List<Build> deleteBuild(@PathParam("projectid") String projectId, @PathParam("releaseid") String releaseId, @PathParam("buildid") String buildId);

	@Path("/{projectid}/components")
	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public Component addComponent(@PathParam("projectid") String projectId, Component component);

	@Path("/{projectid}/components")
	@Produces("application/json")
	@GET
	public List<Component> getComponents(@PathParam("projectid") String projectId);

	@Path("/{projectid}/components/{componentid}")
	@Produces("application/json")
	@GET
	public Component getComponent(@PathParam("projectid") String projectId, @PathParam("componentid") String componentId);

	@Path("/{projectid}/components/{componentid}")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public Component updateComponent(@PathParam("projectid") String projectId, @PathParam("componentid") String componentId, Component component);

	@Path("/{projectid}/components/{componentid}")
	@Produces("application/json")
	@DELETE
	public List<Component> deleteComponent(@PathParam("projectid") String projectId, @PathParam("componentid") String componentId);

	@Path("/{projectid}/tags")
	@Produces("application/json")
	@GET
	public List<String> getAllTags(@PathParam("projectid") String projectId);

	@Path("/{projectid}/tags")
	@Produces("application/json")
	@POST
	public List<String> addTags(@PathParam("projectid") String projectId, List<String> tagsToAdd);

	@Path("/{projectid}/tags/{tagname}")
	@Produces("application/json")
	@DELETE
	public List<String> deleteTag(@PathParam("projectid") String projectId, @PathParam("tagname") String tagName);

	@Path("/{projectid}/automationTools")
	@Produces("application/json")
	@GET
	public List<String> getAllAutomationTools(@PathParam("projectid") String projectId);

	@Path("/{projectid}/automationTools")
	@Produces("application/json")
	@POST
	public List<String> addAutomationTools(@PathParam("projectid") String projectId, List<String> toolsToAdd);

	@Path("/{projectid}/automationTools/{automationTool}")
	@Produces("application/json")
	@DELETE
	public List<String> deleteAutomationTool(@PathParam("projectid") String projectId, @PathParam("automationTool") String automationTool);

	@Path("/{projectid}/datadrivenProperties")
	@Produces("application/json")
	@GET
	public List<DataDrivenPropertyType> getDataDrivenProperties(@PathParam("projectid") String projectId);

	@Path("/{projectid}/datadrivenProperties")
	@Produces("application/json")
	@POST
	public DataDrivenPropertyType addDataDrivenProperty(@PathParam("projectid") String projectId, DataDrivenPropertyType property);

	@Path("/{projectid}/datadrivenProperties/{propertyname}")
	@Produces("application/json")
	@DELETE
	public List<DataDrivenPropertyType> deleteDataDrivenPropertyByName(@PathParam("projectid") String projectId, @PathParam("propertyname") String propertyName);

	@Path("/{projectid}/extensions")
	@Produces("application/json")
	@GET
	public List<DataExtension<Project>> getExtensions(@PathParam("projectid") String projectId);

	@Path("/{projectid}/extensions/{extensionid}")
	@Produces("application/json")
	@GET
	public DataExtension<Project> getExtensionById(@PathParam("projectid") String projectId, @PathParam("extensionid") String extensionid);

	@Path("/{projectid}/extensions")
	@Produces("application/json")
	@Consumes("application/json")
	@POST
	public DataExtension<Project> addExtension(@PathParam("projectid") String projectId, DataExtension<Project> extension);

	@Path("/{projectid}/extensions/{extensionid}")
	@Produces("application/json")
	@Consumes("application/json")
	@PUT
	public DataExtension<Project> updateExtension(@PathParam("projectid") String projectId, @PathParam("extensionid") String extensionid, DataExtension<Project> extension);

	@Path("/{projectid}/extensions/{extensionid}")
	@Produces("application/json")
	@Consumes("application/json")
	@DELETE
	public List<DataExtension<Project>> deleteExtensionById(@PathParam("projectid") String projectId, @PathParam("extensionid") String extensionid);
}
