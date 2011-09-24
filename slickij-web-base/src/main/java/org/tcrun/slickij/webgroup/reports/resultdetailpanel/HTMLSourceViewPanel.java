package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import com.google.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.dao.StoredFileDAO;

/**
 *
 * @author jcorbett
 */
public class HTMLSourceViewPanel extends HideableLinkablePanel
{
	@Inject
	private StoredFileDAO fileDAO;

	private StoredFile file;

	private boolean already_formatted;

	public static ResourceReference HTML_IMAGE = new PackageResourceReference(HTMLSourceViewPanel.class, "htmlicon.png");
	public static ResourceReference PRETTIFY_JS = new PackageResourceReference(HTMLSourceViewPanel.class, "prettify.js");

	public HTMLSourceViewPanel(String id, StoredFile htmlfile)
	{
		super(id);
		already_formatted = false;
		file = htmlfile;

		WebMarkupContainer script = new WebMarkupContainer("prettify-js")
		{
			@Override
			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.put("src", urlFor(PRETTIFY_JS, null));
			}
		};
		add(script);

		String htmlsource = new String(fileDAO.getFileContent(htmlfile.getObjectId()));
		add(new Label("htmlsource", htmlsource));
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("style", "display: none");
	}

	@Override
	public void show(AjaxRequestTarget target)
	{
		super.show(target);
		if(!already_formatted)
		{
			target.appendJavaScript("prettyPrint();");
			already_formatted = true;
		}
	}

	@Override
	public IResource getLinkImage()
	{
		return HTML_IMAGE.getResource();
	}

	@Override
	public String getLinkText()
	{
		return file.getFilename();
	}
}
