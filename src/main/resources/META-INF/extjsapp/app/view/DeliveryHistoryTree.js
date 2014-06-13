var store = Ext.create('Ext.data.TreeStore', {
			root : {
				expanded : true,
				children : [{
							"text" : "detention",
							"leaf" : true
						}, {
							text : "homework",
							expanded : true,
							children : [{
										text : "book report",
										leaf : true
									}, {
										text : "algebra",
										leaf : true
									}]
						}, {
							text : "buy lottery tickets",
							leaf : true
						}]
			}
		});

Ext.define('MyApp.view.DeliveryHistoryTree', {
			requires : ['MyApp.store.DeliveryHistoryDataStore'],
			extend : 'Ext.tree.Panel',
			alias : 'widget.DeliveryHistoryTree',
			rootVisible : false,
			id : 'dhTree',
			initComponent : function() {
				console.error('initCoponent');
				var me = this;
				Ext.applyIf(me, {
							store : 'DeliveryHistoryDataStore',
							layout : 'anchor'

						});

				this.callSuper(arguments);

				/*
				 * Ext.Ajax.request({ url :
				 * '/FlexibleOrders/deliveryHistory/test', method : 'GET',
				 * success : function(response) { var text =
				 * response.responseText; shippedAmount =
				 * Ext.JSON.decode(text).data;
				 * Ext.getStore('DeliveryHistoryDataStore').setRootNode(shippedAmount); }
				 * });
				 */
			}
		});