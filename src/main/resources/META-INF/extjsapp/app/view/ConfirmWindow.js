Ext.define('MyApp.view.ConfirmWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Bestellung best&auml;tigen',
	itemid: 'ConfirmWindow'
	/*initComponent : function() {
		var me = this;
		var firstDataStore = Ext.create('MyApp.store.BestellpositionDataStore',
				{
					id : 'jfjfjfjfjfjfjf'
				});
		var firstGrid = Ext.create('MyApp.view.BestellpositionGridPanel', {
					xtype : 'BestellpositionGrid',
					region : 'south',
					flex : 1,
					id : 'firstGrid',
					store : firstDataStore
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
	}*/

});