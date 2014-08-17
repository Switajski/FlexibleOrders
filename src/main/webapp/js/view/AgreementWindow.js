Ext.define('MyApp.view.AgreementWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Auftrag liefern',
	itemid : 'AgreementWindow',
	alias : 'widget.AgreementWindow',
	layout : 'fit',
	defaultInvoiceNumber : 0,
	width : 378,
	record : null,
	closeAction : 'destroy',
	bottomGrid : {
		xtype : 'PositionGrid',
		dock : 'bottom',
		id : 'CreateAgreementItemGrid',
		flex : 1,
		store : 'CreateAgreementItemDataStore',
		title : "Auftragspositionen",
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
					text : 'Menge'/*,
					editor : {
						xtype : 'numberfield',
						allowBlank : false,
						minValue : 1
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
							Ext.getStore('CreateConfirmationReportItemDataStore')
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
					fieldLabel : 'Auftragsnr.',
					name : 'orderAgreementNumber',
					id : 'orderAgreementNumber',
					allowBlank : false
				}]
	},
	addressForm : {
		xtype : 'fieldset',
		title : 'Rechnungsadresse',
		flex : 1,
		items : []
	},
	deliveryAddressForm : {
		xtype : 'fieldset',
		title : 'Lieferadresse',
		flex : 1,
		items : []
	},
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');
		Ext.applyIf(me, {
					layout : 'anchor',
					items : [{
								xtype : 'form',
								id : 'AgreementForm',
								bodyPadding : 10,
								items : [this.headerForm],

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
	}

});