Ext.define('MyApp.store.DeliveryHistoryDataStore', {
	extend : "Ext.data.TreeStore",
	autoload : false,
	proxy : {
		type : 'ajax',
		url : '/FlexibleOrders/deliveryHistory/byReportItemId/0',
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	root : {
		expanded : true,
		text : "My Root"
	}
});