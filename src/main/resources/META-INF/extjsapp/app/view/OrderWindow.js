Ext.define('MyApp.view.OrderWindow', {
	extend : 'MyApp.view.DeliverWindow',
	title : 'Bestellung aufgeben',
	width : 700,
	id : 'OrderWindow',
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
					fieldLabel : 'Nachname'
				}, {
					itemid : 'newOrderNumber',
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
	},
	addressForm : null,
	bottomGrid : {
		xtype : 'BestellpositionGrid',
		getOrderNumber : function() {
			return Ext.ComponentQuery.query('combobox[itemid=newOrderNumber]')[0].rawValue;
		},
		plugins : [Ext.create('Ext.grid.plugin.CellEditing', {
					clicksToEdit : 1
				})],
		dock : 'bottom',
		id : 'CreateOrderGrid',
		flex : 1,
		store : 'CreateOrderDataStore',
		// store : 'InvoiceItemDataStore',
		title : "Bestellpositionen",
		features : null,
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
				listeners : {
					'blur' : function(xObject, state, eOpts) {
						var request = Ext.Ajax.request({
									url : '/FlexibleOrders/products/retrieveRecommededPriceNet',
									params : {
										productNumber : xObject.value
									},
									method:'GET',
									success : function(response) {
										var json = Ext.decode(response.responseText);
										store = Ext.data.StoreMgr.lookup('CreateOrderDataStore');
										record = store.getAt(0);
										record.set('priceNet', json.data.value);
									}
								});
					}
				},
				tpl : Ext.create(
								'Ext.XTemplate',
								'<tpl for=".">',
								'<div class="x-boundlist-item">{productNumber} - {name}</div>',
								'</tpl>'),
				displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
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
	},
	onSave : function(button, event, option) {
		MyApp.getApplication().getController('MyController').order(button,
				event, option);
	}
});