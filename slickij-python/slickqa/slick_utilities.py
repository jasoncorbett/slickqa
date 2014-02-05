from datetime import datetime, timedelta
from dateutil.tz import *

#----------------------------------------------------------------------
def get_date():
    """
    A date object formatted for slick with the local time zone
    """
    return datetime.now(tzlocal()).strftime('%a, %d %b %Y %H:%M:%S %Z')