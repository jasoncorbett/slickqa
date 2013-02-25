
module("slick.models.AMQPSystemConfiguration");

test('AMQPSystemConfiguration Model Exists', function() {
	var eventConfigClass = slick.models.AMQPSystemConfiguration;
    Assert(eventConfigClass).shouldBeType('function')
	var config = new eventConfigClass({});
    Assert(config).shouldBeInstanceOf(slick.models.SystemConfiguration, 'slick.models.SystemConfiguration');
});

test('AMQPSystemConfiguration Model Defaults', function() {
	var eventConfiguration = new slick.models.AMQPSystemConfiguration({});
	Assert(eventConfiguration.toJSON()).shouldHaveProperty('className', 'org.tcrun.slickij.api.data.AMQPSystemConfiguration');
    Assert(eventConfiguration.toJSON()).shouldHaveProperty('configurationType', 'amqp-system-configuration');
    Assert(eventConfiguration.toJSON()).shouldHaveProperty('name', 'AMQP System Configuration');
});
