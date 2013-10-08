var app = FwBase.Wtf.Application.current();
var homeModel = app.model("navmodel");
homeModel.on("add", function(){
	var model = homeModel.page().at(0);
	var navList = model.get("navList");
	var sidenav = app.component("homesidenav");
	for(var i = 0; i < navList.length; i ++){
		sidenav.addItem(navList[i]);
	}
});

FwBase.Wtf.Application.navigateTo("dashboard");