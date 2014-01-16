define(["base/base", "./coolfieldset", "css!./coolfieldset"], function(base){
	FwBase.Wtf.View.Controls.Fieldset = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	$.extend(FwBase.Wtf.View.Controls.Fieldset.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_fieldset').html()),
			postInit : function(){
				this.fieldset = this.el.children("#fieldset");
				this.fieldset.append(this.fieldset.siblings());
				this.fieldset.coolfieldset({speed:"fast"});
			},
			makeDefault : function(){
				this.setDefault({collapse: false, legend: 'FieldSet'});
			}
		}
	);
	return FwBase.Wtf.View.Controls.Fieldset;
});