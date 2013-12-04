define(["base/base", "highcharts"], function(plot){	
	FwBase.Wtf.View.Controls.Chart_bar = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_bar.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_bar').html()),
			mockMetadata : function(){
				this.setDefault({mock : true});
			},
			makeDefault : function() {
				
			},
			postInit : function() {
//				this.grided = false;
//				
//				this.plot = $.plot(this.el.children("#chart_bar"), [[]], {
//					series: {
//						bars: {
//							show: true,
//							barWidth: 0.6,
//							horizontal: true
//						}
//					},
//					yaxis: {
//						ticks: 4,
//						min: 0
//					},
//					xaxis: {
//						ticks: 4
//					}
//				});
//				if(this.metadata.mock){
//					var d1 = [[3, 0], [8, 4], [5, 8], [2, 12]];
//					var d2 = [[3, 1], [8, 5], [5, 9], [3, 13]];
//					//var data = [[["January", 10], ["February", 8], ["March", 4], ["April", 13], ["May", 17], ["June", 9]]];
//					
//					var data = [d1, d2];
//					this.data(data);
//				}
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

	return FwBase.Wtf.View.Controls.Chart_bar;
});