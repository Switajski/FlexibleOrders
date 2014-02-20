Ext.define('MyApp.view.ErstelleBestellungWindow', {
	extend : 'Ext.window.Window',
	id : 'ErstelleBestellungWindow',
	alias : 'widget.ErstelleBestellungWindow',
	layout : 'fit',
	closeAction : 'hide',
	title : 'Erstelle Bestellung',
	width : 700,

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			layout : 'anchor',

			items : [{
				xtype : 'form',
				itemid : 'ErstelleBestellungForm',
				id : 'ErstelleBestellungForm',
				// dock : 'top',
				bodyPadding : 10,
				items : [{

					xtype : 'fieldset',
					title : 'Bestellung',
					flex : 1,
					items : [{
						/*
						 * xtype : 'numberfield', // anchor: '100%', fieldLabel :
						 * 'Bestellnr', allowBlank : false, name :
						 * 'orderNumber', valueField : 'orderNumber'
						 */
						itemid : 'orderNumber',
						xtype : 'ordernumbercombobox',
						fieldLabel : 'Bestellnr',
						listeners : {
							// change : this.onOrderNumberChange,
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									me.onOrderNumberChange(field, e);
									Ext.ComponentQuery
											.query('#ErstelleBestellungForm button[itemid=add]')[0]
											.focus();
								}
							}
						}
					}]
				}, {
					xtype : 'BestellpositionGrid',
					id : 'BestellpositionGrid',
					layout : {
						type : 'anchor'
					},
					store : 'CreateOrderDataStore',
					listeners : {
						create : function(form, data) {
							store.insert(0, data);
						},
						selectionchange : this.onSelectionchange
					},
					columns : [{
						xtype : 'gridcolumn',
						dataIndex : 'product',
						text : 'Artikel',
						width : 250,
						// name: 'productNumber',
						editor : {
							id : 'ArtikelComboBox',
							xtype : 'combobox',
							displayField : 'name',
							valueField : 'productNumber',
							enableRegEx : true,
							allowBlank : false,
							forceSelection : true,
							queryMode : 'local',
							store : 'ArtikelDataStore',
							tpl : Ext.create(
											'Ext.XTemplate',
											'<tpl for=".">',
											'<div class="x-boundlist-item">{productNumber} - {name}</div>',
											'</tpl>'),
							displayTpl : Ext.create('Ext.XTemplate',
									'<tpl for=".">',
									'{productNumber} - {name}', '</tpl>')
						}
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'orderNumber',
						width : 100,
						text : 'Bestellung',
						filter : {
							type : 'string'
							// , disabled: true
						}
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'quantity',
						width : 75,
						text : 'Menge',
						editor : {
							xtype : 'numberfield',
							allowBlank : false,
							minValue : 1
						}
					}, {
						xtype : 'numbercolumn',
						dataIndex : 'priceNet',
						width : 100,
						text : 'Preis Netto',
						renderer : Ext.util.Format.euMoney,
						editor : {
							xtype : 'numberfield',
							allowBlank : true
						}
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'expectedDelivery',
						text : 'Geplante Auslieferung',
						width : 130,
						format : 'd/m/Y',
						editor : {
							xtype : 'datefield',
							format : 'd/m/Y',
							allowBlank : true,
							minValue : Ext.Date.format(new Date(), 'd/m/Y'),
							minText : 'Datum liegt in der Vergangenheit'
						}
					}]
				}],

				buttons : [{

							text : 'Speichern',
							formBind : true, // only enabled once the form is
							// valid
							disabled : true,
							id : 'SubmitBestellungButton',
							handler : this.onSave
						}]
			}]
		});

		me.callParent(arguments);
	},
	onOrderNumberChange : function(field, event) {

		if (event != null) {
			var store = Ext.data.StoreManager.lookup('CreateOrderDataStore');
			store.filter("orderNumber", field.rawValue);
			console.log(field.rawValue);
			console.log(store);

			// store.getProxy().extraParams = {orderNumber: data};
			// store.read();
		}

	},
	onSelect : function(form, data) {
		alert('onSelect');
	},
	onSelectionchange : function(view, selections, options) {
		// TODO: löschen und Hinzufügen Button der Bps de/aktivieren
		if (selections[0] != null) {
			var orderNumber = this.up('numberfield[orderNumber]');
			var deleteButton = this.down('button[itemid=delete]');
			var addButton = this.down('button[itemid=add]');
			if (selections[0].data.status == 'ORDERED') {
				addButton.setDisabled(false);
				deleteButton.setDisabled(false);
			} else {
				deleteButton.setDisabled(true);
			}
		}
	},
	onSave : function(btn) {
		form = Ext.getCmp('ErstelleBestellungForm').getForm();
		orderNumber = form.getValues().orderNumber;
		customerId = form.getValues().customer;
		store = Ext.data.StoreMgr.lookup('CreateOrderDataStore');
		grid = Ext.getCmp('BestellpositionGrid');
		if (orderNumber == 0 || orderNumber == "") {
			Ext.MessageBox.show({
						title : 'Bestellnummer leer',
						msg : 'Bitte eine Bestellnummer eingeben',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else if (customerId == 0 || customerId == "") {
			Ext.MessageBox.show({
						title : 'Kundenfeld leer',
						msg : 'Bitte eine Kunden auswaehlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
		} else {
			store.sync();
		}
	}

});

/*
 * Ext.define('MyApp.view.ErstelleBestellungWindow', { extend :
 * 'Ext.form.field.ComboBox', alias : 'widget.customercombobox', xtype :
 * 'combobox', // anchor:'100%', name : 'customer', itemid : 'customer',
 * fieldLabel : 'Kunde', displayField : 'shortName', valueField : 'id', //
 * allowBlank: false, enableRegEx : true, forceSelection : true, queryMode :
 * 'local', store : 'KundeDataStore', tpl : Ext.create('Ext.XTemplate', '<tpl
 * for=".">', '<div class="x-boundlist-item">{id} - {shortName}</div>', '</tpl>'),
 * displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">', '{id} -
 * {shortName}', '</tpl>')
 * 
 * });
 */