define(function(){
	model.url = "devices";
	menu.on(click, function(){
		$app.model('list').trigger("sync");
	});
});
	
	