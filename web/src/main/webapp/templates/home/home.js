
$(function(){
  var NavItem = Backbone.Model.extend({
	  
  });
  
  var NavItemList = Backbone.Collection.extend({
	  
  });

  var items = new NavItemList;
  items.url = 'rest/home/homeinfos';
  
  //top level of view
  var AppView = Backbone.View.extend({

//    // Our template for the line of statistics at the bottom of the app.
//    statsTemplate: _.template($('#stats-template').html()),
//
//    // Delegated events for creating new items, and clearing completed ones.
//    events: {
//      "keypress #new-todo":  "createOnEnter",
//      "click #clear-completed": "clearCompleted",
//      "click #toggle-all": "toggleAllComplete"
//    },

    // At initialization we bind to the relevant events on the `Todos`
    // collection, when items are added or changed. Kick things off by
    // loading any preexisting todos that might be saved in *localStorage*.
    initialize: function() {
//      this.input = this.$("#new-todo");
//      this.allCheckbox = this.$("#toggle-all")[0];
      this.listenTo(items, 'add', this.addOne);
      this.listenTo(items, 'reset', this.addAll);
      this.listenTo(items, 'all', this.render);
//      this.footer = this.$('footer');
//      this.main = $('#main');
      items.fetch();
    },

    render: function() {
    	if(items.length == 0)
    		return;
//    	for(var i = 0; i < items.length; i ++){
//    		var view = new ItemView({model: items.get(i)});
//    		this.$el.append(view.render().el);
//    	}
    },

    addOne: function(item) {
    	var view = new ItemsView({model: item});
    	view.render();
    },

    addAll: function() {
//      Todos.each(this.addOne, this);
    }

//    // If you hit return in the main input field, create new **Todo** model,
//    // persisting it to *localStorage*.
//    createOnEnter: function(e) {
//      if (e.keyCode != 13) return;
//      if (!this.input.val()) return;
//
//      Todos.create({title: this.input.val()});
//      this.input.val('');
//    },
//
//    // Clear all done todo items, destroying their models.
//    clearCompleted: function() {
//      _.invoke(Todos.done(), 'destroy');
//      return false;
//    },
//
//    toggleAllComplete: function () {
//      var done = this.allCheckbox.checked;
//      Todos.each(function (todo) { todo.save({'done': done}); });
//    }

  });
  
  var ItemsView = Backbone.View.extend({
	  template: _.template($('#sidenav-item-template').html()),
	  grouptemplate: _.template($('#sidenav-group-template').html()), 

//	  // The DOM events specific to an item.
//	  events: {
//		  "click .toggle"   : "toggleDone",
//		  "dblclick .view"  : "edit",
//		  "click a.destroy" : "clear",
//		  "keypress .edit"  : "updateOnEnter",
//		  "blur .edit"      : "close"
//	  },

	  // The TodoView listens for changes to its model, re-rendering. Since there's
	  // a one-to-one correspondence between a **Todo** and a **TodoView** in this
	  // app, we set a direct reference on the model for convenience.
	  initialize: function() {
		  this.listenTo(this.model, 'change', this.render);
//		  this.listenTo(this.model, 'destroy', this.remove);
	  },

	  // Re-render the titles of the todo item.
	  render: function() {
		  var navList = this.model.get("navList");
		  for(var i = 0; i < navList.length; i ++){
			  var item = navList[i];
			  var pItem = item['id'] == null ? ($('#sidebar-nav > #' + item['groupId'])) : $('#sidebar-nav');
			  if(item['id'] != null)
				  pItem.append(this.grouptemplate(item));
			  else
				  pItem.append(this.template(item));
	  	  }
		  $('.app_item_click').click(function() {
			  Jnrd.Wtf.View.navigateTo(this.href, null, this.target != 'Y');
			  return false;
		  });
		  Jnrd.Wtf.View.navigateTo("apps/dashboard");
//		  this.$el.toggleClass('done', this.model.get('done'));
//		  this.input = this.$('.edit');
		  return this;
	  }

//	  toggleDone: function() {
//		  this.model.toggle();
//	  },

//	  edit: function() {
//		  this.$el.addClass("editing");
//		  this.input.focus();
//	  },
//
//	  // Close the `"editing"` mode, saving changes to the todo.
//	  close: function() {
//		  var value = this.input.val();
//		  if (!value) {
//			  this.clear();
//		  } 
//		  else {
//			  this.model.save({title: value});
//			  this.$el.removeClass("editing");
//		  }
//	  },
//
//	  // If you hit `enter`, we're through editing the item.
//	  updateOnEnter: function(e) {
//		  if (e.keyCode == 13) this.close();
//	  },
//
//	  // Remove the item, destroy the model.
//	  clear: function() {
//		  this.model.destroy();
//	  }

  });
  var App = new AppView;
});
