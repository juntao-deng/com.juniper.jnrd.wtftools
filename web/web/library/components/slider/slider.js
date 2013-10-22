define(["base/base"], function(base, uislider){
	FwBase.Wtf.View.Controls.Slider = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Slider.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_slider').html()),
		mockMetadata : function() {
			
		},
		makeDefault : function() {
			this.setDefault({min:0, max:100, step:1, bg: 'bg-green', range:(this.metadata.values && this.metadata.values.length > 1) ? true : false, values : [10, 50], valueLabel : '', valuePrefix : '', orientation: "vertical"});
		},
		postInit : function(){
			var oThis = this;
			this.slider = this.el.children('#slider');
			this.slider.slider(
				{
					range: oThis.metadata.range,
	            	values: oThis.metadata.values,
	                min: oThis.metadata.min,
	                max: oThis.metadata.max,
	                step: oThis.metadata.step,
	                slide: function (event, ui) {
	                	var str = joinValues(oThis, ui.values);
	                	$(this).siblings('#sys_slider_amount').text(str);
	                }
	            }
			);
			var str = joinValues(this, this.metadata.values);
			this.slider.siblings('#sys_slider_amount').text(str);
		},
		reInit : function(meta) {
			if(this.slider)
				this.slider.slider('destroy');
			for(var i in meta){
				this.metadata[i] = meta[i];
			}
			if(this.metadata.values.length == 1)
				this.metadata.range = false;
			this.postInit();
		},
		enable : function() {
			if(arguments.length == 0)
				return true;
		},
		values : function() {
			if(arguments.length == 0)
				return this.slider.slider("values");
			this.slider.slider("values", arguments[0]);
		}
	});
	function joinValues(obj, values){
		var str = (obj.metadata.valueLabel == '') ? '' : (obj.metadata.valueLabel + ":");
    	for(var i = 0; i < values.length; i ++){
    		str += obj.metadata.valuePrefix + values[i];
    		if(i != values.length - 1)
    			str += "/";
    	}
    	return str;
	}
	return FwBase.Wtf.View.Controls.Slider;
});