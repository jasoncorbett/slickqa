import os
import sys
import thread
import threading
import datetime
import traceback
from logging import *
from slickApi import SlickAsPy
from dateutil.tz import *

ERROR = 40
WARN = 30
INFO = 20
DEBUG = 10
TRACE = 5

log_levels = {
    ERROR : 'ERROR',
    WARN : 'WARN',
    INFO : 'INFO',
    DEBUG : 'DEBUG',
    TRACE : 'TRACE'
    }

# See logging doc for info on these
_startTime = datetime.datetime.now(tzlocal())
logThreads = 1
logMultiprocessing = 1
logProcesses = 1

def start_logging(loggerName, slickcon=None, slickurl="http://localhost:8080/api", username="tcrunij", 
                  password="f00b@r", otherhandlers=None):
    setLoggerClass(Slicklogger)
    log = getLogger(loggerName)
    for level, levelName in log_levels.iteritems():
        addLevelName(level, levelName)
    log.setLevel(TRACE)
    formatter = SlickFormatter()
    if not slickcon:
        handler = SlickHandler(log.level, baseurl=slickurl, username=username, password=password)
    else:
        handler = SlickHandler(log.level, slickcon)
    handler.setFormatter(formatter)
    log.addHandler(handler)
    if isinstance(otherhandlers, list):
        for other in otherhandlers:
            log.addHandler(other)
    elif otherhandlers:
        log.addHandler(otherhandlers)
    isinstance(log, Slicklogger)
    return log

class Slicklogger(getLoggerClass()):
    
    def makeRecord(self, name, level, fn, lno, msg, args, exc_info, func=None, extra=None):
        return SlickRecord(name, level, fn, lno, msg, args, exc_info, func)
    
    def trace(self, message, *args, **kwargs):
        self.log(TRACE, message, *args, **kwargs)
        
    def end_test(self, resultId):
        '''empties the log queue '''
        if self.handlers:
            self._handle_log(self.handlers, resultId)
        else:
            # See if the parent has any handlers
            if self.parent.handlers:
                self._handle_log(self.parent.handlers, resultId)
            
    def _handle_log(self, handlers, resultId):
        for handler in handlers:
            if hasattr(handler, "end_test"):
                handler.end_test(resultId)
                
class SlickRecord(LogRecord):
    def __init__(self, name, level, pathname, lineno,
                 msg, args, exc_info, func=None):
        """
        Initialize a logging record with interesting information.
        """
        # using the localized time instead of time.time()
        ct = datetime.datetime.now(tzlocal())
        self.name = name
        self.msg = msg
        if args and len(args) == 1 and isinstance(args[0], dict) and args[0]:
            args = args[0]
        self.args = args
        self.levelname = getLevelName(level)
        self.levelno = level
        self.pathname = pathname
        try:
            self.filename = os.path.basename(pathname)
            self.module = os.path.splitext(self.filename)[0]
        except (TypeError, ValueError, AttributeError):
            self.filename = pathname
            self.module = "Unknown module"
        self.exc_info = exc_info
        self.exc_text = None      # used to cache the traceback text
        self.lineno = lineno
        self.funcName = func
        self.created = ct
        self.msecs = ct.microsecond
        self.relativeCreated = (self.created - _startTime)
        if logThreads and thread:
            self.thread = thread.get_ident()
            self.threadName = threading.current_thread().name
        else:
            self.thread = None
            self.threadName = None
        if not logMultiprocessing:
            self.processName = None
        else:
            self.processName = 'MainProcess'
            mp = sys.modules.get('multiprocessing')
            if mp is not None:
                try:
                    self.processName = mp.current_process().name
                except StandardError:
                    pass
        if logProcesses and hasattr(os, 'getpid'):
            self.process = os.getpid()
        else:
            self.process = None

class SlickFormatter(Formatter):
    '''Formats the python log record into something easily dumped into JSON'''
    def format(self, record):
        now = self.formatTime(record)
        exctype = excvalue = exctraceback = None
        try:
            # TODO: figure out what lang each term is using and decode it
            message = unicode(record.msg, 'latin_1')
        except UnicodeDecodeError as de:
            message = "Error trying to decode. {}".format(traceback.format_exc())
        except UnicodeEncodeError as ee:
            message = "Error trying to encode. {}".format(traceback.format_exc())
        except Exception as e:
            message = "Some weird error occured. {}".format(traceback.format_exc())
        if record.exc_info:
            exctype = str(record.exc_info[0])
            excvalue = str(record.exc_info[1])
            exctraceback = traceback.format_exc().splitlines()
        
        return {"entryTime": now, "level": record.levelname, "loggerName": record.name, "message": message, 
                "exceptionClassName": exctype, "exceptionMessage": excvalue, "exceptionStackTrace": exctraceback}
    
    def formatTime(self, record, datefmt='%a, %m %b %Y %H:%M:%S %Z'):
        return record.created.strftime(datefmt)
    
class SlickHandler(Handler):
    '''Hadles logging to slick from a test'''
    def __init__(self, level=NOTSET, slickcon=None, baseurl="http://localhost:8080/slickij-war/api", 
                 username="tcrunij", password="f00b@r"):
        super(SlickHandler, self).__init__(level)
        self._slick_con = slickcon
        if not slickcon or not isinstance(slickcon, SlickAsPy):
            self._slick_con = SlickAsPy(baseurl, username, password)
        self._log_queue = []
        
    def emit(self, record):
        # use the specified format
        message = self.format(record)
        # do we need to add unicode supprt?
        
        # add to the queue
        self._log_queue.append(message)
        
    def end_test(self, resultId):
        if self._log_queue:
            self._slick_con.add_log_entries(self._log_queue, resultId)
            # clear the queue
            self._log_queue = []
        
