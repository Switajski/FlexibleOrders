Ext.define('MyApp.store.CountryDataStore', {
	extend : 'Ext.data.Store',
	requires : [ 'MyApp.model.CountryData' ],

	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent([ Ext.apply({
			model : 'MyApp.model.CountryData',
			storeId : 'CountryDataStore',
			pageSize : 1000,
			proxy : {
				type : 'ajax',
				url : '/FlexibleOrders/country',
				headers : {
					accept : 'application/json'
				},
				reader : {
					type : 'json',
					root : 'data'
				},
				api : {
					read : '/FlexibleOrders/country'
				},
				writer : {
					type : 'json',
					allowSingle : true,
					root : 'data'
				}
			}
		}, cfg) ]);
	}
});