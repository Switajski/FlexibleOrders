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

Ext.define('MyApp.view.PositionGridPanel', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.PositionGrid',
	title : "Abstrakte Positionen - (abstract items)",
	customIsFirstPanel : true,
	customicon: 'images/new_rechnung.png',
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');

		Ext.applyIf(me, {
			plugins : [this.editing],
			/*
			 * tbar : [{ itemid: 'add', dock : 'top', icon : 'images/add.png',
			 * text : 'hinzuf&uuml;gen', scope : this, handler : this.onAddClick }, {
			 * itemid : 'delete', icon : 'images/delete.png', text :
			 * 'l&ouml;schen', scope : this, handler : this.onDeleteClick }],
			 * dockedItems : [{ xtype : 'toolbar', dock : 'bottom', text :
			 * 'sync', scope : this }],
			 */

			listeners:{
		itemdblclick: this.onitemdblclick
	},
			features : [filters],
			columns : [{
						xtype : 'gridcolumn',
						dataIndex : 'product',
						text : 'Artikel',
						width : 200,
						displayField : 'name',
						valueField : 'productNumber'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'orderNumber',
						width : 75,
						text : 'Bestellung',
						filter : {
							type : 'string'
						}
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'orderConfirmationNumber',
						width : 75,
						text : 'AB'
					},{
						xtype : 'gridcolumn',
						dataIndex : 'invoiceNumber',
						width : 75,
						text : 'Rechnung'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'quantity',
						width : 50,
						text : 'Menge'

					}, {
						xtype : 'numbercolumn',
						dataIndex : 'priceNet',
						width : 75,
						text : 'Preis Netto',
						renderer : Ext.util.Format.euMoney
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'status',
						width : 75,
						text : 'Status',
						filter : {
							type : 'string'
						}
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'customer',
						width : 25,
						text : 'KId'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'expectedDelivery',
						text : 'Geplante Auslieferung',
						width : 120,
						format : 'd/m/Y'
					},
					{
						xtype : 'actioncolumn',
						width : 30,
						sortable : false,
						// menuDisabled: true,
						items : [{
							icon : this.customicon,
							tooltip : 'Position in den naechten Schritt bringen',
							scope : this,
							handler : this.onActionClick
						}],
						//disabled : this.customIsFirstGrid
					},
					{
						xtype : 'actioncolumn',
						width : 30,
						sortable : false,
						// menuDisabled: true,
						items : [{
							icon : 'images/pdf_button.png',
							tooltip : 'Position in den naechten Schritt bringen',
							scope : this,
							handler : this.onPdfClick
						}],
						//disabled : this.customIsFirstGrid
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
	onitemdblclick:function(grid, record){
		MyApp.getApplication().getController('MyController').showTransition(grid, record);
	}

});