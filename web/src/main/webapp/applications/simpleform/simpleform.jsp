<jsp:include page="../../templates/form/form.jsp"></jsp:include>
<script>
	var menuObj = {
		groups:[
			[{id:'add',name:'Add'}, {id:'edit',name:'Edit'}, {id:'del',name:'Delete'}]
		]
	};

	var formObj = {
		readOnly:true,
		headers:[
			{id:'', name:'', sortable:'false'}, {}, {}
		]
	};
	var menuCtrl = FwBase.Wtf.View.createControl({type:"menu", objMeta: menuObj});
	var tableCtrl = FwBase.Wtf.View.createControl({type:"form", objMeta: formObj});
</script>