Ext.define('MyApp.view.ConfirmWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Auftrag best&auml;tigen',
	width : 350,
	id : 'ConfirmWindow',
	bottomGrid : {
		xtype : 'PositionGrid',
		dock : 'bottom',
		id : Ext.getCmp('CreateConfirmationReportItemGrid'),
		flex : 1,
		store : 'CreateConfirmationReportItemDataStore',
		title : "Auftragsbest&auml;tigungspositionen",
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
					text : 'Menge'/*
									 * , editor : { xtype : 'numberfield',
									 * allowBlank : false, minValue : 1 }
									 */
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
							Ext
									.getStore('CreateConfirmationReportItemDataStore')
									.removeAt(rowIndex);
						}
					}]
				}]
	},
	headerForm : {
		xtype : 'fieldset',
		// title : 'Kunde',
		flex : 1,
		items : [{
					id : 'newOrderConfirmationNumber',
					xtype : 'textfield',
					anchor : '100%',
					fieldLabel : 'ABnr',
					name : 'orderConfirmationNumber'
				}, {
					xtype : 'displayfield',
					anchor : '100%',
					name : 'customerNumber',
					fieldLabel : 'Kundennr'
				}, {
					xtype : 'displayfield',
					anchor : '100%',
					name : 'lastName',
					fieldLabel : 'Nachname'
				}, {
					xtype : 'datefield',
					format : 'd/m/Y',
					anchor : '100%',
					allowBlank : true,
					fieldLabel : 'Liefertermin',
					minValue : Ext.Date.format(new Date(), 'd/m/Y'),
					minText : 'Datum liegt in der Vergangenheit',
					name : 'expectedDelivery'
				}, {
					xtype : 'deliverymethodcombobox',
					anchor : '100%',
					store : 'DeliveryMethodDataStore',
					id : 'confirmDMComboBox',
					fieldLabel : 'Lieferart'
				}, {
					xtype : 'fieldset',
					flex : 1,
					title : 'Details',
					items : [{
								xtype : 'textfield',
								anchor : '100%',
								name : 'contact1',
								fieldLabel : 'Anspr.1'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'contact2',
								fieldLabel : 'Anspr.2'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'contact3',
								fieldLabel : 'Anspr.3'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'contact4',
								fieldLabel : 'Anspr.4'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'mark',
								fieldLabel : 'Ihr Zeichen'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'paymentConditions',
								fieldLabel : 'Zahlungskond.'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'saleRepresentative',
								fieldLabel : 'Vertreter'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'vatIdNo',
								fieldLabel : 'GUT'
							}, {
								xtype : 'textfield',
								anchor : '100%',
								name : 'vendorNumber',
								fieldLabel : 'Lieferantennr.'
							}]
				}]
	},
	onSave : function(button, event, option) {
		MyApp.getApplication().getController('ConfirmController').confirm(
				button, event, option);
	}
});