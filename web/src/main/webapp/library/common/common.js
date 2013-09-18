Jnrd = {};
Jnrd.Wtf = {};
Jnrd.Wtf.View = {
	navigateTo : function(url, reqData, useFrame) {
		if(useFrame == null)
			useFrame = false;
		if(useFrame == false){
			$.ajax({url: url, 
				type: 'GET', 
				data: reqData, 
				dataType: 'html', 
				timeout: 1000, 
				error: function(){
					this.updateContent('page request error!');
				}, 
				success: function(result){
					this.updateContent(result);
				},
				updateContent: function(content){
					var container = $('#home_content_part');
					container.html(content);
				}
			}); 
		}
		else{
			var iframe = document.createElement("iframe");
			//todo reqData
			iframe.src = url;
			iframe.className = "contentiframe";
			iframe.style.width = (document.documentElement.clientWidth - 240 - 32) + "px";
			iframe.style.height = (document.documentElement.clientHeight - 106) + "px";
			
			var container = $('#home_content_part');
			container.empty();
			container.append(iframe);
		}
	}
};
