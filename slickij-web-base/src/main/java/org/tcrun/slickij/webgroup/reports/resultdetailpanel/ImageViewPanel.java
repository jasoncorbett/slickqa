package org.tcrun.slickij.webgroup.reports.resultdetailpanel;

import com.google.inject.Inject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.tcrun.slickij.api.data.StoredFile;
import org.tcrun.slickij.api.data.dao.StoredFileDAO;

/**
 *
 * @author jcorbett
 */
public class ImageViewPanel extends HideableLinkablePanel
{
	private String image_url;

	@Inject
	private StoredFileDAO fileDAO;

	private IResource image;

	private StoredFile file;

	public ImageViewPanel(String id, StoredFile imagefile)
	{
		super(id);
		file = imagefile;
		image = new BufferedDynamicImageResource(imagefile.getFilename().replaceAll(".*\\\\.", ""))
		{
			@Override
			protected byte[] getImageData(Attributes attributes)
			{
				return fileDAO.getFileContent(file.getObjectId());
			}

		};
		add(new Image("image", image));
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("style", "display:none");
	}

	@Override
	public IResource getLinkImage()
	{
		return image;
	}

	@Override
	public String getLinkText()
	{
		return file.getFilename();
	}
}
