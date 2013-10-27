wdefine(function(){
	$app.component("top_menu").item("add").on("click", function(obj){
		obj.eventCtx.height = 500;
		obj.eventCtx.width = 900;
	});
});