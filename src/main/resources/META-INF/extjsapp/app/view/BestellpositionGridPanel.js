Ext.define('MyApp.view.BestellpositionGridPanel',{
	extend: 'Ext.grid.Panel',
	alias: 'widget.BestellpositionGrid',
	//width: 250,
	//height: 300,
	title: "Bestellpositionen",
	requires: [
	           'MyApp.model.ArtikelData',
	           'MyApp.store.BestellpositionDataStore',
	           'Ext.grid.plugin.CellEditing',
	           'Ext.form.field.Text',
	           'Ext.toolbar.TextItem'
	           ],
	
	 //store: 'BestellpositionDataStore',
	 initComponent: function() {
	        var me = this;
	        this.editing = Ext.create('Ext.grid.plugin.CellEditing');

	        Ext.applyIf(me, {
	        	plugins: [this.editing],
	        	tbar: [{
		        	dock: 'top',
		        	icon: 'images/add.png',
		        	text: 'hinzuf&uuml;gen',
		        	scope: this, 
		        	handler: this.onAddClick
		        }, {
		        	itemId: 'delete',
		        	icon: 'images/delete.png',
		        	text: 'l&ouml;schen',
		        	scope: this.onDeleteClick
		        }], 
	            columns: [
	                {
	                	 xtype: 'gridcolumn',
	                   	 dataIndex: 'product',
	                   	 text: 'Artikel',
	                   	 width: 150,
	                   	 editor: {
	                   		 id: 'ArtikelComboBox',
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
	                   	 text: 'Menge',
	                   	 editor: {
	                        xtype: 'numberfield',
	                        allowBlank: false
	                    }
	                   	 
	                },
	                {
	                   	 xtype: 'numbercolumn',
	                   	 dataIndex: 'priceNet',
	                   	 width: 75,
	                   	 text: 'Preis Netto',
	                   	 renderer: Ext.util.Format.euMoney
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
	                   	 text: 'Geplante Ausl.',
	                   	 width: 100,
	                   	 editor: {
	                   		 xtype: 'datefield',
	                   		 format: 'd/m/Y',
	                   		 allowBlank: true,
	                   		 minValue: Ext.Date.format(new Date(), 'd/m/Y'),
	                   		 minText: 'Datum liegt in der Vergangenheit'
	               	 }
	            }
	           ],
	        });
			me.callParent(arguments);
			
	},
	
	onSync: function(){
        this.store.sync();
    },
    
    onSelectChange: function(selModel, selections){
        this.down('#delete').setDisabled(selections.length === 0);
    },
	
	onAddClick: function(){
		var rec = new MyApp.model.BestellpositionData({
			
	        status: 'ORDERED'
			
		}), edit = this.editing;
		this.store.insert(0,rec);
		edit.startEditByPosition({
			row:0,
			column:0
		});
	},
	onDeleteClick: function(){
		var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection) {
			this.store.remove(selection);
		}
	},
	getBestellnr: function(){
		alert('hallo');
	}
	

});