wdefine(function(){
	var selectMd = {
		options : [
		     {text : "navmenu", value : "navmenu"},
		     {text : "navtree", value : "navtree"},
		     {text : "bodygrid", value : "bodygrid"},
		     {text : "landingpage", value : "landingpage"},
		     {text : "landingpagetree", value : "landingpagetree"}
		]
	};
	
	$app.metadata('templateinput', selectMd);
});