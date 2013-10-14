define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Image = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	FwBase.Wtf.View.Controls.Image.CONST_STYLE_POLAROID = "polaroid";
	FwBase.Wtf.View.Controls.Image.CONST_STYLE_CIRCLE = "circle";
	FwBase.Wtf.View.Controls.Image.CONST_STYLE_ROUNDeD = "rounded";
	FwBase.Wtf.View.Controls.Image.CONST_IMG500x500 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAAGQCAYAAACAvzbMAAASVElEQVR4Xu3dQWvTaRfG4bgQceFScSsuFbeKH9+VK3Gpa8VlVyKIqEMKGTJ/0za9TxI7va+BlxemPWme6zz4I2nt3Dk7O/u98g8BAgQIELimwB0BuaaYTydAgACBcwEBcREIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SAAAECAuIOECBAgEAkICARmyECBAgQEBB3gAABAgQiAQGJ2AwRIECAgIC4AwQIECAQCQhIxGaIAAECBATEHSBAgACBSEBAIjZDBAgQICAg7gABAgQIRAICErEZIkCAAAEBcQcIECBAIBIQkIjNEAECBAgIiDtAgAABApGAgERshggQIEBAQNwBAgQIEIgEBCRiM0SA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";
	$.extend(FwBase.Wtf.View.Controls.Image.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_image').html()),
			postInit : function(){
				var oThis = this;
				this.el.children("#image").click(function(){
					oThis.trigger("click", {source : oThis});
				});
			},
			mockMetadata : function(){
				this.makeDefault();
			},
			
			makeDefault : function() {
				var width = null;
				var height = null;
				if(this.metadata.src == null || this.metadata.src == ""){
					width = 200;
					height = 200;
				}
				
				this.setDefault({style : "polaroid", src : FwBase.Wtf.View.Controls.Image.CONST_IMG500x500, width : width, height : height});
				this.metadata.cssclass = "img-" + this.metadata.style;
			}
		}
	);
	return FwBase.Wtf.View.Controls.Image;
});