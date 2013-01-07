
module('slick.models.SystemConfiguration');

test('SystemConfiguration Model exists', function() {
	sysconfigClass = slick.models.SystemConfiguration;
	Assert(sysconfigClass).shouldBeType("function");
	var sysconfig = new sysconfigClass({});
    Assert(sysconfig).shouldBeInstanceOf(Backbone.DeepModel, 'Backbone.DeepModel');
});

test('SystemConfiguration.loadOne exists and returns jqXHR', function() {
    var server = this.sandbox.useFakeServer();
    var sysconfig = new slick.models.SystemConfiguration({ 'configurationType': 'foobar' });
    Assert(sysconfig.loadOne).shouldBeType("function");
    var response = [];
    server.respondWith("GET", "api/system-configuration?config-type=foobar",
                       [200, {"content-type": "application/json"},
                       JSON.stringify(response)]);
    var req = sysconfig.loadOne();
    Assert(req.then).shouldBeType('function');
    server.respond();

});

test('SystemConfiguration.loadOne single response', function() {
    var server = this.sandbox.useFakeServer();
	var sysconfig = new slick.models.SystemConfiguration({ 'configurationType': 'foobar' });

	var response = [{
		'id': 1,
		'className': 'org.tcrun.slickij.api.data.AMQPSystemConfiguration',
		'configurationType': 'amqp-system-configuration'
	}];
	server.respondWith("GET", "api/system-configuration?config-type=foobar",
	                         [200, {"content-type": "application/json"},
                              JSON.stringify(response)]);

    var successSpy = this.spy();
    var errorSpy = this.spy();

	sysconfig.loadOne().then(successSpy, errorSpy);

	server.respond();

	Assert(sysconfig.toJSON()).shouldEqual(response[0]);

    ok(successSpy.calledOnce, "Success should have been called after successful mocked server communication.");
    Assert(errorSpy.callCount).shouldEqual(0);
});

test('SystemConfiguration.loadOne empty response', function() {
    var server = this.sandbox.useFakeServer();
    var sysconfig = new slick.models.SystemConfiguration({ 'configurationType': 'foobar', 'hello': 'world' });

    var beforeLoad = sysconfig.toJSON();
    var response = [];
    server.respondWith("GET", "api/system-configuration?config-type=foobar",
                       [200, {"content-type": "application/json"},
                       JSON.stringify(response)]);

    var successSpy = this.spy();
    var errorSpy = this.spy();

    sysconfig.loadOne().then(successSpy, errorSpy);

    server.respond();

    Assert(sysconfig.toJSON()).shouldEqual(beforeLoad);
    ok(successSpy.calledOnce, "Success should have been called after successful mocked server communication.");
    Assert(errorSpy.callCount).shouldEqual(0);
});

test('SystemConfiguration.loadOne multiple responses', function() {
    var server = this.sandbox.useFakeServer();
    var sysconfig = new slick.models.SystemConfiguration({ 'configurationType': 'foobar' });

    var response = [{
        'id': 1,
        'className': 'org.tcrun.slickij.api.data.AMQPSystemConfiguration',
        'configurationType': 'amqp-system-configuration'
    },
    {
        'id': 2,
        'className': 'org.tcrun.slickij.api.data.AMQPSystemConfiguration',
        'configurationType': 'amqp-system-configuration'
    }];
    server.respondWith("GET", "api/system-configuration?config-type=foobar",
        [200, {"content-type": "application/json"},
            JSON.stringify(response)]);

    var successSpy = this.spy();
    var errorSpy = this.spy();

    sysconfig.loadOne().then(successSpy, errorSpy);

    server.respond();

    Assert(sysconfig.toJSON()).shouldEqual(response[0]);

    ok(successSpy.calledOnce, "Success should have been called after successful mocked server communication.");
    Assert(errorSpy.callCount).shouldEqual(0);

});

