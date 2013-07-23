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
		        dockedItems:[{
		        	xtype: 'toolbar',
		        	dock:'bottom',
		        	text:'sync',
		        	scope: this
		        }],
	            columns: [
	                {
	                	 xtype: 'gridcolumn',
	                   	 dataIndex: 'product',
	                   	 text: 'Artikel',
	                   	 width: 200,
//	                   	 name: 'productNumber',
	                   	 editor: {
	                   		 id: 'ArtikelComboBox',
	                   		 xtype: 'combobox',
	                   		 //allowBlank: false,
	                   		 displayField: 'name',
	                   		 valueField: 'productNumber',
	                   		 enableRegEx: true,
	                   		 allowBlank: false,
	                   		 forceSelection: true,
	                   		 queryMode: 'local',
	                   		 store: 'ArtikelDataStore',
	                   		 tpl: Ext.create('Ext.XTemplate',
	                   				 '<tpl for=".">',
	                   				 '<div class="x-boundlist-item">{productNumber} - {name}</div>',
	                   				 '</tpl>'
	                   		 ),
	                   		 displayTpl: Ext.create('Ext.XTemplate',
	                   				 '<tpl for=".">',
	                   				 '{productNumber} - {name}',
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
	                        allowBlank: false,
	                        minValue : 1	
	                    }
	                   	 
	                },
	                {
	                   	 xtype: 'numbercolumn',
	                   	 dataIndex: 'priceNet',
	                   	 width: 75,
	                   	 text: 'Preis Netto',
	                   	 renderer: Ext.util.Format.euMoney,
	                   	 editor: {
	                        xtype: 'numberfield',
	                        allowBlank: true
	                    }
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
	                   	 text: 'Geplante Auslieferung',
	                   	 width: 110,
	                   	 format: 'd/m/Y',
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
		bestellnr = Ext.ComponentQuery.query('form[itemid="form"]')[0].getForm().getValues().orderNumber;
		console.log('hierher!');
		if (bestellnr == null || bestellnr==0 || bestellnr=="")
		{
			Ext.MessageBox.show({
				title: 'Bestellnummer leer',
				msg: 'Bitte eine Bestellnummer eingeben',
				icon: Ext.MessageBox.ERROR,
				buttons: Ext.Msg.OK
			});
		} 
		else {
			var rec = new MyApp.model.BestellpositionData({
				status: 'ORDERED'
			}), edit = this.editing;
			rec.data.orderNumber = bestellnr;
			this.store.insert(0,rec);
			edit.startEditByPosition({
				row:0,
				column:0
			});
		}
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