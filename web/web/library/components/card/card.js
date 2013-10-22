define(["base/base", "tab/tab"], function(base){
	FwBase.Wtf.View.Controls.Card = function(){
		FwBase.Wtf.View.Controls.Tab.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Card.prototype, FwBase.Wtf.View.Controls.Tab.prototype, {
		templateInit: function(){
			return _.template($('#sys_atom_controls_card').html());
		},
		next : function() {
			
		}
	});
	return FwBase.Wtf.View.Controls.Card;
});