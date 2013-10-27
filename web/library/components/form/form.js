define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Form = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
		this.elements = [];
	};
	$.extend(FwBase.Wtf.View.Controls.Form.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_form').html()),
		postInit : function(){
			var oThis = this;
			requireComponent(["input_base", "input", "input_ip"], function(){
				for(var i = 0; i < oThis.metadata.elements.length; i ++){
					var elemeta = oThis.metadata.elements[i];
					elemeta.labelWidth = oThis.metadata.labelWidth;
					var container = oThis.el.find(".formelement#" + elemeta.name);
					oThis.elements.push(new FwBase.Wtf.View.Controls[Util.capitalize(elemeta.type)](container, elemeta, elemeta.name));
				}
				
			});
		},
		makeDefault : function() {
			
		},
		mockMetadata : function() {
			this.setDefault({rows : 2, labelWidth : 120, elements : [
			     {name: 'a', text: 'a', nextrow : false, rows : 1, type:'input'},{name: 'b', text: 'b', nextrow : false, rows : 1, type:'input'},
			     {name: 'c', text: 'c', nextrow : false, rows : 1, type:'input'},{name: 'd', text: 'd', nextrow : false, rows : 1, type:'input'},
			     {name: 'e', text: 'e', nextrow : false, rows : 1, type:'input'},{name: 'f', text: 'f', nextrow : false, rows : 1, type:'input'},
			]});
		}
	});
	return FwBase.Wtf.View.Controls.Form;
});