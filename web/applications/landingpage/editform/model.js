wdefine(function(){
	$app.metadata("wizard", {title:'Form Wizard' , activeItem: 'step1', items: [{id:'step1', text : 'Step 1, Input IP Range'}, {id:'step2', text : 'Input Security Store'}]});
	$app.metadata("iprange", {rows : 1, labelWidth : 80, elements : [
	                                                 			     {name: 'startip', label: 'Start Ip : ', nextrow : false, rows : 1, type:'input_ip'},
	                                                			     {name: 'endip', label: 'End Ip : ', nextrow : false, rows : 1, type:'input_ip'}
	                                                			]});
	
	$app.metadata("secrange", {rows : 1, labelWidth : 80, elements : [
	                                                 			     {name: 'user', label: 'User Id : ', nextrow : false, rows : 1, type:'input'},
	                                                			     {name: 'password', label: 'Password : ', nextrow : false, rows : 1, type:'input'}
	                                                			]});
	
	$app.model("iprangemodel", {url : "ipranges"});
	$app.metadata("ipthumbnails", {model: 'iprangemodel'});
	
	$app.model("secmodel", {url : "securitystores"});
	$app.metadata("secthumbnails", {model: 'secmodel'});
	
});