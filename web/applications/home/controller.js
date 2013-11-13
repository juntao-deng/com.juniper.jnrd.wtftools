wdefine(function(){
	$app.on('loaded', function(){
		FwBase.Wtf.Application.navigateTo(window.mainCtx + "/dashboard");
	});
	var homeModel = $app.model("navmodel");
	homeModel.on("add", function(){
		var row = this.page().at(0);
		var navList = row.get("navList");
		var sidenav = this.ctx.component("homesidenav");
		for(var i = 0; i < navList.length; i ++){
			sidenav.addItem(navList[i]);
		}
	});
	
});