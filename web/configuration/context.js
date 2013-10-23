function testClientMode() {
	if(window.location.href.indexOf('file://') == 0)
		return true;
	return false;
}
window.clientMode = testClientMode();
window.mainCtx = "#CTX#";
window.frameCtx = "#FRMCTX#";

if(window.clientMode){
	window.frameworkPath = "#FRAME_PATH#";
	window.contextMappings = {
		'#CTX#' : '#CTXPATH#applications/'
	};
	window.contextMappings[window.frameCtx] = '#FRAME_PATH#applications/';
}
else{
	window.frameworkPath = "/" + window.frameCtx + "/";
	window.contextMappings = {
		'#CTX#' : '#CTX#applications/'
	};
	window.contextMappings[window.frameCtx] = '/' + window.frameCtx + "/applications/";
}


var req = document.createElement("script");
req.src = window.frameworkPath + "ext-lib/requirejs/require.js";
document.body.appendChild(req);


req.onreadystatechange = function(){
	if(req.readyState == "loaded" || req.readyState == "complete"){
		sys_loadCommon();
	} 
};
req.onload = function(){
	sys_loadCommon();
};
req.onerror = function(){
	alert('Not Found (404): require');
};

function sys_loadCommon() {
	var common = document.createElement("script");
	common.src = window.frameworkPath + "library/common/common.js";
	document.body.appendChild(common);
}