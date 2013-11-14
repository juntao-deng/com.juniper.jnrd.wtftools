wdefine(function(){
	DesignSupport.eventControllerWrapper();
	
	$app.component("entitybutton").on("click", function(){
		var event = {action: 'entity'};
		FwBase.Wtf.Design.DesignSupport.interactWithEclipse(event, callbackForEntity);
	});
	
	$app.component("generatebutton").on("click", function(){
		var event = {action: 'generatecode', selectedClass: this.data().selectedClass};
		DesignSupport.interactWithEclipse(event, callbackForGenerate);
	});
	function callbackForGenerate(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		else{
			//infos.generatedList
			//show msg
		}
	}
	function callbackForEntity(infos) {
		if(infos.errormsg){
			alert(infos.errormsg);
			return;
		}
		if(infos.selectedClass){
			$app.component("generatebutton").enable(true);
			$app.component("entityattr").value(infos.selectedClass);
			$app.component("generatebutton").data({restapiExist: infos.restapiExist, selectedClass: infos.selectedClass});
		}
		else{
			$app.component("generatebutton").enable(false);
			$app.component("entityattr").value("");
			$app.component("generatebutton").data({restapiExist: false});
		}
		
//		var model = $app.model('columnsModel');
//		var columnInfos = infos.columnInfos;
//		if(columnInfos && columnInfos.length > 0){
//			model.page().reset();
//			model.page().add(columnInfos);
//		}
//		$app.attr("restservice", infos.restservice);
//		if(infos.generateClass != null){
//			$app.attr('generateClass', infos.generateClass);
//			alert("The restful service for class '" + infos.generateClass + "' doesn't exist, it will be created after you click the 'Save Changes' Button");
//		}
	}
});