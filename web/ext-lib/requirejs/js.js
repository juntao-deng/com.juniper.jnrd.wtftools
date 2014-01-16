define({
    load: function (name, req, onload, config) {
    	var id = name;
    	var truePath = config.paths[id];
    	if(truePath == null)
    		truePath = name;
        var link = document.createElement("script");
        link.src = req.toUrl(truePath + ".js");
        document.getElementsByTagName("head")[0].appendChild(link);
        link.onload = function() {
        	onload();
        }
    }
});