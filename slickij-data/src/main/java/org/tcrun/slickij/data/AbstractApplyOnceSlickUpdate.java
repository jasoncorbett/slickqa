package org.tcrun.slickij.data;

import com.google.inject.Inject;
import org.tcrun.slickij.api.data.LogEntry;
import org.tcrun.slickij.api.data.LogLevel;
import org.tcrun.slickij.api.data.SlickUpdate;
import org.tcrun.slickij.api.data.UpdateRecord;
import org.tcrun.slickij.api.data.dao.UpdateRecordDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The abstract update is a parent class for all updates.  It provides a default
 * implementation of the needsApplying method.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 5:22 PM
 */
public abstract class AbstractApplyOnceSlickUpdate implements SlickUpdate
{
    private UpdateRecordDAO recordDAO;
    private List<LogEntry> logs;

    @Inject
    public AbstractApplyOnceSlickUpdate(UpdateRecordDAO recordDAO)
    {
        this.recordDAO = recordDAO;
        logs = new ArrayList<LogEntry>();
    }

    @Override
    public boolean needsApplying()
    {
        UpdateRecord record = recordDAO.findOne("updateId", getUpdateId());
        return record == null;
    }

    @Override
    public final void apply()
    {
        performUpdate();
        UpdateRecord record = new UpdateRecord();
        record.setLogs(logs);
        record.setUpdateId(getUpdateId());
        recordDAO.save(record);
    }

    public abstract void performUpdate();

    public void addLogEntry(LogLevel level, String message)
    {
        LogEntry entry = new LogEntry();
        entry.setLevel(level);
        entry.setEntryTime(new Date());
        entry.setLoggerName(this.getClass().getName());
        entry.setMessage(message);
        logs.add(entry);
    }

    public void logDebug(String message)
    {
        addLogEntry(LogLevel.DEBUG, message);
    }

    public void logInfo(String message)
    {
        addLogEntry(LogLevel.INFO, message);
    }

    public void logWarn(String message)
    {
        addLogEntry(LogLevel.WARN, message);
    }

    public void logError(String message)
    {
        addLogEntry(LogLevel.ERROR, message);
    }
}
