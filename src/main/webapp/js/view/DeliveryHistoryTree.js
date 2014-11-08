Ext.define('MyApp.view.DeliveryHistoryTree', {
			requires : ['MyApp.store.DeliveryHistoryDataStore'],
			extend : 'Ext.tree.Panel',
			alias : 'widget.DeliveryHistoryTree',
			rootVisible : false,
			id : 'dhTree',
			initComponent : function() {
				var me = this;
				Ext.applyIf(me, {
							store : 'DeliveryHistoryDataStore',
							layout : 'anchor',
							listeners : {
								itemdblclick : this.onitemdblclick
							}

						});

				this.callSuper(arguments);

			},
			onitemdblclick : function(grid, record, a, index) {
				var win = window.open('/FlexibleOrders/reports/' + record.data.id + '.pdf', '_blank');
				win.focus();
			}
		});