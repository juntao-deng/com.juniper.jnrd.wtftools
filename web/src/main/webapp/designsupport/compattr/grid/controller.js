wdefine(function(){
	$app.component("entitybutton").on("click", function(){
		var event = {action: 'entity'};
		FwBase.Wtf.Design.DesignSupport.interactWithEclipse(event, callbackForEntity);
	});
	
	function callbackForEntity(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		var model = $app.model('columnsModel');
		var columnInfos = infos.columnInfos;
		if(columnInfos && columnInfos.length > 0){
			for(var i = 0; i < columnInfos.length; i ++){
				model.addRow(columnInfos[i]);
			}
		}
	}
});