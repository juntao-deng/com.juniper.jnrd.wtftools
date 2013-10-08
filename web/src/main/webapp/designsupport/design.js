window.DesignMode = true;
window.ClientConfig = {
	baseRequirePath : window.designRemoteUrl + '../library/components',
	localPrefix : {
		html : "html",
		template : "templ",
		js : 'js'
	},
	prefix : function(key, ori){
		return this.localPrefix[key] ? this.localPrefix[key] : ori;
	}
};


//function sys_getBaseLocation() {
//	var location = document.location.href;
//	var index = location.indexOf("design.html");
//	return location.substring(0, index);
//}
//
//window.baseLocation = sys_getBaseLocation();
