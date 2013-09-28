window.wtfTempLoaderContainer = document.createElement("iframe");
//window.wtfTempLoaderContainer.style.position = "absolute";
//window.wtfTempLoaderContainer.style.height = "1px";
//window.wtfTempLoaderContainer.style.width = "0px";
document.body.appendChild(window.wtfTempLoaderContainer);
window.baseLocation = sys_getBaseLocation();
define({
    	load: function (name, req, onload, config) {
        	var id = name.substring(0, name.lastIndexOf("."));
        	var truePath = config.paths[id];
        	if(truePath == null)
        		truePath = name;
        	var url = window.baseLocation + req.toUrl(truePath);
        	window.wtfTempLoaderContainer.src = url;
        	$(window.wtfTempLoaderContainer).load(function(){
        		var content = $(window.wtfTempLoaderContainer.contentWindow.document.getElementsByTagName("head")).html();
        		onload(content);
        	});
    	}
     }
);

function sys_getBaseLocation() {
	var location = document.location.href;
	var index = location.indexOf("design.html");
	return location.substring(0, index);
}