Ext.define('MyApp.model.InvoiceItemData', {
			extend : 'MyApp.model.ShippingItemData',

			fields : [{
						name : 'trackNumber'
					},{
						name : 'packageNumber'
					}]
		});