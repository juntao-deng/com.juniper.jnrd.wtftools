wdefine(function(){
	$app.component('addipbt').on('click', function(){
		this.app.model('iprangemodel').page().add();
	});
	
	$app.component('addsecbt').on('click', function(){
		this.app.model('secmodel').page().add();
	});
});