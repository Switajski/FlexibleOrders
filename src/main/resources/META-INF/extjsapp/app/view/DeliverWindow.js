Ext.define('MyApp.view.DeliverWindow', {
	extend : 'Ext.window.Window',
	title : 'Auftrag liefern',
	itemid : 'DeliverWindow',
	alias : 'widget.DeliverWindow',
	layout : 'fit',
	defaultInvoiceNumber : 0,
	width : 378,
	record : null,
	closeAction : 'destroy',
	bottomGrid : {
		xtype : 'DeliveryNotesItemGrid',
		dock : 'bottom',
		id : 'CreateDeliveryNotesItemGrid',
		flex : 1,
		store : 'CreateDeliveryNotesItemDataStore',
		title : "Lieferscheinpositionen",
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
					xtype : 'actioncolumn',
					width : 30,
					sortable : false,
					menuDisabled : true,
					items : [{
						icon : '/FlexibleOrders/images/delete.png',
						tooltip : 'Position l&ouml;schen',
						scope : this,
						handler : function(grid, rowIndex) {
							Ext.getStore('CreateDeliveryNotesItemDataStore')
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
					fieldLabel : 'Lieferscheinnr.',
					name : 'deliveryNotesNumber',
					id : 'deliveryNotesNumber',
					allowBlank : false
				}, {
					xtype : 'numberfield',
					fieldLabel : 'Versandkosten',
					name : 'shipment',
					allowBlank : true,
					allowDecimals : true,
					//allowExponential : false,
					minValue : 0
					
				}, {
					xtype : 'textfield',
					fieldLabel : 'Paketnr',
					name : 'packageNumber',
					allowBlank : true
				}, {
					xtype : 'textfield',
					fieldLabel : 'Sendungsnr',
					name : 'trackNumber',
					allowBlank : true
				}/*
					 * , { xtype : 'numberfield', fieldLabel : 'Menge', name :
					 * 'quantity', allowBlank : false, allowDecimals : false,
					 * allowExponential : false, minValue : 1, maxValue :
					 * this.record.data.quantity }
					 */]
	},
	addressForm : {
		xtype : 'fieldset',
		title : 'Lieferadresse',
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
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');
		Ext.applyIf(me, {
					layout : 'anchor',
					items : [{
								xtype : 'form',
								id : 'DeliverForm',
								bodyPadding : 10,
								items : [this.headerForm, this.addressForm],

								dockedItems : [{
											xtype : 'toolbar',
											dock : 'bottom',
											ui : 'footer',
											items : ['->', {
														iconCls : 'icon-save',
														itemId : 'save',
														text : 'Speichern',
														disabled : false,
														scope : this,
														handler : this.onSave
													}]
										}, this.bottomGrid]
							}]
				});

		me.callParent(arguments);

	},
	/**
	 * this method is to override by the using Component (usually Panel)
	 */
	updateRecord : function() {
		console.log('Override me!');
	},

	/**
	 * this method listens to the save button and is usually overridden by a
	 * panel. see {@Link MyController.deliver}
	 */
	onSave : function() {
		var active = this.activeRecord, form = this.getForm();

		if (!active) {
			return;
		}
		if (form.isValid()) {
			form.updateRecord(active);
			this.onReset();
		}
	},
	onComboboxChange : function() {
		if (record != null) {
			var combobox = Ext.ComponentQuery.query('invoicenumbercombobox')[0];
			this.record.data.invoiceNumber = combobox.getValue();
			console.log('new value' + combobox.getValue());
		}
	}

});