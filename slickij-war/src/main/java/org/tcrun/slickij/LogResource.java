package org.tcrun.slickij;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.read.CyclicBufferAppender;
import org.slf4j.LoggerFactory;
import org.tcrun.slickij.api.data.LogEntry;
import org.tcrun.slickij.api.data.LogLevel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The LogResource is a way to both inject into the server logs, and retrieve them.
 *
 * @author Jason Corbett
 * Date: 1/21/12
 * Time: 1:00 AM
 *
 */
@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
public class LogResource
{
    public static final String CYCLIC_BUFFER_APPENDER_NAME = "CYCLIC";
    
    private CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender;

    public LogResource()
    {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        cyclicBufferAppender = (CyclicBufferAppender<ILoggingEvent>) lc.getLogger(
        Logger.ROOT_LOGGER_NAME).getAppender(CYCLIC_BUFFER_APPENDER_NAME);
    }
    
    @GET
    public List<LogEntry> getLogs()
    {
        ArrayList<LogEntry> retval = new ArrayList<LogEntry>();
        if(cyclicBufferAppender != null)
        {
            for(int i = 0; i < cyclicBufferAppender.getLength(); i++)
            {
                ILoggingEvent event = (ILoggingEvent)cyclicBufferAppender.get(i);
                LogEntry entry = new LogEntry();
                entry.setLevel(LogLevel.valueOf(event.getLevel().levelStr));
                entry.setLoggerName(event.getLoggerName());
                entry.setEntryTime(new Date(event.getTimeStamp()));
                entry.setMessage(event.getFormattedMessage());
                if(event.getThrowableProxy() != null)
                {
                    entry.setExceptionClassName(event.getThrowableProxy().getClassName());
                    entry.setExceptionMessage(event.getThrowableProxy().getMessage());
                    StackTraceElementProxy[] stackTraceElements = event.getThrowableProxy().getStackTraceElementProxyArray();
                    List<String> stacktrace = new ArrayList<String>(stackTraceElements.length);
                    for(StackTraceElementProxy element : stackTraceElements)
                      stacktrace.add(element.getSTEAsString());
                    entry.setExceptionStackTrace(stacktrace);
                }
                retval.add(entry);
            }
        }
        return retval;
    }
}
