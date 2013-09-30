window.DesignMode = true;
window.DesignConfig = {
	baseRequirePath : '../library/components',
	localPrefix : {
		html : "html",
		template : "templ"
	},
	prefix : function(key, ori){
		return this.localPrefix[key] ? this.localPrefix[key] : ori;
	}
};


function sys_getBaseLocation() {
	var location = document.location.href;
	var index = location.indexOf("design.html");
	return location.substring(0, index);
}


window.baseLocation = sys_getBaseLocation();

window.wtfTempLoaderContainer = [];
function getWtfTempLoaderIframe(){
	for(var i = 0; i < window.wtfTempLoaderContainer.length; i ++){
		if(window.wtfTempLoaderContainer[i].inUsing == false){
			window.wtfTempLoaderContainer[i].inUsing = true;
			return window.wtfTempLoaderContainer[i];
		}
	}
	//not find
	var iframe = document.createElement("iframe");
	iframe.style.position = "absolute";
	iframe.style.height = "1px";
	iframe.style.width = "0px";
	iframe.style.left = "-100px";
	document.body.appendChild(iframe);
	iframe.inUsing = true;
	window.wtfTempLoaderContainer.push(iframe);
	return iframe;
}