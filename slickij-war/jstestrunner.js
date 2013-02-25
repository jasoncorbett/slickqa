/*
 * QtWebKit-powered headless test runner using PhantomJS
 *
 * PhantomJS binaries: http://phantomjs.org/download.html
 * Requires PhantomJS 1.6+ (1.7+ recommended)
 *
 * Run with:
 *   phantomjs runner.js [url-of-your-qunit-testsuite]
 *
 * e.g.
 *   phantomjs runner.js http://localhost/qunit/test/index.html
 */

/*jshint latedef:false */
/*global phantom:false, require:false, console:false, window:false, QUnit:false */

(function() {
	'use strict';

	var args = require('system').args;
    var fs = require('fs');

	// arg[0]: scriptName, args[1...]: arguments
	if (args.length !== 2) {
		console.error('Usage:\n  phantomjs runner.js [url-of-your-qunit-testsuite]');
		phantom.exit(1);
	}

	var url = args[1],
		page = require('webpage').create();

    function green(message) {
        return '\u001b[32m' + message + '\u001b[0m';
    }

    function red(message) {
        return '\u001b[31m' + message + '\u001b[0m';
    }

	// Route `console.log()` calls from within the Page context to the main Phantom context (i.e. current `this`)
	page.onConsoleMessage = function(msg) {
		console.log(msg);
	};

	page.onInitialized = function() {
		page.evaluate(addLogging);
	};

	page.onCallback = function(message) {
		var result,
			failed;

		if (message) {
			if (message.name === 'QUnit.done') {
				result = message.data;

                console.log('Took ' + result.runtime +  'ms to run ' + result.total + ' tests. ' + green("" + result.passed + ' passed') + ", " + red("" + result.failed + ' failed') + '.');

				failed = !result || result.failed;

				phantom.exit(failed ? 1 : 0);
			} else if(message.name === 'JUnitXMLOutput') {
                var outputdir = fs.workingDirectory + fs.separator + 'target' + fs.separator + 'qunit';
                if(! fs.exists(outputdir)) {
                    fs.makeTree(outputdir);
                }
                var outputfile = outputdir + fs.separator + 'TEST-javascript.xml';
                fs.write(outputfile, message.xml);
            } else if(message.name === 'QUnit.testDone') {
                var result = message.data.result;
                var name = message.data.name;
                var current_test_assertions = message.assertions;
                if (result.failed) {
                    console.log(red('Test failed: ' + name));

                    for (var i = 0, len = current_test_assertions.length; i < len; i++) {
                        var assertionMessage = current_test_assertions[i];

                        if(assertionMessage.match(/^Successful/)) {
                            assertionMessage = green(assertionMessage);
                            console.log('    ' + assertionMessage);
                        } else {
                            var arrayOfLines = assertionMessage.match(/[^\r\n]+/g);
                            console.log('    ' + red(arrayOfLines[0]));
                            for(var j = 1, jlen = arrayOfLines.length; j < jlen; j++) {
                                console.log('    ' + arrayOfLines[j]);
                            }
                        }
                    }
                } else {
                    console.log(green('Test passed: ' + name));
                }

            }

		}
	};

	page.open(url, function(status) {
		if (status !== 'success') {
			console.error('Unable to access network: ' + status);
			phantom.exit(1);
		} else {
			// Cannot do this verification with the 'DOMContentLoaded' handler because it
			// will be too late to attach it if a page does not have any script tags.
			var qunitMissing = page.evaluate(function() { return (typeof QUnit === 'undefined' || !QUnit); });
			if (qunitMissing) {
				console.error('The `QUnit` object is not present on this page.');
				phantom.exit(1);
			}

			// Do nothing... the callback mechanism will handle everything!
		}
	});

	function addLogging() {
		window.document.addEventListener('DOMContentLoaded', function() {
			var current_test_assertions = [];

			QUnit.log(function(details) {
				var response;


				response = details.message || '';

				if (typeof details.expected !== 'undefined') {
					if (response) {
						response += ', ';
					}

					response += 'expected: ' + details.expected + ', but was: ' + details.actual;
					if (details.source) {
						response += "\n" + details.source;
					}
				}
                // Ignore passing assertions
                if (details.result) {
                    current_test_assertions.push('Successful assertion: ' + response);
                } else {
    				current_test_assertions.push('Failed assertion: ' + response);
                }
			});

			QUnit.testDone(function(result) {
				var i,
					len,
					name = result.module + ': ' + result.name;

                if (typeof window.callPhantom === 'function') {
                    window.callPhantom({
                        name: 'QUnit.testDone',
                        data: { name: name, result: result},
                        assertions: current_test_assertions
                    });
                }

/*				if (result.failed) {
					console.log('Test failed: ' + name);

					for (i = 0, len = current_test_assertions.length; i < len; i++) {
						console.log('    ' + current_test_assertions[i]);
					}
				}*/

				current_test_assertions.length = 0;
			});

			QUnit.done(function(result) {
				//console.log('Took ' + result.runtime +  'ms to run ' + result.total + ' tests. ' + result.passed + ' passed, ' + result.failed + ' failed.');

				if (typeof window.callPhantom === 'function') {
					window.callPhantom({
						'name': 'QUnit.done',
						'data': result
					});
				}
			});
		}, false);
	}
})();
