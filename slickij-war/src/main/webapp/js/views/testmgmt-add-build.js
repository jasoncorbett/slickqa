/*
 * ---------------------------------------------------------------------------
 * testmgmt-add-build.js
 * ---------------------------------------------------------------------------
 */
function loadReleaseList1()
{
	$.ajax({
	url: "api/projects/" + getCurrentProject().id,
	type: "GET",
	dataType: "json",
	success: function(data1) 
	{
		var data2 = typeof data1 != 'object' ? JSON.parse(data1) : data1;
		var index = 0;
		var includepass = true;
		var elSel = document.getElementById('releaseList');
		$("#releaseList").empty();
		// Add an Option object to Drop Down/List Box
		for(var i = 0;i<data2.releases.length; i++)
		{
			var opt = document.createElement("option");
			opt.text = data2.releases[i].name;
			opt.value = data2.releases[i].id;
			document.getElementById("releaseList").options.add(opt);
		}
	},
	error: function(jqXHR, textStatus, errorThrown)
	{
		alert(textStatus+ " :" + errorThrown);
	}
	});
}

Pages.group("testmgmt").page("addbuild").addRequiredTemplate("managetests-add-build").addRequiredData("proj", function() {
	return "api/projects/" + getCurrentProject().id;
}).addDisplayMethod(function() {
	var dat = this.data["proj"];
	var includepass = true;
	setSlickTitle("Add Build");
	if(dat.releases.length == 0)
	{
		includepass = false;
	}
	$.tmpl(this.templates["managetests-add-build"],{"includepass":includepass}).appendTo("#main");
	loadReleaseList1();
	$('#addBuild').click(function()
	{	
		var ur = "api/projects/"+ getCurrentProject().id + "/releases";
		var str = document.getElementById('buildName');
		if(str.value == "")
		{
		   alert("Build name cannot be empty");
		}
		else
		{
			   var d = document.getElementById("releaseList").value;
			   var buildName = str.value;
               $.ajax({
            	   		url:"api/projects/" + getCurrentProject().id + "/releases/" + d + "/builds", 
            	   		data: JSON.stringify({ "name": buildName }),
            	   		type: "POST",
            	   		success: function(data){
            	   			alert("Build added");
               			}, 
               			contentType: "application/json"
               		});
		}//end else
	}); //end click	   
}); //end addDisplayMethod

