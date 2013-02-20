/**
 * The email subscription page.  This page will allow you to input an email address and get a list of subscriptions.
 * User: jcorbett
 * Date: 2/19/13
 * Time: 1:11 PM
 */

var EmailSubscriptionPage = SlickPage.extend({
    name: "Edit Email Subscriptions",
    codename: "emailsubscriptions",
    group: "reports",
    navigation: true,

    initialize: function() {
        if(this.options.positional.length > 0) {
            this.needEmail = false;
            this.address = this.options.positional[0];
            this.requiredData = {
                projects: "api/projects",
                environments: "api/configurations?configurationType=ENVIRONMENT",
                emailsettings: "api/system-configuration?config-type=email-subscription&name=" + this.address
            };
            this.on("dataRecieved", this.onDataRecieved, this);
        } else {
            this.needEmail = true;
        }


        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onDataRecieved: function(event) {
        key = event[0];
        data = event[1];

        if(key == 'projects') {
            _.each(data, function(project) {
                this.addRequiredData(project.id + "-testplans", "api/testplans?projectid=" + project.id);
            }, this);
        }

    },

    onReady: function() {
        if(!this.needEmail)
        {
            if(this.data.emailsettings.length > 0)
            {
                if(! this.data.emailsettings[0].subscriptions) {
                    this.data.emailsettings[0].subscriptions = [];
                }
                this.emailsettings = new slick.models.EmailSubscription(this.data.emailsettings[0]);
            } else
            {
                this.emailsettings = new slick.models.EmailSubscription({
                    name: this.address,
                    enabled: true,
                    subscriptions: []
                });
            }
            this.globalSubscription = this.isSubscribed("Global", "");
            this.availableSubscriptions = [];
            _.each(this.data.projects, function(project) {
                this.availableSubscriptions.push(this.generateDataLine("Project", project.id, project.name, ""));
                if(project.releases) {
                    _.each(project.releases, function(release) {
                        this.availableSubscriptions.push(this.generateDataLine("Release", release.id, release.name, project.name));
                    }, this);
                }
                var testplans = this.data[project.id + "-testplans"];
                if(testplans) {
                    _.each(testplans, function(testplan) {
                        this.availableSubscriptions.push(this.generateDataLine("Testplan", testplan.id, testplan.name, project.name));
                    }, this);
                }
            }, this);
        }
    },

    onFinish: function() {
        if(this.needEmail) {
            $("#reports-emailsubscriptions-emailaddress-form").on("submit", this.onEmailAddressSubmit);
            $("#reports-emailsubscriptions-emailaddress-submit").on("click", this.onEmailAddressSubmit);
        } else {
            $("#reports-emailsubscriptions-available").dataTable({
                aaData: this.availableSubscriptions,
                aoColumns: [
                    {"sTitle": "Subscribed", "sWidth": "10%", "sType": "html"},
                    {"sTitle": "Subscription Type", "sWidth": "20%"},
                    {"sTitle": "Name", "sWidth": "50%"},
                    {"sTitle": "Project", "sWidth": "20%"}
                ],
                bJQueryUI: true,
                bAutoWidth: false,
                bPaginate: false,
                sDom: "<\"H\"lfr>tS<\"F\"ip>",
                sScrollY: "" + ($("#content").height() - (3 * $("#reports-emailsubscriptions-address-name").height()) - (5 * $("#footer").height()) - $("#content-bottom-pad").height()) + "px"
            });
            this.$el.on("change", ".reports-emailsubscriptions-subscription", {page: this}, this.onSubscriptionChanged);
        }

    },

    onSubscriptionChanged: function(event) {
        var page = event.data.page;
        var type = $(event.target).attr("value");
        var value = $(event.target).attr("name");
        var added = $(event.target).is(":checked");
        if(added) {
            page.addSubscription(type, value);
        } else {
            page.removeSubscription(type, value);
        }

    },

    onEmailAddressSubmit: function(event) {
        event.preventDefault();
        $.address.value("reports/emailsubscriptions/" + $("#reports-emailsubscriptions-emailaddress").val());
    },

    generateDataLine: function(type, value, name, project) {
        var dataline = [];
        var checkbox = "<input type=\"checkbox\" class=\"reports-emailsubscriptions-subscription\" id=\"" + value + "\" name=\"" + value + "\" value=\"" + type + "\" ";
        if(this.isSubscribed(type, value)) {
            checkbox = checkbox + "checked ";
        }
        checkbox = checkbox + "/>";
        dataline.push(checkbox);
        dataline.push(type);
        dataline.push(name);
        dataline.push(project);

        return dataline;
    },

    isSubscribed: function(type, value) {
        var subscribed = false;
        return _.find(this.emailsettings.get("subscriptions"), function (subscription) {
             return type == subscription.subscriptionType && value == subscription.subscriptionValue;
        });
    },

    addSubscription: function(type, value) {
        subscriptions = this.emailsettings.get('subscriptions');
        if(! subscriptions) {
            subscriptions = [];
        }
        subscriptions.push({onStart: false, subscriptionType: type, subscriptionValue: value});
        this.emailsettings.set({subscriptions: subscriptions});
        this.emailsettings.save().done(function(data) {
            $.jGrowl("Subscription updated");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.jGrowl("Problem saving subscription: " + textStatus);
            window.onPageChange();
        });
    },

    removeSubscription: function(type, value) {
        var subscriptions = _.filter(this.emailsettings.get("subscriptions"), function(subscription) {return !(subscription.subscriptionType == type && subscription.subscriptionValue == value)});
        this.emailsettings.set({subscriptions: subscriptions});
        this.emailsettings.save().done(function(data) {
            $.jGrowl("Subscription removed");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.jGrowl("Problem removing subscription: " + textStatus);
            window.onPageChange();
        });
    }
});
