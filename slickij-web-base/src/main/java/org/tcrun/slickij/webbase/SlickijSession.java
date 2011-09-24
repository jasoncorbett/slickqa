package org.tcrun.slickij.webbase;

import com.google.inject.Inject;
import java.util.List;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.UserAccount;
import org.tcrun.slickij.api.data.dao.ProjectDAO;
import org.tcrun.slickij.api.data.dao.UserAccountDAO;

/**
 *
 * @author jcorbett
 */
public class SlickijSession extends AuthenticatedWebSession
{
	@Inject
	private UserAccountDAO m_useraccountDAO;

	@Inject
	private ProjectDAO projectDao;

	private List<Project> projectsCache = null;

	private UserAccount userAcct = null;

	private Project currentProject = null;

	private String currentGroup;
        
        private boolean showAutomationInfo = true;

	public static SlickijSession get()
	{
		return (SlickijSession) WebSession.get();
	}

	public SlickijSession(Request req)
	{
		super(req);
	}

	public boolean isAuthenticated()
	{
		return userAcct != null;
	}

	@Override
	public boolean authenticate(String username, String password)
	{
		if(m_useraccountDAO != null)
		{
			UserAccount acct = m_useraccountDAO.get(username);
			if(acct == null)
				return false;
			if(acct.authenticate(password))
			{
				userAcct = acct;
				return true;
			}
			else
				return false;
		} else
		{
			return false;
		}
	}

	@Override
	public void invalidate()
	{
		userAcct = null;
		super.invalidate();
	}

	public UserAccount getUserAcct()
	{
		return userAcct;
	}

	public void setUserAcct(UserAccount userAcct)
	{
		this.userAcct = userAcct;
	}

	public UserAccountDAO getUseraccountDAO()
	{
		return m_useraccountDAO;
	}

	public void setUseraccountDAO(UserAccountDAO p_useraccountDAO)
	{
		m_useraccountDAO = p_useraccountDAO;
	}

	@Override
	public Roles getRoles()
	{
		if(isAuthenticated())
			return new Roles("user");
		else
			return null;
	}

	public String getCurrentGroup()
	{
		return currentGroup;
	}

	public void setCurrentGroup(String currentGroup)
	{
		this.currentGroup = currentGroup;
	}

	public Project getCurrentProject()
	{
		if (this.currentProject == null)
		{
			if(getListOfProjects().size() > 0)
				this.currentProject = getListOfProjects().get(0);
		}
		return this.currentProject;
	}

	public void setCurrentProject(Project proj)
	{
		this.currentProject = proj;
	}

	public List<Project> getListOfProjects()
	{
		if(projectsCache == null)
			projectsCache = projectDao.find().asList();
		return projectsCache;
	}

	public void resetCache()
	{
		projectsCache = null;
	}
}
