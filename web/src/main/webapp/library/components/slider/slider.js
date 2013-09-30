//loadCss("library/controls/slider/slider.css");
define(["base/base"], function(base, uislider){
	FwBase.Wtf.View.Controls.Slider = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Slider.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_slider').html()),
		postInit : function(){
			$("#" + this.id + " > #sys_slider").slider(
				{
	            	value: 6,
	                min: 0,
	                max: 12,
	                step: 1,
	                slide: function (event, ui) {
	                    $(this).siblings('#sys_slider_amount').text(ui.value);
	                }
	            }
			);
		},
		makeDefault : function(metadata){
			
		}
	});
	return FwBase.Wtf.View.Controls.Slider;
});