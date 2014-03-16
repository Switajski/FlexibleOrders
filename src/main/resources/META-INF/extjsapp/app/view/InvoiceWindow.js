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
					allowBlank : false,
					anchor : '100%'
				}, {
					xtype : 'textareafield',
					grow : true,
					fieldLabel : 'Zahlungsbd.',
					name : 'paymentConditions',
					anchor : '100%'
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
					fieldLabel : 'Firma',
					allowBlank : false
				}, {
					xtype : 'textfield',
					anchor : '100%',
					name : 'name2',
					fieldLabel : 'Name',
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
	},
	bottomGrid : {
		xtype : 'InvoiceItemGrid',
		dock : 'bottom',
		id : 'CreateInvoiceItemGrid',
		flex : 1,
		store : 'CreateInvoiceItemDataStore',
		title : "Rechnungspositionen",
		features : null,
		selType : 'cellmodel',
		plugins : [Ext.create('Ext.grid.plugin.CellEditing', {
					clicksToEdit : 1
				})],
		features : [{
			ftype : 'grouping',
			groupHeaderTpl : '{columnName}: {name} ({rows.length} Position{[values.rows.length > 1 ? "en" : ""]}) {[values.rows[0].created]}',
			hideGroupedHeader : false,
			startCollapsed : false
				// id: 'orderNumber'
		}],
		columns : [{
					xtype : 'gridcolumn',
					dataIndex : 'product',
					text : 'Artikel',
					width : 85,
					displayField : 'name',
					valueField : 'productNumber'
				}, {
					xtype : 'gridcolumn',
					dataIndex : 'productName',
					width : 150,
					text : 'Artikel Name'
				}, {
					xtype : 'gridcolumn',
					dataIndex : 'quantityLeft',
					width : 50,
					text : 'Menge',
					editor : {
						xtype : 'numberfield',
						allowBlank : false,
						minValue : 1
					}
				}, {
					xtype : 'gridcolumn',
					dataIndex : 'priceNet',
					width : 50,
					text : 'Preis',
					renderer : Ext.util.Format.euMoney/*,
					editor : {
						xtype : 'numberfield',
						allowBlank : false,
						minValue : 0
					}*/
				}, {
					xtype : 'actioncolumn',
					width : 30,
					sortable : false,
					menuDisabled : true,
					items : [{
						icon : '/FlexibleOrders/images/delete.png',
						tooltip : 'Position l&ouml;schen',
						scope : this,
						handler : function(grid, rowIndex) {
							store = Ext.getStore('CreateInvoiceItemDataStore')
							store.removeAt(rowIndex, 1);
						}
					}]
				}]
	}
	
});