Ext.define('MyApp.store.InvoiceNumberDataStore', {
	extend : 'Ext.data.Store',

	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent([ Ext.apply({
			fields : [ 'invoiceNumber' ],
			storeId : 'InvoiceNumberStore',
			pageSize : 1000,
			proxy : {
				actionMethods : {
					read : 'GET',
					update : 'PUT',
					destroy : 'DELETE',
					create : 'POST'
				},
				type : 'ajax',
				url : '/FlexibleOrders/reportitems/listInvoiceNumbers',
				headers : {
					accept : 'application/json'
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			}
		}, cfg) ]);
	}
});