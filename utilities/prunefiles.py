#!/usr/bin/env python
__author__ = 'jcorbett'

import argparse
import pymongo
import sys

from gridfs import GridFS

def main(arguments):
    parser = argparse.ArgumentParser(description='Remove files from a set of results.')
    parser.add_argument("-p", "--project", dest="project", required=True, help="The project to prune files from")
    parser.add_argument("-r", "--release", dest="release", required=True, help="The release of the project to prune files from.")
    options = parser.parse_args(args=arguments)

    connection = pymongo.Connection()
    db = connection['slickij']
    gridfs = GridFS(db)
    project = db.projects.find_one({'name': options.project})
    if project is None:
        print "There is no project with the name", options.project
        sys.exit(1)
    release = None
    for possible in project['releases']:
        if possible['name'] == options.release:
            release = possible
            break
    else:
        print "There is no release with the name", options.release
        sys.exit(1)

    number_of_results = db.results.find({'release.releaseId': release['id']}).count()
    print "There are", number_of_results, "results in that release."
    resultnum = 0
    for result in db.results.find({'release.releaseId': release['id']}):
        sys.stdout.write("{:.2f}%\r".format(((float(resultnum) / number_of_results) * 100)))
        sys.stdout.flush()
        resultnum += 1
        if 'files' in result:
            for fileref in result['files']:
                fileobj = db[fileref.collection].find_one(fileref.id)
                gridfs.delete(fileref.id)
    print "Done Removing files from", number_of_results, "results."
    print "Removing file references from the results."
    db.results.update({'release.releaseId': release['id']}, {"$unset": {"": 1}}, False, True)
    print "Done."


if __name__ == '__main__':
    main(sys.argv[1:])

