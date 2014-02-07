define(function(){
	return {
		navigateSelection: function(taskId, taskType, params) {
			params = params | {};
			if(taskType == "ext3"){
				navigateExt3Task(taskId, params);
			}
			else if(taskType == "ext4"){
				navigateExt4Task(taskId, params);
			}
			else{
				navigateWtfTask(taskId, params);
			}
		},
		navigateExt3Task: function(taskId, params){
    		Jx.shell.legacyFrame.openTask( taskId, params  );
    		workspace.activateLegacyCard();
		},
		navigateExt4Task: function(taskId, params){
    		params.taskId = taskId;
    		Jx.shell.createTask(taskClass, params);
    	
	    	/*Fire event 'taskcreated' if App needs to handle different cases once a Shell task is created
	    	 * Ex: Task might want to navigateTo a legacy task based on some actions after the Shell task
	    	 * is created.
	    	 */
    		
    		var currentTask = Junos.Spaces.foundation.task.Task.currentTask;
    		if(currentTask) {
    			currentTask.fireEvent('taskcreated', currentTask);
    		}
		},
		navigateWtfTask: function(taskId, params){
			
		}
	};
});