/*
 * ---------------------------------------------------------------------------
 * testmgmt-add-release.js
 * ---------------------------------------------------------------------------
 */
function loadReleaseList()
{
	$.ajax({
	url: "api/projects/" + getCurrentProject().id,
	type: "GET",
	dataType: "json",
	success: function(data1) 
	{
			var data2 = typeof data1 != 'object' ? JSON.parse(data1) : data1;
			var index = 0;
			var elSel = document.getElementById('selectX');
			$("#selectX").empty();
			// Add an Option object to Drop Down/List Box
			if(data2.releases.length == 0)
			{
				var opt = document.createElement("option");
				opt.text = "No release added yet";
				opt.value = "No release added yet";
				document.getElementById("selectX").options.add(opt);
			}
			else
			{
				for(var i = 0;i<data2.releases.length; i++)
				{
					var opt = document.createElement("option");
					opt.text = data2.releases[i].name;
					opt.value = data2.releases[i].name;
					document.getElementById("selectX").options.add(opt);
				}
			}
		
	},
	error: function(jqXHR, textStatus, errorThrown)
	{
		alert(textStatus+ " :" + errorThrown);
	}
	});	
}

Pages.group("testmgmt").page("addrelease").addRequiredTemplate("managetests-add-release").addRequiredData("proj", function() {
	return "api/projects/" + getCurrentProject().id;
}).addDisplayMethod(function() {
	
	var dat = this.data["proj"];
	var includepassed = true;
	if(dat.releases.length == 0)
	{
		includepassed = false;
	}
	setSlickTitle("Add Release");
	$.tmpl(this.templates["managetests-add-release"], {"current_project": getCurrentProject().name }).appendTo("#main");
	loadReleaseList();
	$("#current-project-select").change(function(evt) {
		//TODO:HANDLER FOR SELECTION CHANGE 
	});
	$('#addReleaseButton').click(function()
	{
		var ur = "api/projects/"+ getCurrentProject().id + "/releases";
		var str = document.getElementById('releasename');
		$.ajax({
		   		url: ur,
		   	    data: JSON.stringify({"name":str.value }),
		   	    type: "POST",
		   	    //dataType: "json",
		   	    success: function(json, status)
		   	    {
					alert("Release added");
		   			$("#releasename").val("");
		   	        if (status != "success") 
		   	        {
		   	        	alert("Error loading data");
		    	        //console.log("Error loading data");
		    	        return;
		            }
		    	    loadReleaseList();
		    	 },
		    	 contentType: "application/json",
		    	 error: function(result, status, err) 
		    	 {
		    	     //log("Error loading data");
		    	    alert(status+":"+err);
		    	    return;
		    	 }
		    });
	}); //end click	   
}); //end addDisplayMethod

