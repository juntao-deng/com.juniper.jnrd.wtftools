wdefine(function(){
	var colsinput = $app.component('colsinput');
	colsinput.on('valuechange', function(options){
		var value = this.value();
		var slider = $app.component("colslider");
		var values = [];
		var enable = true;
		if(value == 1){
			values = [12];
			enable = false;
		}
		else if(value == 2){
			values = [6];
		}
		else if(value == 3){
			values = [4, 8];
		}
		else{
			values = [12];
			enable = false;
		}
		var meta = slider.cloneMetadata();
		meta.values = values;
		meta.enable = enable;
		slider.reset(meta);
	});

	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var colsinput = $app.component('colsinput');
		var rowsinput = $app.component('rowsinput');
		var slider = $app.component('colslider');
		var cols = colsinput.value();
		var rows = rowsinput.value();
		var sliders = slider.values();
		$app.close();
		insertLayout(rows, cols, sliders);
		FwBase.Wtf.Design.DesignSupport.designable();
		FwBase.Wtf.Design.DesignSupport.syncHtml();
	});

	function insertLayout(rows, cols, sliders){
		var str = "";
		for(var i = 0; i < rows; i ++){
			str += '<div class="row-fluid">';
				if(cols == 1){
					str += '<div class="span12" wtftype="container"></div>';
				}
				else if(cols == 2){
					str += '<div class="span' + sliders[0] + '" wtftype="container"></div>';
					str += '<div class="span' + (12 - parseInt(sliders[0])) + '" wtftype="container"></div>';
				}
				else if(cols == 3){
					str += '<div class="span' + sliders[0] + '" wtftype="container"></div>';
					str += '<div class="span' + (parseInt(sliders[1]) - parseInt(sliders[0])) + '" wtftype="container"></div>';
					str += '<div class="span' + (12 - parseInt(sliders[1])) + '" wtftype="container"></div>';
				}
				else{
					var average = parseInt(12 / cols);
					for(var j = 0; j < cols - 2; j ++){
						str += '<div class="span' + average + '" wtftype="container"></div>';
					}
					str += '<div class="span' + (12 - (cols - 1)* average) + '" wtftype="container"></div>';
				}
			str += '</div>';
		}
		
		var menu = FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu');
		menu.remove();
		FwBase.Wtf.Design.DesignSupport.currParent.append(str);
	}
});
