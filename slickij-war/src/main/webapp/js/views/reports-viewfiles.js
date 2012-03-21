var ReportsViewFilesPage = SlickPage.extend({
    name: "Files For Failed Test Cases",
    codename: "viewfiles",
    group: "reports",
    //TODO: Set this to false
    navigation: false,

    initialize: function() {
        if(! this.options.result) {
            this.requiredData = {
                "results": function() {
                    return "api/results/" + this.options.positional[0];
                    //TODO: Remove this line
                    //return "api/results/4f5fc2f53677706940bb0702";
                }
            };
        }
        this.on("ready", this.onReady, this);
        this.on("finish", this.onFinish, this);
    },

    onReady: function() {
        if(this.options.result) {
            this.result = this.options.result;
        } else {
            this.result = this.data.results;
        }
        this.title = "File List For Testcase: '" + this.result.testcase.name + "'";
    },

    onFinish: function() {
        this.buildFileList();
        prettyPrint();
    },

    buildFileList: function() {
        _.each(this.result.files, function(file) {

            var mimeType = file.mimetype;
            var fileName = file.filename;
            var fileId   = file.id;
            var fileContent = "<html><header><title>This is a test!</title></header><body><h1>This is to test pretty files!!!</h1></body></html>";
            var html = "";
            var html2 = "";

            if (mimeType.indexOf("text/html") != -1) {

                //Build the list of files
                html += "<div class=\"fileicon\">";
                html += "<img class=\"file box\" src=\"/images/htmlicon.png\" alt=\"" + fileName + "\"/>";
                html += "<a class=\"label filename\" href=\"/api/files/" + fileId + "/content/" + fileName + "\">" + fileName + "</a>";
                html += "</div>";

                //Build the file viewer
                html2 += "<div class=\"filedisplay\" data-fileid=\"" + fileId + "\" data-filename=\"" + fileName + "\">";

                $.ajax({
                    type: "GET",
                    url: "api/files/" + fileId + "/content/" + fileName,
                    dataType: "html",
                    success: function(html) {
                        //console.log("Success:" + html);
                        html2 += "<code id=\"" + fileId + "\" class=\"prettyprint lang-html\" data-fileUrl=\"/api/files/" + fileId + "/content/" + fileName + "\" >" + html + "</code>";
                    },
                    error: function() {
                        html2 += "<code id=\"" + fileId + "\" class=\"prettyprint lang-html\" data-fileUrl=\"/api/files/" + fileId + "/content/" + fileName + "\" >Could not retrieve the file from the server.</code>";
                    }
                });

                html2 += "</div>";
                console.log(html2);

            } else if (mimeType.indexOf("image/png") != -1) {

                html += "<div class=\"fileicon\">";
                html += "<img class=\"file box\" src=\"/api/files/" + fileId + "/content/" + fileName + "\" alt=\"" + fileName + "\"/>";
                html += "<a class=\"label filename\" href=\"/api/files/" + fileId + "/content/" + fileName + "\">" + fileName + "</a>";
                html += "</div>";

                //Build the file viewer
                html2 += "<div class=\"filedisplay\">";
                html2 += "<img src=\"/api/files/" + fileId + "/content/" + fileName + "\" alt=\"" + fileName + "\"/>";
                html2 += "</div>";
            }

            $('#filelist').append(html);
            $('#fileviewer').append(html2);
        });

    },

    getHtmlFile: function(element, fileName, fileId) {

        $(element).load("api/files/" + fileId + "/content/" + filename, function(response, status, xhr) {
            if (status == "error") {
                ($element).append("Could not find file: '" + fileName + "'" + "Error: " + xhr.status + " " + xhr.message);
                page.error("Could not find file: '" + fileName + "'" + "Error: " + xhr.status + " " + xhr.message);
            }
        });

    }

});
