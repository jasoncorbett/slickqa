#!/usr/bin/env python
import os
from distutils.core import setup

def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

#TODO: support pip and dependencies
setup(
    name = "slickqa",
    version = "0.1.396",
    author = "Chris Saxey",
    author_email = "jared.jorgensen@quest.com",
    description = ("Python interface to Slick QA Results Database"),
    license = "Apache License 2.0",
    keywords = "testing qa results automation",
    url = "http://code.google.com/p/slickqa/",
    packages = ['slickqa'],
    long_description=read('README'),
    classifiers=[
        "Development Status :: 3 - Alpha",
        "Topic :: Testing",
        "License :: OSI Approved :: Apache License 2.0",
    ],
)
