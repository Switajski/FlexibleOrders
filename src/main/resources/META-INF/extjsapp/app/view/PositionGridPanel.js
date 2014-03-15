var filters = {
	ftype : 'filters',
	// encode and local configuration options defined previously for easier
	// reuse
	encode : true, // json encode the filter query
	local : false, // defaults to false (remote filtering)

	// Filters are most naturally placed in the column definition, but can also
	// be
	// added here.
	filters : [{
				type : 'string',
				dataIndex : 'visible'
			}]
};

var grouping = {
	ftype : 'grouping',
	groupHeaderTpl : 'Dok.nr.: {name} ({rows.length} Position{[values.rows.length > 1 ? "en" : ""]}) {[values.rows[0].created]}',
	hideGroupedHeader : false,
	startCollapsed : false
};

Ext.define('MyApp.view.PositionGridPanel', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.PositionGrid',
	title : "Abstrakte Positionen - (abstract items)",
	customIsFirstPanel : true,
	customicon : '/FlexibleOrders/images/new_rechnung.png',
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');

		Ext.applyIf(me, {
			/*
			 * plugins : [this.editing], /* tbar : [{ itemid: 'add', dock :
			 * 'top', icon : 'images/add.png', text : 'hinzuf&uuml;gen', scope :
			 * this, handler : this.onAddClick }, { itemid : 'delete', icon :
			 * 'images/delete.png', text : 'l&ouml;schen', scope : this, handler :
			 * this.onDeleteClick }], dockedItems : [{ xtype : 'toolbar', dock :
			 * 'bottom', text : 'sync', scope : this }],
			 */

			listeners : {
				itemdblclick : this.onitemdblclick
			},
			features : [grouping],

			// based on MyApp.model.BestellpositionData'
			columns : [{
						xtype : 'gridcolumn',
						dataIndex : 'product',
						text : 'Artikel',
						width : 75,
						displayField : 'name',
						valueField : 'productNumber'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'productName',
						width : 150,
						text : 'Artikel Name'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'created',
						width : 70,
						text : 'erstellt',
						format : 'd/m/Y'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'orderNumber',
						width : 90,
						text : 'Bestellung'
						// }, {
					// xtype : 'gridcolumn',
					// dataIndex : 'invoiceNumber',
					// width : 80,
					// text : 'Rechnung'
				}	, {
						xtype : 'gridcolumn',
						dataIndex : 'quantity',
						width : 50,
						text : 'Menge'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'quantityLeft',
						width : 50,
						text : 'offen'
					}, {
						xtype : 'numbercolumn',
						dataIndex : 'priceNet',
						width : 50,
						text : 'Preis',
						renderer : Ext.util.Format.euMoney
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'status',
						width : 90,
						text : 'Status'
					}, /*{
						xtype : 'gridcolumn',
						dataIndex : 'customerNumber',
						width : 70,
						text : 'Kundennr'
					},*/ {
						xtype : 'gridcolumn',
						dataIndex : 'customerName',
						width : 120,
						text : 'Name'
						// }, {
					// xtype : 'gridcolumn',
					// dataIndex : 'expectedDelivery',
					// text : 'Geplante Auslieferung',
					// width : 20,
					// format : 'd/m/Y'
				}	, {
						xtype : 'actioncolumn',
						width : 30,
						sortable : false,
						// menuDisabled: true,
						items : [{
							icon : this.customicon,
							tooltip : 'Position in den naechten Schritt bringen',
							scope : this,
							handler : this.onActionClick
						}]
						// disabled : this.customIsFirstGrid
					}, {
						xtype : 'actioncolumn',
						width : 30,
						sortable : false,
						// menuDisabled: true,
						items : [{
							icon : '/FlexibleOrders/images/pdf_button.png',
							tooltip : 'Position in den naechten Schritt bringen',
							scope : this,
							handler : this.onPdfClick
						}]

						// disabled : this.customIsFirstGrid
					}, {
						xtype : 'actioncolumn',
						width : 30,
						sortable : false,
						menuDisabled : true,
						items : [{
							icon : '/FlexibleOrders/images/delete.png',
							tooltip : 'In den vorherigen Abschnitt verschieben',
							scope : this,
							handler : this.onRemoveClick
						}]
					}

			],
			dockedItems : [{
						xtype : 'pagingtoolbar',
						dock : 'bottom',
						width : 360,
						displayInfo : true,
						store : this.store
					}]
		});
		me.callParent(arguments);

	},

	onSync : function() {
		this.store.sync();
	},

	onActionClick : function() {
		// alert('Diese Methode ist "abstract" - sollte in den Unterklassen
		// implementiert werden ');
	},
	onitemdblclick : function(grid, record) {
		MyApp.getApplication().getController('MyController').showTransition(
				grid, record);
	},
	onRemoveClick : function(grid, rowIndex, a, b, c, d, e, f, g) {
		this.getStore().removeAt(rowIndex);
		this.getStore().sync();
	}

});