wdefine(function(){
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = $app.parent.component(compId);
		var metadata = comp.metadata;
		$app.component('idattr').value(compId);
		$app.component('editableattr').checked(metadata.editable);
	});
	
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
			alert(columnInfos);
			model.addRow(columnInfos);
		}
	}
	
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var idattr = $app.component('idattr').value();
		var editableattr = $app.component('editableattr').checked();
		var model = $app.model('columnsModel');
		var rows = model.page().rows();
		if(rows == null || rows.length == 0){
			alert("no columns defined");
			return;
		}
		var columns = [];
		for(var i = 0; i < rows.length; i ++){
			columns[i] = rows[i].toJSON();
		}
		
		var md = {'columns': columns, editable : editableattr};
		
		var controller = null;
		var rest = null;
		updateComponent(idattr, md, controller, rest);
		$app.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncAppFiles(id, md, controller, rest);
	}
});