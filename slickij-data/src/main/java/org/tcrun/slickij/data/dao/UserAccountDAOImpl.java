package org.tcrun.slickij.data.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.inject.Inject;
import org.tcrun.slickij.api.data.UserAccount;
import org.tcrun.slickij.api.data.dao.UserAccountDAO;

/**
 *
 * @author jcorbett
 */
public class UserAccountDAOImpl extends BasicDAO<UserAccount, String> implements UserAccountDAO
{
	@Inject
	public UserAccountDAOImpl(Datastore ds)
	{
		super(UserAccount.class, ds);
	}
}
