Ext.define('MyApp.store.DeliveryMethodDataStore', {
			extend : 'Ext.data.Store',
			customurl : '/FlexibleOrders/deliverymethods/json',
			requires : ['MyApp.model.DeliveryMethodData'],
			autoLoad : true,

			constructor : function(cfg) {
				var me = this;
				cfg = cfg || {};
				me.callParent([Ext.apply({
							autoLoad : true,
							model : 'MyApp.model.DeliveryMethodData',
							storeId : 'DeliveryMethodDataStore',
							pageSize : 1000,
							proxy : {
								type : 'ajax',
								url : '/FlexibleOrders/customers/json',
								headers : {
									accept : 'application/json'
								},
								reader : {
									type : 'json',
									root : 'data'
								},
								api : {
									read : this.customurl,
									update : "/FlexibleOrders/deliverymethods/udpate",
									destroy : this.customurl,
									create : "/FlexibleOrders/deliverymethods/create"
								},
								writer : {
									type : 'json',
									allowSingle : true,
									root : 'data'
								}
							}
						}, cfg)]);
			}
		});