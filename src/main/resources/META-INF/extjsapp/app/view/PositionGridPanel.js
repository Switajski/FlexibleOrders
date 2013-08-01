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
	title : "Positionen",
	initComponent : function() {
		var me = this;
		this.editing = Ext.create('Ext.grid.plugin.CellEditing');

		Ext.applyIf(me, {
			plugins : [this.editing],
			/*tbar : [{
						itemid: 'add',
						dock : 'top',
						icon : 'images/add.png',
						text : 'hinzuf&uuml;gen',
						scope : this,
						handler : this.onAddClick
					}, {
						itemid : 'delete',
						icon : 'images/delete.png',
						text : 'l&ouml;schen',
						scope : this,
						handler : this.onDeleteClick
					}],
			dockedItems : [{
						xtype : 'toolbar',
						dock : 'bottom',
						text : 'sync',
						scope : this
					}],*/
			
			features : [filters],
			columns : [{
				xtype : 'gridcolumn',
				dataIndex : 'product',
				text : 'Artikel',
				width : 200,
				displayField: 'name',
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
				dataIndex : 'invoiceNumber',
				width : 75,
				text : 'Rechnung'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'orderConfirmationNumber',
				width : 75,
				text : 'AB'
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
				text : 'Status'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'expectedDelivery',
				text : 'Geplante Auslieferung',
				width : 110,
				format : 'd/m/Y'
			},
			
			{
                xtype: 'actioncolumn',
                width: 30,
                sortable: false,
                //menuDisabled: true,
                items: [{
                    icon: 'images/new_rechnung.png',
                    tooltip: 'Delete Plant',
                    scope: this,
                    handler: this.onActionClick
                }]
            }
			
			]
		});
		me.callParent(arguments);

	},

	onSync : function() {
		this.store.sync();
	},
	
	onActionClick: function(){
		alert('Diese Methode ist "abstract" - sollte in den Unterklassen implementiert werden ');
	}

	
});