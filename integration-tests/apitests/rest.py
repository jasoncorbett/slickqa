import json
import zlib
import urllib
import urlparse
import httplib2
import traceback
from dateutil.tz import *
from datetime import datetime

json_content = {'Content-Type': 'application/json'}
STREAM_CONTENT = {'Content-Type': 'application/octet-stream'}
GET = 'GET'
POST = 'POST'
PUT = 'PUT'
DELETE = 'DELETE'

class RestError(Exception):
    pass

class RestApi(object):
    def __init__(self, baseurl):
        self.http_connection = httplib2.Http()
        self.baseurl = baseurl
        if self.baseurl.endswith('/'):
            self.baseurl = baseurl[0:-1]

    def _fix_uri(self, uri, charset='utf-8'):
        if isinstance(uri, unicode):
            uri = uri.encode(charset, 'ignore')
        scheme, netloc, path, query, fragment = urlparse.urlsplit(uri)
        path = urllib.quote(path, '/%')
        query = urllib.quote(query, ':&=')
        return urlparse.urlunsplit((scheme, netloc, path, query, fragment))

    def _get_url(self, *args, **kwargs):
        if len(kwargs) > 0:
            uri = '/'.join([self.baseurl,] + list(args)) + "?" + kwargs
        else:
            uri = '/'.join([self.baseurl,] + list(args))
        url = self._fix_uri(uri)
        return url

    def _safe_return(self, response, content):
        if response['status'] == "200":
            return json.loads(content)
        elif response['status'] == "204":
            return None
        else:
            raise RestError("Response: {}\nContent: {}".format(response, content))

    def get(self, *args, **kwargs):
        return self._safe_request(GET, None, None, *args, **kwargs)

    def post(self, post_data, *args, **kwargs):
        return self._safe_request(POST, post_data, None, *args, **kwargs)

    def post_octet_stream(self, post_data, *args, **kwargs):
        return self._safe_request(POST, post_data, True, *args, **kwargs)

    def delete(self, *args, **kwargs):
        return self._safe_request(DELETE, None, None, *args, **kwargs)

    def put(self, update_data, *args, **kwargs):
        return self._safe_request(PUT, update_data, None, *args, **kwargs)
    
    def _safe_request(self, method, data=None, stream=None, *args, **kwargs):
        url = self._get_url(*args, **kwargs)
        if data and not stream:
            data = json.dumps(data)
        content_type = json_content
        if stream:
            content_type = STREAM_CONTENT
        good_content = None
        # sometimes we get bad content back from the server
        # this is a hack to fix that
        for i in range(5):
            if GET in method:
                response, content = self.http_connection.request(url)
            elif POST in method:
                response, content = self.http_connection.request(url, POST, data, content_type)
            elif PUT in method:
                response, content = self.http_connection.request(url, PUT, data, content_type)
            elif DELETE in method:
                response, content = self.http_connection.request(url, DELETE)
            try:
                good_content = self._safe_return(response, content)
            except RestError as se:
                raise
            except:
                print 'Here is the content recieved from the server: {}'.format(content)
            if good_content or good_content is None:
                return good_content
        raise RestError("Tried connecting 5 times to {} {} \nData: {}\nResponse: {}\nContent: {}".format(url, method, data, response, content))
                
