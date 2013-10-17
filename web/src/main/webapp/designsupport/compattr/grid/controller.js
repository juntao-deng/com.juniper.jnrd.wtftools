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
		var rows = infos.columnInfos;
		if(rows && rows.length > 0){
			for(var i = 0; i < rows.length; i ++){
				model.addRow(rows[i]);
			}
		}
	}
});