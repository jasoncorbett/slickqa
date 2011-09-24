package org.tcrun.slickij.webbase;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author jcorbett
 */
public class BookmarkablePageLinkWithName<T> extends Panel
{
	public BookmarkablePageLinkWithName(String id, Class<? extends Page> target, IModel<T> nameModel)
	{
		super(id);
		BookmarkablePageLink<T> link = new BookmarkablePageLink<T>("bmkpglink", target);
		link.add(new Label("bmkpglinkname", nameModel));
		add(link);
	}

	public BookmarkablePageLinkWithName(String id, Class<? extends Page> target, String name)
	{
		super(id);
		BookmarkablePageLink<T> link = new BookmarkablePageLink<T>("bmkpglink", target);
		link.add(new Label("bmkpglinkname", name));
		add(link);
	}

	public BookmarkablePageLinkWithName(String id, Class<? extends Page> target, PageParameters params, IModel<T> nameModel)
	{
		super(id);
		BookmarkablePageLink<T> link = new BookmarkablePageLink<T>("bmkpglink", target, params);
		link.add(new Label("bmkpglinkname", nameModel));
		add(link);
	}

	public BookmarkablePageLinkWithName(String id, Class<? extends Page> target, PageParameters params, String name)
	{
		super(id);
		BookmarkablePageLink<T> link = new BookmarkablePageLink<T>("bmkpglink", target, params);
		link.add(new Label("bmkpglinkname", name));
		add(link);
	}

	
}
