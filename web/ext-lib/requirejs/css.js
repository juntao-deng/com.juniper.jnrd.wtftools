define({
    load: function (name, req, onload, config) {
    	var id = name.substring(0, name.lastIndexOf("."));
    	var truePath = config.paths[id];
    	if(truePath == null)
    		truePath = name;
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = req.toUrl(truePath + ".css");
        document.getElementsByTagName("head")[0].appendChild(link);
        onload();
    }
});