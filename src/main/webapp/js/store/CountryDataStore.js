Ext.define('MyApp.store.CountryDataStore', {
	extend : 'Ext.data.Store',
	customurl : '/FlexibleOrders/country',
	requires : [ 'MyApp.model.CountryData' ],
	autoLoad : true,

	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent([ Ext.apply({
			autoLoad : true,
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
					read : this.customurl
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