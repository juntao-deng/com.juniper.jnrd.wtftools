wdefine(function(){
	$app.on('loaded', function(){
		var compId = FwBase.Wtf.Design.DesignSupport.currParent.attr('id');
		var comp = $app.parent.component(compId);
		var metadata = comp.metadata;
		$app.component('idattr').value(compId);
		$app.component('editableattr').checked(metadata.editable);
		$app.component('multiattr').checked(metadata.multiselect);
		$app.component('heightattr').value(metadata.height);
		$app.component('minheightattr').value(metadata.minHeight);
		$app.component('pageattr').value(metadata.pagination != null);
		
		var columns = metadata.columns;
		var model = $app.model('columnsModel');
		model.page().add(columns);
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
			model.page().reset();
			model.page().add(columnInfos);
		}
		if(infos.generateClass != null){
			$app.attr('generateClass', infos.generateClass);
			alert("The restful service for class '" + infos.generateClass + "' doesn't exist, it will be created after you click the 'Save Changes' Button");
		}
	}
	
	window.ccc = callbackForEntity;
	
	var modelBt = $app.component("modelbt");
	modelBt.on('click', function(){
		FwBase.Wtf.Design.DesignSupport.editModel();
	});

	var behaviorBt = $app.component("behaviorbt");
	behaviorBt.on('click', function(){
		FwBase.Wtf.Design.DesignSupport.editBehavior();
	});
	
	
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var app = this.app;
		var idattr = app.component('idattr').value();
		var editableattr = app.component('editableattr').checked();
		var model = app.model('columnsModel');
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
		var rest = {generateClass: app.attr('generateClass')};
		updateComponent(idattr, md, controller, rest);
		app.close();
	});
	
	function updateComponent(id, md, controller, rest){
		FwBase.Wtf.Design.DesignSupport.syncAppFiles(id, md, controller, rest);
	}
});