wdefine(['css!../../applications/home/theme'], function(){
	var homeModel = new FwBase.Wtf.Model("navmodel", {url: 'home/homeinfos'});
	var app = FwBase.Wtf.Application.current();
	app.model(homeModel);
});
