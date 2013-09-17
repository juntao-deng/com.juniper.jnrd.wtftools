<div class="sidebar-nav" id="sidebar-nav">
</div>

<script type="text/template" id="sidenav-item-template">
	<li><a href="{{= url}}">{{= name}}</a></li>
</script>

<script type="text/template" id="sidenav-group-template">
	<a href="#{{= id}}" class="nav-header" data-toggle="collapse"><i class="{{= icon}}"></i>{{= name}}</a>
    <ul id="{{= id}}" class="nav nav-list collapse {{= (collapse=='Y'? 'in' : '')}}">
	</ul>
</script>