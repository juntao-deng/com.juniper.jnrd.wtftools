window.DesignMode = true;
window.DesignConfig = {
	baseRequirePath : '../library/controls',
	localPrefix : {
		text : 'html',
	},
	prefix : function(ori){
		return this.localPrefix[ori] ? this.localPrefix[ori] : ori;
	}
};