
module('slick.models.SystemConfiguration');

test('SystemConfiguration Model exists', function() {
	sysconfigClass = slick.models.SystemConfiguration;
	Assert(sysconfigClass).shouldBeType("function");
	var sysconfig = new sysconfigClass({});
    Assert(sysconfig).shouldBeInstanceOf(Backbone.DeepModel, 'Backbone.DeepModel');
});

test('SystemConfiguration.loadOne single response', function() {
    this.server = this.sandbox.useFakeServer();
	var sysconfig = new slick.models.SystemConfiguration({ 'configurationType': 'foobar' });
	Assert(sysconfig.loadOne).shouldBeType("function");

	var response = {
		'id': 1,
		'className': 'org.tcrun.slickij.api.data.AMQPSystemConfiguration',
		'configurationType': 'amqp-system-configuration'
	};
	this.server.respondWith("GET", "api/system-configuration?config-type=foobar",
	                         [200, {"content-type": "application/json"},
                              JSON.stringify(response)]);

	var req = sysconfig.loadOne();
    Assert(req.then).shouldBeType('function');

	this.server.respond();

	Assert(sysconfig.toJSON()).shouldEqual(response);
});

