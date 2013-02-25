package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static org.tcrun.slickij.api.data.CopyUtil.copyDateIfNotNull;

/**
 *
 * @author jcorbett
 */
public class LogEntry implements Serializable, Copyable<LogEntry>
{
	@Property
	private Date entryTime;

	@Property
	private LogLevel level;

	@Property
	private String loggerName;

	@Property
	private String message;

	@Property
	private String exceptionClassName;

	@Property
	private String exceptionMessage;

	@Property
	private List<String> exceptionStackTrace;

	public Date getEntryTime()
	{
		return entryTime;
	}

	public void setEntryTime(Date entryTime)
	{
		this.entryTime = entryTime;
	}

	public String getExceptionClassName()
	{
		return exceptionClassName;
	}

	public void setExceptionClassName(String exceptionClassName)
	{
		this.exceptionClassName = exceptionClassName;
	}

	public String getExceptionMessage()
	{
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage)
	{
		this.exceptionMessage = exceptionMessage;
	}

	public List<String> getExceptionStackTrace()
	{
		return exceptionStackTrace;
	}

	public void setExceptionStackTrace(List<String> exceptionStackTrace)
	{
		this.exceptionStackTrace = exceptionStackTrace;
	}

	public LogLevel getLevel()
	{
		return level;
	}

	public void setLevel(LogLevel level)
	{
		this.level = level;
	}

	public String getLoggerName()
	{
		return loggerName;
	}

	public void setLoggerName(String loggerName)
	{
		this.loggerName = loggerName;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}


    @Override
    public LogEntry createCopy()
    {
        LogEntry copy = new LogEntry();

        copy.setEntryTime(copyDateIfNotNull(getEntryTime()));
        copy.setExceptionClassName(getExceptionClassName());
        copy.setExceptionMessage(getExceptionMessage());
        copy.setExceptionStackTrace(getExceptionStackTrace());
        copy.setLevel(getLevel());
        copy.setLoggerName(getLoggerName());
        copy.setMessage(getMessage());

        return copy;
    }
}
