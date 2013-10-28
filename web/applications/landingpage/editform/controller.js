wdefine(function(){
	$app.component('addipbt').on('click', function(){
		var page = this.app.model('iprangemodel').page().add({startip : '100.100.100.100'});
	});
	
	$app.component('addsecbt').on('click', function(){
		var page = this.app.model('secmodel').page().add({user : 'other'});
	});
	
	$app.component('wizard').on('finish', function(){
		var callback = function(){
			$app.parent.model('devicesmodel').page().fetch();
		};
		var options = {reset: true, success : callback};
		$app.parent.model("devicesmodel").page().action('discover', options);
		$app.close();
	});
});