/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 9/3/12
 * Time: 8:57 PM
 */
var ReportsAddTestrunGroupPage = SlickPage.extend({
    group: "reports",
    codename: "testrungroup-add",
    name: "Add Test Run Group",

    initialize: function() {
        this.on("finish", this.onFinish, this);
    },

    onFinish: function() {
        $("#add-testrungroup-form").on("submit", {page: this}, this.onAdd);
        $("#add-testrungroup-form-submit").on("click", {page: this}, this.onAdd);
    },

    onAdd: function(event) {
        event.preventDefault();
        var name = $("#add-testrungroup-form-name").val();
        var page = event.data.page;
        $.ajax({
            url: "api/testrungroups",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({name: name}),
            success: function() {
                window.onPageChange();
            },

            error: function() {
                page.error("Adding new testrungroup failed.");
                window.onPageChange(); // reload page
            }
        });
    }
});
