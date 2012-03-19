var ReportViewLogs = SlickPage.extend({
    name: "View Logs",
    codename: "viewlogs",
    group: "reports",

    initialize: function() {
        if(!(this.options.result)){
            this.requiredData = {
                result: function() {
                    return "api/results/" + this.options.positional[0];
                }
            }
        }
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        if(this.options.result) {
            this.result = this.options.result;
        }
        else {
            this.result = this.data.result;
        }
    },

    onFinish: function() {
        var logEntries = [];
        _.each(this.result.log, function(logEntry, index){
            var combinedMessage = "<pre style=\"white-space: pre-wrap\">";
            if (logEntry.message) {
                combinedMessage += logEntry.message;
            }
            if (logEntry.exceptionClassName) {
                combinedMessage += logEntry.exceptionClassName;
            }
            if (logEntry.exceptionMessage) {
                combinedMessage += logEntry.exceptionMessage;
            }
            if (logEntry.exceptionStackTrace) {
                combinedMessage += logEntry.exceptionStackTrace
            }

            var loggernameArray = logEntry.loggerName.split('.');
            var loggername = loggernameArray[loggernameArray.length - 1];

            combinedMessage += "</pre>"
            logEntries[index] = [new Date(logEntry.entryTime).toLocaleTimeString(), logEntry.level,
                "<span class=\"force-wrap\" title=\"" + logEntry.loggerName + "\">" + loggername + "</span>", combinedMessage];
        });
        var datatable = $("#viewlogtable").dataTable({
            aaData: logEntries,
            aoColumns: [
                {"sTitle": "Time", "sWidth": "7%"},
                {"sTitle": "Level", "sWidth": "7%"},
                {"sTitle": "Logger Name", "sWidth": "18%"},
                {"sTitle": "Message", "sWidth": "68%"}],
            bJQueryUI: true,
            bAutoWidth: false,
            bDeferRender: true,
            bPaginate: false,
            sDom: '<"H"lfr<"clear">>tS<"F"ip>',
            sScrollY: "" + $(document).height() - $("#viewlogtable").position().top - ($(document).height() * .2) + "px"
        });
        datatable.fnSort([[0, "desc"]]);
    }
});
