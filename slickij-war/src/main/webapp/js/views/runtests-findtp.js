/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 3/19/12
 * Time: 7:56 PM
 */

var RunTestsFindTestplan = SlickPage.extend({
    codename: "findtp",
    name: "Find Testplan",
    group: "runtests",
    navigation: true,

    requiredData: {
        "plans": function() {
            return "api/testplans?projectid=" + getCurrentProject().id;
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },
    
    onReady: function() {
        this.tabledata = [];
        _.each(this.data.plans, function(plan) {
            this.tabledata[this.tabledata.length] = [
                plan.name,
                "<span id=\"testplan-tc-count-" + plan.id + "\">?</span>",
                "<a href=\"#/runtests/scheduletp/" + plan.id + "\" class=\"modal-link button\" >Schedule</a>"
            ];
        }, this);
    },
    
    onFinish: function() {
        $("#runtests-findtp-table").dataTable({
            aaData: this.tabledata,
            aoColumns: [
                {"sTitle": "Name", "sWidth": "60%"},
                {"sTitle": "Test Count", "sWidth": "20%", "sType": "html"},
                {"sTitle": "Actions", "sWidth": "20%", "sType": "html"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: "<\"H\"lfr>tS<\"F\"ip>",
            sScrollY: "" + ($(document).height() - $("#runtests-findtp-table").position().top -  (3 * $("#titlebar").height())) + "px"
        });

        _.each(this.data.plans, function(plan) {
            $.ajax({
                url: "api/testplans/" + plan.id + "/testcount",
                type: "GET",
                dataType: "json",
                plan: plan,
                success: function(data) {
                    $("#testplan-tc-count-" + this.plan.id).text(data);
                }
            });
        });
    }

});
