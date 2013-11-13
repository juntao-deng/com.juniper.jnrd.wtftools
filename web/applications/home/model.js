wdefine(['css!../../applications/home/theme'], function(){
	var homeModel = new FwBase.Wtf.Model({url: "homeinfos"});
	$app.model("navmodel", homeModel);
});
