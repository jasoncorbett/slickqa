import datetime
import traceback
from logging import *
from slickApi import SlickAsPy

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
    def trace(self, message, *args, **kwargs):
        self.log(TRACE, message, *args, **kwargs)
        
    def end_test(self, resultId):
        '''empties the log queue '''
        for handler in self.handlers:
            if hasattr(handler, "end_test"):
                handler.end_test(resultId)

class SlickFormatter(Formatter):
    '''Formats the python log record into something easily dumped into JSON'''
    def format(self, record):
        now = self.formatTime(record)
        exctype = excvalue = exctraceback = None
        if record.exc_info:
            exctype = str(record.exc_info[0])
            excvalue = str(record.exc_info[1])
            exctraceback = traceback.format_exc().splitlines()
        
        return {"entryTime": now, "level": record.levelname, "loggerName": record.name, "message": record.msg, 
                "exceptionClassName": exctype, "exceptionMessage": excvalue, "exceptionStackTrace": exctraceback}
    
    def formatTime(self, record, datefmt=None):
        ct = datetime.datetime.fromtimestamp(record.created)
        return ct.isoformat()
    
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
        self._slick_con.add_log_entries(self._log_queue, resultId)
        # clear the queue
        self._log_queue = []
        