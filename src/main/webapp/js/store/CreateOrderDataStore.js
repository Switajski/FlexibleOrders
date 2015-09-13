Ext.define('MyApp.store.CreateOrderDataStore', {
	extend : 'Ext.data.Store',
	customurl : '/FlexibleOrders/transitions/order',
	custommodel : 'MyApp.model.ItemData',
	customstoreid : 'CreateOrderDataStore',
	requires : [ 'MyApp.model.ItemData' ],
	alias : 'widget.CreateOrderDataStore',
	groupField : 'orderNumber',
	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent([ Ext.apply({
			model : this.custommodel,
			storeId : this.customstoreid,
			proxy : {
				type : 'ajax',
				actionMethods : {
					read : 'GET',
					update : 'PUT',
					destroy : 'DELETE',
					create : 'POST'
				},
				api : {
					read : this.customurl,
					update : this.customurl,
					destroy : this.customurl,
					create : this.customurl
				},
				headers : {
					Accept : 'application/json'
				},
				reader : {
					type : 'json',
					successProperty : 'success',
					root : 'data',
					messageProperty : 'message'
				},
				writer : {
					type : 'json',
					root : 'data',
					writeAllFields : false
				}
			}
		}, cfg) ]);
	}
});