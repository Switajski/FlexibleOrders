Ext.define('MyApp.view.TransitionWindow', {
	extend : 'Ext.window.Window',
	customid : 'orderNumber',
	customfirstreport : 'Abstrakt',
	customsecondreport : 'Abstrakt2',
	customfirststore : 'BestellpositionDataStore',
	customsecondstore : 'ShippingItemDataStore',
	customfirstgrid:'PositionGrid',
	customsecondgrid:'PositionGrid',
	customtransitionfunction: function(){
		alert('muss ueberschrieben werden!');
	},
	itemid : 'TransitionWindow',
	requires : ['MyApp.view.BestellpositionGridPanel'],
	height : 500,
	width : 820,
	closeAction : 'hide',
	title : 'TransitionWindow',
	initComponent : function() {
		var me = this;
		//var firstDataStore = Ext.create(this.customfirststore, {id:''});
		//var secondDataStore = Ext.create(this.customsecondstore, {id:'andereId'});
		//TODO: über custom transitionFunction actionmethode des PositionGripanel überschreiben
		
		Ext.ComponentQuery.query('');

		Ext.applyIf(me, {
					layout : 'border',
					defaults : {
						// collapsible : true,
						split : true
						// bodyStyle : 'padding:15px'
					},
					items : [{
								xtype : 'form',
								region : 'north',
								id : 'BestellungForm',
								dock : 'top',
								bodyPadding : 10,
								items : [{
											xtype : 'fieldset',
											title : this.customfirstreport+'details',
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
														fieldLabel : this.customfirstreport+'datum'
													}]
										}

								]
							}, {
								xtype : this.customfirstgrid,
								region : 'center',
								itemid: 'firstGrid',
								flex : 1,
								store : this.customfirststore,
								onActionClick: this.customtransitionfunction,
								customIsFirstGrid: true
							},
							{
								xtype : this.customsecondgrid,
								region : 'south',
								itemid: 'secondGrid',
								flex : 1,
								store : this.secondDataStore,
								customIsFirstGrid: false
							}

					]
				});

		me.callParent(arguments);
	},
	loadAndShow : function(record) {
		this.show();
		/*this.down('form').getForm().loadRecord(record);
		this.down('grid[itemid=firstGrid]').getStore().filters.removeAll();
		this.down('grid[itemid=firstGrid]').getStore().filter(this.customid,
				record.data.orderNumber);
		this.down('grid[itemid=secondGrid]').getStore().filters.removeAll();
		this.down('grid[itemid=secondGrid]').getStore().filter(this.customid,
				record.data.orderNumber);*/
	}

});