package org.tcrun.slickij.data;

import com.google.inject.Inject;
import org.tcrun.slickij.api.UpdateResource;
import org.tcrun.slickij.api.data.SlickUpdate;
import org.tcrun.slickij.api.data.UpdateRecord;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 10:04 PM
 */
public class UpdateResourceImpl implements UpdateResource
{
    private Set<SlickUpdate> availableUpdates;
    private UpdateRecordDAO updateRecordDAO;

    @Inject
    public UpdateResourceImpl(Set<SlickUpdate> availableUpdates, UpdateRecordDAO updateRecordDAO)
    {
        this.availableUpdates = availableUpdates;
        this.updateRecordDAO = updateRecordDAO;
    }

    @Override
    public List<SlickUpdate> getAvailableUpdates()
    {
        return new ArrayList<SlickUpdate>(availableUpdates);
    }

    @Override
    public List<SlickUpdate> performNeededUpdates()
    {
        ArrayList<SlickUpdate> retval = new ArrayList<SlickUpdate>();
        for(SlickUpdate update : availableUpdates)
        {
            try
            {
                if(update.needsApplying())
                {
                    update.apply();
                    retval.add(update);
                }
            } catch (RuntimeException ex)
            {
                // Log exception?
            } catch (Exception ex)
            {
                // Log exception?
            }
        }

        return retval;
    }

    @Override
    public List<UpdateRecord> getUpdateRecords()
    {
        return updateRecordDAO.find().asList();
    }
}
