Ext.define('MyApp.view.DeliveryHistoryPanel', {
			extend : 'Ext.window.Window',

			title : 'Lieferhistorie',
			itemid : 'DeliveryHistoryPanel',
			alias : 'widget.DeliveryHistoryPanel',
			layout : 'fit',
			defaultInvoiceNumber : 0,
			width : 378,
			record : null,
			closeAction : 'destroy',
			requires : ['MyApp.store.ArtikelDataStore',
					'MyApp.model.ArtikelData', 'MyApp.view.DeliveryHistoryTree'],

			initComponent : function() {
				var me = this;
				Ext.applyIf(me, {
							layout : 'anchor',
							items : [{
										xtype : 'DeliveryHistoryTree'
									}]
							
						});

				me.callParent(arguments);

			}

		});