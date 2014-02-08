Ext.define('MyApp.store.ShippingItemDataStore', {
			extend : 'MyApp.store.BestellpositionDataStore',
			alias : 'widget.ShippingItemDataStore',
			customurl : '/FlexibleOrders/reportitems/tobeshipped',
			custommodel : 'MyApp.model.ShippingItemData',
			customstoreid : 'ShippingItemDataStore',
			requires : ['MyApp.model.ShippingItemData'],
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				// var secondStore =
				// Ext.ComponentQuery.query('grid[itemid=secondGrid]')[0].getStore();
				var ocnr = this.getStore().data.items[0].data.orderNumber;
				MyApp.getApplication().getController('MyController').confirm(
						"ok", ocnr, record);

			}
		});