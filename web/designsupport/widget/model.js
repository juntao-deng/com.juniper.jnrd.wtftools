wdefine(function(){
	var selectMd = {
		id : 'templateinput', 
		options : [
		     {name : "navmenu", value : "navmenu"},
		     {name : "navtree", value : "navtree"},
		     {name : "bodygrid", value : "bodygrid"},
		     {name : "landingpage", value : "landingpage"},
		     {name : "landingpagetree", value : "landingpagetree"}
		]
	};
	
	$app.metadata(selectMd);
});