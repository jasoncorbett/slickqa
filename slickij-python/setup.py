#!/usr/bin/env python
import os
from distutils.core import setup

def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

setup(
    name = "slickqa",
    version = "0.0.311",
    author = "Chris Saxey",
    author_email = "chris.saxey@quest.com",
    description = ("Python interface to Slick QA Results Database"),
    license = "Apache License 2.0",
    keywords = "testing qa results automation",
    url = "http://code.google.com/p/slickqa/",
    py_modules=['slickApi'],
    long_description=read('README'),
    classifiers=[
        "Development Status :: 3 - Alpha",
        "Topic :: Testing",
        "License :: OSI Approved :: Apache License 2.0",
    ],
)
