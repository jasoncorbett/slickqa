package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;

/**
 * System configuration class that allows customization of email templates.
 * User: jcorbett
 * Date: 1/28/13
 * Time: 11:27 AM
 */
public class EmailTemplateConfiguration extends AbstractSystemConfiguration
{
    @Property
    public ProjectReference project;

    @Property
    public String subjectTemplate;

    @Property
    public String emailTemplate;

    public ProjectReference getProject()
    {
        return project;
    }

    public void setProject(ProjectReference project)
    {
        this.project = project;
    }

    public String getSubjectTemplate()
    {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate)
    {
        this.subjectTemplate = subjectTemplate;
    }

    public String getEmailTemplate()
    {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate)
    {
        this.emailTemplate = emailTemplate;
    }

    @Override
    public void validate() throws InvalidDataError
    {
        if(project == null && subjectTemplate == null && emailTemplate == null)
            throw new InvalidDataError("EmailTemplateConfiguration", "project, subjectTemplate, and emailTemplate", "All three properties cannot be null.");
    }

    @Override
    public void update(SystemConfiguration update) throws InvalidDataError
    {
        if(update instanceof EmailTemplateConfiguration)
        {
            EmailTemplateConfiguration realUpdate = EmailTemplateConfiguration.class.cast(update);
            if(realUpdate.project != null)
                project = realUpdate.project;
            if(realUpdate.subjectTemplate != null)
                subjectTemplate = realUpdate.subjectTemplate;
            if(realUpdate.emailTemplate != null)
                emailTemplate = realUpdate.emailTemplate;
        }
    }
}
