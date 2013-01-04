
(function() {
	// this self executing function is the javascript way to make sure that variables don't
	// creep into the global namespace.  You have to explicitly put variables into the global
	// namespace by prefixing them with 'window.'

	var slick = {
		models: {},
		collections: {}
	}

	// ---------------------- Models ------------------------------------------

	slick.models.SystemConfiguration = Backbone.DeepModel.extend({
		loadOne: function() {
            return $.ajax({
                url: "api/system-configuration?config-type=" + this.get('configurationType'),
                model: this,
                success: function(data) {
                    this.model.set(data);
                }
            });

		}
	});

	slick.models.AMQPSystemConfiguration = slick.models.SystemConfiguration.extend({
		defaults: {
			'className': 'org.tcrun.slickij.api.data.AMQPSystemConfiguration',
			'configurationType': 'amqp-system-configuration',
			'name': 'AMQP System Configuration'
		}
	});

		window.slick = slick;

})()





