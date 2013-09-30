define({
    	load: function (name, req, onload, config) {
        	var id = name.substring(0, name.lastIndexOf("."));
        	var truePath = config.paths[id];
        	if(truePath == null)
        		truePath = name;
        	var url = window.baseLocation + req.toUrl(truePath);
        	var iframe = getWtfTempLoaderIframe();
        	iframe.src = url;
        	$(iframe).load(function(){
        		var currFrame = $(this)[0];
        		var content = $(currFrame.contentWindow.document.body).html();
        		currFrame.inUsing = false;
        		onload(content);
        	});
    	}
     }
);
