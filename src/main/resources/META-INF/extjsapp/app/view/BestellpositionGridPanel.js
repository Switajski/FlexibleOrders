Ext.define('MyApp.view.BestellpositionGridPanel',{
	extend: 'Ext.grid.Panel',
	alias: 'widget.BestellpositionGrid',
	//width: 250,
	//height: 300,
	title: "Bestellpositionen",
	requires: [
	           'MyApp.model.ArtikelData',
	           'MyApp.store.BestellpositionDataStore'
	           ],
	
	 store: 'BestellpositionDataStore',
	 initComponent: function() {
	        var me = this;

	        Ext.applyIf(me, {
	            columns: [
	                {
	                	 xtype: 'gridcolumn',
	                   	 dataIndex: 'product',
	                   	 text: 'Artikel',
	                   	 editor: {
	                   		 id: 'ArtikelComboBox',
	                   		 width: 150,
	                   		 xtype: 'combobox',
	                   		 //allowBlank: false,
	                   		 displayField: 'name',
	                   		 enableRegEx: true,
	                   		 forceSelection: true,
	                   		 queryMode: 'local',
	                   		 store: 'ArtikelDataStore',
	                   		 tpl: Ext.create('Ext.XTemplate',
	                   				 '<tpl for=".">',
	                   				 '<div class="x-boundlist-item">{artikelnummer} - {name}</div>',
	                   				 '</tpl>'
	                   		 ),
	                   		 displayTpl: Ext.create('Ext.XTemplate',
	                   				 '<tpl for=".">',
	                   				 '{artikelnummer} - {name}',
	                   				 '</tpl>'
	                   		 )
	                   	 }
	                },
	                {
	                   	 xtype: 'gridcolumn',
	                   	 dataIndex: 'orderNumber',
	                   	 width: 75,
	                   	 text: 'Bestellung'
	                },
	                {
	                   	 xtype: 'gridcolumn',
	                   	 dataIndex: 'invoiceNumber',
	                   	 width: 75,
	                   	 text: 'Rechnung'
	                },
	                {
	                   	 xtype: 'gridcolumn',
	                   	 dataIndex: 'orderConfirmationNumber',
	                   	 width: 75,
	                   	 text: 'AB'
	                },
	                {
	                   	 xtype: 'gridcolumn',
	                   	 dataIndex: 'quantity',
	                   	 width: 50,
	                   	 text: 'Menge'
	                },
	                {
	                   	 xtype: 'gridcolumn',
	                   	 dataIndex: 'priceNet',
	                   	 width: 75,
	                   	 text: 'Preis Netto'
	                 },
	                    {
	                  	 xtype: 'gridcolumn',
	                   	 dataIndex: 'status',
	                   	 width: 75,
	                   	 text: 'Status'
	                 },
	                 {
	                 	 xtype: 'gridcolumn',
	                   	 dataIndex: 'expectedDelivery',
	                   	 text: 'Gepl.',
	                   	 width: 50,
	                   	 editor: {
	                   		 xtype: 'datefield',
	                   		 format: 'd/m/Y',
	                   		 allowBlank: true,
	                   		 minValue: Ext.Date.format(new Date(), 'd/m/Y'),
	                   		 minText: 'Datum liegt in der Vergangenheit'
	               	 }
	            }
	           ]
	        });
			me.callParent(arguments);
	}/*,
                   plugins: [
                       Ext.create('Ext.grid.plugin.RowEditing', {
                           id: 'BpGridRowEditing',
                          clicksToMoveEditor: 1,
                           autoCancel: false
                       })
                   ]*/
});