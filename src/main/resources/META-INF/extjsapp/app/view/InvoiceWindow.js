/**
 * 
 */
 Ext.define('MyApp.view.InvoiceWindow', {
	extend : 'MyApp.view.DeliverWindow',
	title : 'Rechnung erstellen',
	itemid : 'InvoiceWindow',
	alias : 'widget.InvoiceWindow',
	headerForm : {
		xtype : 'fieldset',
		// title : 'Kunde',
		flex : 1,
		items : [{
					xtype : 'displayfield',
					anchor : '100%',
					name : 'customerNumber',
					fieldLabel : 'Kundennr'
				}, {
					xtype : 'displayfield',
					anchor : '100%',
					name : 'lastName',
					fieldLabel : 'Name'
				}, {
					// itemid : 'invoiceNumber',
					// xtype : 'invoicenumbercombobox',
					xtype : 'textfield',
					fieldLabel : 'Rechnungsnr',
					name : 'invoiceNumber',
					id : 'invoiceNumber',
					allowBlank : false
				}, {
					xtype : 'textfield',
					fieldLabel : 'Zahlungsbed.',
					name : 'paymentConditions'				
				}]
	},
	addressForm : {
		xtype : 'fieldset',
		title : 'Rechnungsadresse',
		flex : 1,
		items : [{
					xtype : 'textfield',
					anchor : '100%',
					name : 'name1',
					fieldLabel : 'Name 1',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'name2',
					fieldLabel : 'Name 2',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'street',
					fieldLabel : 'Strasse',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'postalCode',
					fieldLabel : 'PLZ',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'city',
					fieldLabel : 'Stadt',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'country',
					fieldLabel : 'Land',
					allowBlank : false
				}]
	}
	
});