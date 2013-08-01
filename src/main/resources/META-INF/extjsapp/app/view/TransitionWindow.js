Ext.define('MyApp.view.TransitionWindow', {
	customid: 'orderNumber',
	customstore: 'MyApp.store.BestellpositionDataStore',
	itemid: 'TransitionWindow',
	extend : 'Ext.window.Window',
	requires : ['MyApp.view.BestellpositionGridPanel'],
	height : 400,
	width : 500,
	closeAction : 'destroy',
	title : 'TransitionWindow',
	initComponent : function() {
		var me = this;
		var firstDataStore = Ext.create(this.customstore,
				{
				});
		
		Ext.applyIf(me, {
					layout : 'border',
					defaults : {
						// collapsible : true,
						split : true
						// bodyStyle : 'padding:15px'
					},
					items : [{
								xtype : 'form',
								region : 'center',
								id : 'BestellungForm',
								dock : 'top',
								bodyPadding : 10,
								items : [{
											xtype : 'fieldset',
											title : 'Bestellungdetails',
											flex : 1,
											items : [{
														xtype : 'displayfield',
														anchor : '100%',
														name : 'orderNumber',
														fieldLabel : 'Bestellnr'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														name : 'customer',
														fieldLabel : 'Kunde'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														name : 'status',
														fieldLabel : 'Status'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														name : 'created',
														fieldLabel : 'Bestelldatum'
													}]
										}

								]
							}, {
								xtype : 'PositionGrid',
								region : 'south',
								flex : 1,
								id : 'firstGrid',
								store : firstDataStore
							}

					]
				});

		me.callParent(arguments);
	},
	loadAndShow : function(record){
		this.show();
		this.down('form').getForm().loadRecord(record);
		this.down('grid').getStore().filters.removeAll();
		this.down('grid').getStore().filter(this.customid,record.data.orderNumber);
	}

});