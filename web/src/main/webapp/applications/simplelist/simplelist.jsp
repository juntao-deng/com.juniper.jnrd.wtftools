<jsp:include page="../../templates/list/list.jsp"></jsp:include>
<script>
	var menuObj = {
		groups:[
			[{id:'add',name:'Add'}, {id:'edit',name:'Edit'}, {id:'del',name:'Delete'}],
			[{id:'save',name:'Save'}, {id:'disable',name:'Disable'}]
		]
	};

	var gridObj = {
		readOnly:true,
		headers:[
			{id:'', name:'', sortable:'false'}, {}, {}
		]
	};
	var menuCtrl = FwBase.Wtf.View.createControl({type:"menu", objMeta: menuObj});
	var tableCtrl = FwBase.Wtf.View.createControl({type:"table", objMeta: gridObj});
</script>