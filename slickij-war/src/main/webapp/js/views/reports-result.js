/**
 * Created by IntelliJ IDEA.
 * User: jcorbett
 * Date: 3/14/12
 * Time: 9:40 AM
 */

var ReportsResultViewPage = SlickPage.extend({
    group: "reports",
    name: "result",

    requiredData: {
        result: function() {
            return "api/results/" + this.options.positional[0];
        }
    },

    initialize: function() {
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        this.title = "'" + this.data.result.testcase.name + "' Result Details";
        this.data.result.statusClass = this.data.result.status.replace("_", "");
        this.data.result.statusName = this.data.result.status.replace("_", " ");
        this.data.result.recordedTime = new Date(this.data.result.recorded);
        this.data.result.recordedTimeString = this.data.result.recordedTime.toLocaleString();
        this.data.result.runlengthString = getDurationMilliseconds(this.data.result.runlength);

        this.pages = {
            logs: new ReportViewLogs({positional: [], query: {}, noSetTitle: true, result: this.data.result}),
            files: new ReportsViewFilesPage({positional: [], query: {}, noSetTitle: true, result: this.data.result}),
            testcase: new TestManagementViewTestcasePage({positional: [this.data.result.testcase.testcaseId], query: {}, noSetTitle: true}),
            environment: new ComingSoonPage({positional: [], query: {}, noSetTitle: true})
        };
    },

    onFinish: function() {
        var resultViewPage = this;
        this.rendered = 0;
        this.totalSubPages = 4;
        this.tabApplied = false;
        _.each(this.pages, function(value, key) {
            value.on("render", function() {
                $("#result-tab-" + key).append(this.el);
                resultViewPage.rendered++;
            value.on("finish", function() {
                if( (!resultViewPage.tabApplied) && resultViewPage.rendered == resultViewPage.totalSubPages) {
                    $("#result-tab-page").tabs();
                    resultViewPage.tabApplied = true;
                }
            }, value);

            });
            value.pageStart();
        }, this);
    }

});
