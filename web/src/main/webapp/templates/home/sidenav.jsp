<script type="text/template" id="sidenav-item-template">
	<li><a href="{{= url}}" class="app_item_click" target="{{= target}}">{{= name}}</a></li>
</script>

<script type="text/template" id="sidenav-group-template">
	{{if(url == null || url == ""){}}
		<a href="#{{= id}}" class="nav-header" data-toggle="collapse"><i class="{{= icon}}"></i>{{= name}}</a>
    	<ul id="{{= id}}" class="nav nav-list collapse {{= (collapse=='Y'? 'in' : '')}}">
		</ul>
	{{}else{}}
		 <a href="{{= url}}" class="nav-header" ><i class="{{= icon}}"></i>{{= name}}</a>
	{{}}}
</script>