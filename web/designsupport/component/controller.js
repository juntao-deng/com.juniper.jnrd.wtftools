wdefine(function(){
	var okbt = $app.component("okbt");
	okbt.on('click', function(){
		var compInput = $app.component('componentinput');
		if(compInput.value() == null || $.trim(compInput.value()) == ""){
			alert("Please select a component.");
			return;
		}
		var compId = $app.component('componentid');
		if(compId.value() == null || $.trim(compId.value()) == ""){
			alert("Please input an id for the component.");
			return;
		}
		insertComponent(compId.value(), compInput.value());
		$app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
		FwBase.Wtf.Design.DesignSupport.syncHtml();
	});
	
	function insertComponent(id, type){
		var str = null;
		if(type == 'tab' || type == 'card'){
			str = '<div id="' + id + '" wtftype="' + type + '">' +
					'<div id="item1"><div wtftype="container" style="min-height:200px"></div></div>' + 
					'<div id="item2"><div wtftype="container" style="min-height:200px"></div></div>' + 	
			      '</div>';
		}
		else if(type == 'wizard'){
			str = '<div id="' + id + '" wtftype="' + type + '">' +
			'<div id="step1"><div wtftype="container" style="min-height:200px"></div></div>' + 
			'<div id="step2"><div wtftype="container" style="min-height:200px"></div></div>' + 
			'<div id="step3"><div wtftype="container" style="min-height:200px"></div></div>' + 	
	      '</div>';
		}
		else
			str = '<div id="' + id + '" wtftype="' + type + '"></div>';
		FwBase.Wtf.Design.DesignSupport.currParent.html(str);
	}
});