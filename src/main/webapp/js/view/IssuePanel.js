Ext.define('MyApp.view.IssuePanel', {
	extend : 'Ext.panel.Panel',
	frame : false,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	bodyPadding: 7,
	title : 'Gutschriften',
	requires : ['MyApp.store.ItemDataStore'],
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'top',
		items : [{
			id : 'ShowSums',
			icon : '/FlexibleOrders/images/update.png',
			text : 'Offene Betr&auml;ge anzeigen',
			scope : this
		}]

	}],

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [{
				xtype : 'fieldcontainer',
				items : [{
							xtype : 'customercombobox',
							id : 'mainCustomerComboBox',
							fieldLabel: 'Kunde'
						},{
							xtype : 'CreditNoteItemGrid',
							store : 'CreditNoteItemDataStore'
						}

				]
			}]

		});
		me.callParent(arguments);
	}
});