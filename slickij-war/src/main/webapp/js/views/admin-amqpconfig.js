/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 12/19/12
 * Time: 9:48 AM
 */

var AdministrationAMQPConfigPage = SlickPage.extend({
    name: "Event Configuration",
    codename: "amqpconfig",
    group: "admin",
    navigation: true,

    requiredData: {
        "configs": "api/system-configuration?config-type=amqp-system-configuration"
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        if(_.size(this.data.configs) > 0) {
            this.config = this.data.configs[0];
        }
    },

    onFinish: function() {
        if(_.has(this, "config")) {
            this.setValues(this.config);
        }
        $(".view-form-editable").editable(this.onEdit, {page: this});
    },

    onEdit: function(value, settings) {
        var field = $(this).data("fieldname");
        var config = {className: "org.tcrun.slickij.api.data.AMQPSystemConfiguration"}; // we always have to provide the class name
        config[field] = value;

        if(_.has(settings.page, "config")) {
            // we need to update a config
            $("#admin-amqpconfig-error-container").hide();
            $.ajax({
                url: "api/system-configuration/" + settings.page.config.id,
                type: "PUT",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(config),
                success: function(data) {
                    $.jGrowl("Successfully to set field '" + field + "' to value '" + value + "' in system configuration.");
                    settings.page.setValues(data);
                    settings.page.config = data;
                    settings.page.reloadService();
                },
                error: function() {
                    $("#admin-amqpconfig-error").text("Unable to set field '" + field + "' to value '" + value + "' in system configuration, please enter a valid value.");
                    settings.page.setValues(settings.page.config);
                    $("#admin-amqpconfig-error-container").show(150);
                }
            });
        } else {
            // we need to create a new config
            config.configurationType = "amqp-system-configuration";
            config.name = "AMQP System Configuration";
            $.ajax({
                url: "api/system-configuration",
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(config),
                success: function(data) {
                    $.jGrowl("Successfully to set field '" + field + "' to value '" + value + "' in system configuration.");
                    settings.page.setValues(data);
                    settings.page.config = data;
                    settings.page.reloadService();
                },
                error: function() {
                    $("#admin-amqpconfig-error").text("Unable to set field '" + field + "' to value '" + value + "' in system configuration, please enter a valid value.");
                    $("#admin-amqpconfig-" + field).text("Click to Edit");
                    $("#admin-amqpconfig-error-container").show(150);
                }
            })
        }

        return value;
    },

    setValues: function(config) {
        _.each(["hostname", "port", "username", "password", "virtualHost", "exchangeName"], function(field) {
            if(_.has(config, field)) {
                $("#admin-amqpconfig-" + field).text(config[field]);
            } else {
                $("#admin-amqpconfig-" + field).text("Click to Edit");
            }
        }, this);
    },

    reloadService: function() {
        $.ajax({
            url: "api/reload/event-system",
            dataType: "json",
            success: function(data) {
                if(data.systemStatus == "ERROR") {
                    $("#admin-amqpconfig-error").text("Unable to connect to AMQP Server, please check values and ensure the system is up.");
                    $("#admin-amqpconfig-error-container").show(150);
                } else {
                    $.jGrowl("Successfully connected to AMQP server.");
                }
            }
        });
    }


});
