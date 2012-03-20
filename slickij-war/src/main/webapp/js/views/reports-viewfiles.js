var ReportsViewFilesPage = SlickPage.extend({
    name: "Files For Failed Test Cases",
    codename: "viewfiles",
    group: "reports",
    //TODO: Set this to false
    navigation: true,

    initialize: function() {
        if(! this.options.result) {
            this.requiredData = {
                "results": function() {
                    //return "api/results/" + this.options.positional[0];
                    //TODO: Remove this line
                    return "api/results/4f5fc2f53677706940bb0702";
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
//        $(".file").on("click", { page: this }, this.displayFiles());
        console.log("build the dang filelist!!");
        this.buildFileList();

        prettyPrint();
    },

    buildFileList: function() {
        _.each(this.result.files, function(file) {

            var mimeType = file.mimetype;
            var fileName = file.filename;
            var fileId   = file.id;
            var html = "";

            if (mimeType.indexOf("text/html") != -1) {

                html += "<div class=\"fileicon\">";
                html += "<img class=\"file box\" src=\"/images/htmlicon.png\" alt=\"" + fileName + "\"/>";
                html += "<a class=\"label filename\" href=\"/api/files/" + fileId + "/content/" + fileName + "\">" + fileName + "</a>";
                html += "</div>";

            } else if (mimeType.indexOf("image/png") != -1) {

                html += "<div class=\"fileicon\">";
                html += "<img class=\"file box\" src=\"/images/htmlicon.png\" alt=\"" + fileName + "\"/>";
                html += "<a class=\"label filename\" href=\"/api/files/" + fileId + "/content/" + fileName + "\">" + fileName + "</a>";
                html += "</div>";
                //console.log("<pre class=\"prettyprint lang-html\">" /api/files/" + fileId + "/content/" + fileName + "\">" + fileName + "</a>");
            }

            $('#filelist').append(html);
        });
    },

    displayFile: function(event) {
        console.log("testing");
//        _.each(this.result.files, fucntion(id, filename, mimetype)
//        {
//
//        }
//        )
//
//        );

        //build the contents of hte file display div
        //if mime type = text/html
        // render <pre> tags
        //else mime type = image/png
        //files.id
        //page.template(name, filename, $('filedisplaycontainter'));
        //http://localhost:8080/api/files/{{id}}/content/{{filename}}" alt="{{filename}}
    }

});
