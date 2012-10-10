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
        _.each(this.result.files, function(file) {
            if(file.mimetype.match(/.*html.*/)) {
                file.isHtml = true;
                file.icon = "images/htmlicon.png"
            } else if(file.mimetype.match(/^image.*/)) {
                file.isImage = true;
                file.icon = "api/files/" + file.id + "/content/" + file.filename;
            } else if(file.mimetype.match(/^text.*/)) {
		        file.isText = true;
		        file.icon = "images/logs.png";
	        } else if(file.mimetype.match(/.*wmv.*/)) {
                file.isWMV = true;
                file.icon = "images/film.png";
            }
        });
    },

    onFinish: function() {
        var displaywidth = $("#viewfilebox").width() - (2 * $("#filelist").width());
        $("#fileviewer").width(displaywidth);
        $(".filedisplay").hide();
        $(".filedisplay:first-child").show();
        $("a.filename").on("click", function(event) {
            console.log(this);
            event.preventDefault();
            $(".filedisplay").hide();
            $("#" + $(this).data("fileid")).show();
            $("#filename").text($(this).data("filename"));
        });
	
	$(".textdisplay").each(function() {
	    var displaydiv = $(this);
	    var fileId = displaydiv.attr("id");
	    var fileName = displaydiv.data("fileName");
	    
	    $.ajax({
		type: "GET",
		url: "api/files/" + fileId + "/content/" + fileName,
		dataType: "text",
		displaydiv: displaydiv,
		success: function(text) {
		    this.displaydiv.text(text);
		    prettyPrint();
		}
	    })
	});
	
        $(".htmldisplay").each(function() {
            var displaydiv = $(this);
            var fileId = displaydiv.attr("id");
            var fileName = displaydiv.data("fileName");

            $.ajax({
                type: "GET",
                url: "api/files/" + fileId + "/content/" + fileName,
                dataType: "html",
                displaydiv: displaydiv,
                success: function(html) {
                    this.displaydiv.text(html);
                    prettyPrint();
                }
            });
        });

        $(".video-wmv").each(function() {
            var displaydiv = $(this);
            var movie = "api/files/" + displaydiv.attr("id") + "/content/" + displaydiv.data("fileName");
            var src = "media/wmvplayer.xaml";
            var cfg = {height: $("#viewfilebox").height(), width: $("#fileviewer").width(), backgroundcolor:'000000',file: movie,backcolor:'000000', frontcolor:'FFFFFF',windowless:'true'};
            var ply = new jeroenwijering.Player(this,src,cfg);
        });
    }
});
