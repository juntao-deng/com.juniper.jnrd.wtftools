define(["base/base", "highcharts"], function(){	
	FwBase.Wtf.View.Controls.Chart_pie = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_pie.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_pie').html()),
			mockMetadata : function(){
				this.setDefault({mock : true});
			},
			makeDefault : function() {
				
			},
			postInit : function() {
				this.grided = false;
				
//				this.plot = $.plot(this.el.children("#chart_pie"), [[]], {
//					series: {
//				        pie: {
//				            show: true,
//				            radius: 1,
//				            label: {
//				                show: true,
//				                radius: 3/4,
//				                formatter: labelFormatter,
//				                background: {
//				                    opacity: 0.5
//				                }
//				            }
//				        }
//				    },
//				    legend: {
//				        show: false
//				    }
//				});
//				if(this.metadata.mock){
//					var data = [];
//					var series = Math.floor(Math.random() * 6) + 3;
//
//					for (var i = 0; i < series; i++) {
//						data[i] = {
//							label: "Series" + (i + 1),
//							data: Math.floor(Math.random() * 100) + 1
//						}
//					}
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
	
	function labelFormatter(label, series) {
		return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
	}

	return FwBase.Wtf.View.Controls.Chart_pie;
});