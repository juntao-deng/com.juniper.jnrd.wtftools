wdefine(['css!../../applications/home/theme'], function(){
	var homeModel = new FwBase.Wtf.Model({url: "/" + window.mainCtx + '/home/homeinfos'});
	var app = FwBase.Wtf.Application.current();
	app.model("navmodel", homeModel);
});
