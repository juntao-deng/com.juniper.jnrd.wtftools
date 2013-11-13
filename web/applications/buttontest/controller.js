wdefine(function(){
	$app.component("ddd").on("click", function(){
		RestUtil.req("/rest/devices", $app.model("asdfa"), function(){
			
		});
	});
});