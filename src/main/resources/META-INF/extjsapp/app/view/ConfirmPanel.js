/*
 * File: app/view/MainPanel.js
 * 
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 * 
 * This file requires use of the Ext JS 4.2.x library, under independent
 * license. License of Sencha Architect does not include license for Ext JS
 * 4.2.x. For more details see http://www.sencha.com/license or contact
 * license@sencha.com.
 * 
 * This file will be auto-generated each and everytime you save your project.
 * 
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.view.ConfirmPanel', {
	extend : 'Ext.panel.Panel',
	frame : false,
	//height : 850,
	//width : 1000,
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Bestellpositionen bestaetigen',
	requires : ['MyApp.store.ItemDataStore'],
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'top',
		items : [{
			xtype : 'button',
			text : 'Berichte',
			icon : '/FlexibleOrders/images/pdf_button.png',
			menu : {
				xtype : 'menu',
				minWidth : 120,
				items : [{

							id : 'showOrderedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht best&auml;tigte Bestellpositionen anzeigen',
							scope : this
						}, {

							id : 'showConfirmedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht gelieferte Auftragspositionen anzeigen',
							scope : this
						}, {

							id : 'showShippedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'nicht bezahlte Lieferungpositionen anzeigen',
							scope : this
						}, {

							id : 'showCompletedButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'bezahlte/archivierte Positionen anzeigen',
							scope : this
						}, {
							xtype : 'menuseparator'
						}]
			}
		}, {
			id : 'ErstelleBestellungButton',
			icon : '/FlexibleOrders/images/add.png',
			text : 'erstelle Bestellung',
			scope : this
		}/*, {
			id : 'AbBestellungButton',
			text : 'Auftrag best&auml;tigen',
			icon : '/FlexibleOrders/images/new_ab.png',
			tooltip : 'Auftrag bestaetigen. Damit kommen die Bestellpositionen unter offene Posten',
			scope : this
		}, {
			id : 'RechnungBestellungButton',
			text : 'Rechnung erstellen',
			icon : '/FlexibleOrders/images/new_rechnung.png',
			tooltip : 'Rechnung in Pdf erstellen und aus den offenen Posten herausnehmen.',
			scope : this
		}, {
			id : 'BezahltBestellungButton',
			text : 'Als Bezahlt markieren',
			icon : '/FlexibleOrders/images/bezahlt.png',
			tooltip : 'Bestellung als bezahlt markieren',
			scope : this
		}*/]

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
						}, {
							xtype : 'OrderItemGrid',
							store : 'ItemDataStore',
							customurl : '/FlexibleOrders/customers/json/getItems'
							/*extraParams : {
								customer : 1,
								itemType : 'ordered'
							},*/
							
						}, {
							xtype : 'ShippingItemGrid',
							store : 'ShippingItemDataStore'
						}

				]
			}]

		});
		me.callParent(arguments);
	}
});