define(["flot"], function(plot){	
	FwBase.Wtf.View.Controls.Chart_linear = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_linear.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_linear').html()),
			mockMetadata : function(){
				this.setDefault({mock : true});
			},
			makeDefault : function() {
				
			},
			postInit : function() {
				this.grided = false;
				this.plot = $.plot(this.el.children("#chart_linear"), [[]], {
					series: {
						shadowSize: 0	// Drawing is faster without shadows
					},
					yaxis: {
						min: 0,
						max: 100
					},
					xaxis: {
						show: false
					}
				});
				if(this.metadata.mock){
					var mocker = new Mocker(this);
					mocker.run();
				}
			},
			data : function(data) {
				this.plot.setData(data);
				if(!this.grided){
					this.grided = true;
					this.plot.setupGrid();
				}
				this.plot.draw();
			}
		}
	);
	
	function Mocker(obj){
		this.obj = obj;
		this.data = [];
		this.totalPoints = 300;
		this.id = Math.random();
		FwBase.Wtf.Lang.Utils.globalScheduleItem(this);
	}

	Mocker.prototype.run = function() {
		var newDatas = this.getRandomData();
		this.obj.data([newDatas]);
		setTimeout("FwBase.Wtf.Lang.Utils.globalSchedule('" + this.id + "')", 100);
	};
	
	Mocker.prototype.getRandomData = function() {
		this.data;
		if (this.data.length > 0)
			this.data = this.data.slice(1);

		// Do a random walk
		while (this.data.length < this.totalPoints) {

			var prev = this.data.length > 0 ? this.data[this.data.length - 1] : 50,
				y = prev + Math.random() * 10 - 5;

			if (y < 0) {
				y = 0;
			} else if (y > 100) {
				y = 100;
			}

			this.data.push(y);
		}

		// Zip the generated y values with the x values

		var res = [];
		for (var i = 0; i < this.data.length; ++i) {
			res.push([i, this.data[i]]);
		}

		return res;
	};
	

	return FwBase.Wtf.View.Controls.Chart_linear;
});