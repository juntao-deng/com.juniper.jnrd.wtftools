wdefine(function(){
	$app.component("componentinput").on("valuechange", function(options){
		var value = options.value;
		var id = tryGetId(value);
		this.ctx.component("componentid").value(id);
	});
	function tryGetId(value){
		var beginIndex = 1;
		var beginId = value + beginIndex;
		var app = DesignSupport.getDesignApp();
		var comp = app.component(beginId);
		while(comp != null){
			beginId = value + (++ beginIndex);
			comp = app.component(beginId);
		}
		return beginId;
	}
	$app.component("okbt").on('click', function(){
		var app = this.ctx;
		var compInput = app.component('componentinput');
		if(compInput.value() == null || $.trim(compInput.value()) == ""){
			alert("Please select a component.");
			return;
		}
		var compId = app.component('componentid');
		if(compId.value() == null || $.trim(compId.value()) == ""){
			alert("Please input an id for the component.");
			return;
		}
		insertComponent(compId.value(), compInput.value());
		app.close();
		FwBase.Wtf.Application.repaint(FwBase.Wtf.Design.DesignSupport.designable);
		FwBase.Wtf.Design.DesignSupport.syncHtml();
	});
	
	function insertComponent(id, type){
		var str = null;
		if(type == 'tab' || type == 'card'){
			str = '<div id="' + id + '" wtftype="' + type + '">' +
					'<div id="item1"><div wtftype="container" class="design_minheight"></div></div>' + 
					'<div id="item2"><div wtftype="container" class="design_minheight"></div></div>' + 	
			      '</div>';
		}
		else if(type == 'wizard'){
			str = '<div id="' + id + '" wtftype="' + type + '">' +
			'<div id="step1"><div wtftype="container" class="design_minheight"></div></div>' + 
			'<div id="step2"><div wtftype="container" class="design_minheight"></div></div>' + 
			'<div id="step3"><div wtftype="container" class="design_minheight"></div></div>' + 	
	      '</div>';
		}
		else
			str = '<div id="' + id + '" wtftype="' + type + '"></div>';
		var menu = FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu');
		menu.remove();
		var wtfpos = FwBase.Wtf.Design.DesignSupport.currParent.attr('wtfpos');
		if(wtfpos != null){
			var child = $(str);
			child.attr('for', wtfpos);
			FwBase.Wtf.Design.DesignSupport.currParent.append(child);
		}
		else
			FwBase.Wtf.Design.DesignSupport.currParent.append(str);
	}
});