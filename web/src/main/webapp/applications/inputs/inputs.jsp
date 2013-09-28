<%@ include file="../../templates/common/apptitle.jsp" %>
<%@ include file="../../templates/common/breadcrumb.jsp" %>
<div class="container-fluid">
	<div id="input1" wtftype="input">
	</div>
	<div id="input2" wtftype="input">
	</div>
	<div id="input3" wtftype="input">
	</div>
</div>
<script>
	var input1Meta = {label:'String with default'};
	var input2Meta = {label:'String with helpinline', placeholder:'input here', helpinline: "some help here"};
	var input3Meta = {label:'String with popover', placeholder:'input here', popover:{head:'pop head', content:'this is popover body', direction:'right'}};
	var input1 = FwBase.Wtf.View.createControl({id:'input1', type:"input", objMeta: input1Meta});
	var input2 = FwBase.Wtf.View.createControl({id:'input2', type:"input", objMeta: input2Meta});
	var input3 = FwBase.Wtf.View.createControl({id:'input3', type:"input", objMeta: input3Meta});
</script>