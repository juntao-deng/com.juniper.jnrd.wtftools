define({
    	load: function (name, req, onload, config) {
        	var id = name.substring(0, name.lastIndexOf("."));
        	var truePath = config.paths[id];
        	if(truePath == null)
        		truePath = name;
        	var url = req.toUrl(truePath);
        	var iframe = getWtfTempLoaderIframe();
        	iframe.src = url;
        	$(iframe).load(function(){
        		var currFrame = $(this)[0];
        		try{
        			var body = $(currFrame.contentWindow.document.body);
        			var content = body.html();
        			if(content.startWith('<pre')){
        				content = $(body.children()[0]).html();
        			}
        		}
        		catch(error){
        			alert("can not access : " + $(this)[0].src);
        			throw error;
        		}
        		finally{
	        		currFrame.inUsing = false;
	        		onload(content);
        		}
        	});
    	}
     }
);
