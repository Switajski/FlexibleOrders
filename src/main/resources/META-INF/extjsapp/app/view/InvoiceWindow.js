Ext.define('MyApp.view.InvoiceWindow', {
	extend : 'MyApp.view.TransitionWindow',
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
					xtype : 'textfield',
					fieldLabel : 'Rechnungsnr',
					name : 'invoiceNumber',
					id : 'invoiceNumber',
					allowBlank : false,
					anchor : '100%'
				},{
					xtype : 'datefield',
					format : 'd/m/Y',
					allowBlank : true,
					fieldLabel : 'Rechnungsdatum',
					name : 'created'
				},{
					xtype : 'textfield',
					fieldLabel : 'Abrechnung',
					name : 'billing',
					id : 'billing',
					allowBlank : false,
					anchor : '100%'
				},{
					xtype : 'textareafield',
					grow : true,
					fieldLabel : 'Zahlungsbd.',
					name : 'paymentConditions',
					anchor : '100%'
				}]
	},
	addressForm : null,
	deliveryAddressForm : null,
	bottomGrid : {
		xtype : 'PositionGrid',
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