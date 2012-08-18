import json
import urllib
import urlparse
import httplib2

json_content = {'Content-Type': 'application/json'}
STREAM_CONTENT = {'Content-Type': 'application/octet-stream'}
GET = 'GET'
POST = 'POST'
PUT = 'PUT'
DELETE = 'DELETE'

class AttributeDict(dict):
    __getattr__ = dict.__getitem__
    __setattr__ = dict.__setitem__

def obj_hook_attr_dict(dct):
    return AttributeDict(dct)

class RestError(Exception):
    pass

class RestApi(object):
    def __init__(self, baseurl):
        self.http_connection = httplib2.Http()
        self.baseurl = baseurl
        if self.baseurl.endswith('/'):
            self.baseurl = baseurl[0:-1]
        self.urlargs = []

    def __getattr__(self, item):
        self.urlargs.append(item)
        return self

    def __getitem__(self, item):
        self.urlargs.append(item)
        return self

    def _fix_uri(self, uri, charset='utf-8'):
        if isinstance(uri, unicode):
            uri = uri.encode(charset, 'ignore')
        scheme, netloc, path, query, fragment = urlparse.urlsplit(uri)
        path = urllib.quote(path, '/%')
        return urlparse.urlunsplit((scheme, netloc, path, query, fragment))

    def _get_url(self, *args, **kwargs):
        if len(kwargs) > 0:
            uri = '/'.join([self.baseurl,] + self.urlargs + list(args)) + "?" + urllib.urlencode(kwargs)
        else:
            uri = '/'.join([self.baseurl,] + self.urlargs + list(args))
        self.urlargs = []
        url = self._fix_uri(uri)
        return url

    def _safe_return(self, response, content):
        if response['status'] == "200":
            retval = json.loads(content, object_hook=obj_hook_attr_dict)
            return retval
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
        return good_content


def get_default_release(project):
    default_release_id = project.defaultRelease
    for release in project.releases:
        if release.id == default_release_id:
            return release

def get_default_build(release):
    if "releases" in release:
        #they handed us a project, that's ok
        release = get_default_release(release)
    default_build_id = release.defaultBuild
    for build in release.builds:
        if build.id == default_build_id:
            return build
