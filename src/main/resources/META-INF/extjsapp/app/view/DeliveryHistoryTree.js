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

			}
		});