Ext.define('MyApp.view.MainPanel', {
	extend : 'Ext.panel.Panel',
	frame : false,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	bodyPadding : 7,
	title : 'Stand der Bestellungen',
	requires : ['MyApp.store.ItemDataStore'],
	dockedItems : [{
				xtype : 'toolbar',
				dock : 'top',
				items : [{
							id : 'ErstelleBestellungButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'erstelle Bestellung',
							scope : this
						}, {
							id : 'CreateCustomerButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'erstelle Kunden',
							scope : this
						}, {
							id : 'UpdateCustomerButton',
							icon : '/FlexibleOrders/images/update.png',
							text : 'Kunden bearbeiten',
							scope : this
						}, {
							id : 'ShowSums',
							icon : '/FlexibleOrders/images/update.png',
							text : 'Offene Betr&auml;ge anzeigen',
							scope : this
						}]

			}],

	initComponent : function() {
		var me = this;
		border = 10;

		Ext.applyIf(me, {
			items : [{
				xtype : 'fieldcontainer',
				items : [{
							xtype : 'customercombobox',
							id : 'mainCustomerComboBox',
							fieldLabel : 'Kunde'
						}, {
							xtype : 'OrderItemGrid',
							store : 'ItemDataStore',
							customurl : '/FlexibleOrders/customers/json/getItems'
						}, {
							xtype : 'splitter'
						}, {
							xtype : 'ShippingItemGrid',
							store : 'ShippingItemDataStore'
						},{
							xtype : 'splitter'
						}, {
							xtype : 'DeliveryNotesItemGrid',
							store : 'DeliveryNotesItemDataStore'
						},{
							xtype : 'splitter'
						}, {
							xtype : 'InvoiceItemGrid',
							store : 'InvoiceItemDataStore'
						}]
			}]

		});
		me.callParent(arguments);
	}
});