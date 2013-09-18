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
					var reserve = $('footer');
					var container = $('#home_content_part');
					container.html(content);
					if(reserve != null)
						container.append(reserve);
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
			var reserve = $('footer');
			var container = $('#home_content_part');
			container.empty();
			container.append(iframe);
			if(reserve != null)
				container.append(reserve);
		}
	}
};
