Ext.define('MyApp.store.ShippingItemDataStore', {
	extend : 'MyApp.store.ItemDataStore',
	alias : 'widget.ShippingItemDataStore',
	customurl : '/FlexibleOrders/reportitems/listAllToBeProcessed',
	customstoreid : 'ShippingItemDataStore',
	groupField : 'documentNumber',
	onActionClick : function(view, a, b, column, event, record, f) {
		var ocnr = this.getStore().data.items[0].data.orderNumber;
		MyApp.getApplication().getController('MyController').confirm("ok",
				ocnr, record);

	}
});