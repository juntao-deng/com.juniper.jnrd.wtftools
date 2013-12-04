define(["base/base", "highcharts"], function(plot){	
	FwBase.Wtf.View.Controls.Chart_linear = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_linear.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_linear').html()),
			mockMetadata : function(){
				this.setDefault({width: '100%', height: '300', title: 'Historic and Estimated Worldwide Population Growth by Region', subtitle: 'Source: Wikipedia.org',
					categories: ['1750', '1800', '1850', '1900', '1950', '1999', '2050'], ytitle: 'Billions', valueSuffix: ' millions',
		            series: [{
		                name: 'Asia',
		                data: [502, 635, 809, 947, 1402, 3634, 5268]
		            }, {
		                name: 'Africa',
		                data: [106, 107, 111, 133, 221, 767, 1766]
		            }, {
		                name: 'Europe',
		                data: [163, 203, 276, 408, 547, 729, 628]
		            }, {
		                name: 'America',
		                data: [18, 31, 54, 156, 339, 818, 1201]
		            }, {
		                name: 'Oceania',
		                data: [2, 2, 2, 6, 13, 30, 46]
		            }]});
			},
			makeDefault : function() {
				this.setDefault({title: 'Line Chart', width: '100%', height: '200'});
			},
			postInit : function() {
				this.el.children("#chart_linear").highcharts({
		            title: {
		                text: this.metadata.title,
		                x: -20 //center
		            },
		            subtitle: {
		                text: this.metadata.subtitle,
		                x: -20
		            },
		            xAxis: {
		                categories: this.metadata.categories,
		            },
		            yAxis: {
		                title: {
		                    text: this.metadata.ytitle
		                },
		                plotLines: [{
		                    value: 0,
		                    width: 1,
		                    color: '#808080'
		                }]
		            },
		            tooltip: {
		                valueSuffix: this.metadata.valueSuffix
		            },
		            legend: {
		                layout: 'vertical',
		                align: 'right',
		                verticalAlign: 'middle',
		                borderWidth: 0
		            },
		            plotOptions: {
		                area: {
		                    stacking: 'normal',
		                    lineColor: '#666666',
		                    lineWidth: 1,
		                    marker: {
		                        lineWidth: 1,
		                        lineColor: '#666666'
		                    }
		                }
		            },
		            series : this.metadata.series
		        });
				

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