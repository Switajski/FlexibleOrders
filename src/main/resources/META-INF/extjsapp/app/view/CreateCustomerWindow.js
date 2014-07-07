Ext.define('MyApp.view.CreateCustomerWindow', {
	extend : 'Ext.window.Window',
	title : 'Kunden erstellen',
	itemid : 'CreateCustomerWindow',
	alias : 'widget.CreateCustomerWindow',
	customerNumberEditable : true,
	layout : 'fit',
	defaultInvoiceNumber : 0,
	record : Ext.create('MyApp.model.KundeData', {
				}),
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			layout : 'anchor',
			items : [{
						xtype : 'form',
						id : 'CustomerForm',
						bodyPadding : 10,
						items : [{
									xtype : 'fieldset',
									title : 'Daten',
									flex : 1,
									items : [{
												xtype : 'numberfield',
												anchor : '100%',
												name : 'customerNumber',
												fieldLabel : 'Kundennr'
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'email',
												fieldLabel : 'E-Mail',
												vtype: 'email'
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'firstName',
												fieldLabel : 'Vorname'
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'lastName',
												fieldLabel : 'Nachname'
											}]
								}, {
									xtype : 'fieldset',
									title : 'Rechnungsadresse',
									flex : 1,
									items : [{
												xtype : 'textfield',
												anchor : '100%',
												name : 'name1',
												fieldLabel : 'Firma',
												allowBlank : true
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'name2',
												fieldLabel : 'Name',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'street',
												fieldLabel : 'Strasse',
												allowBlank : false
											}, {
												xtype : 'numberfield',
												anchor : '100%',
												name : 'postalCode',
												fieldLabel : 'PLZ',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'city',
												fieldLabel : 'Stadt',
												allowBlank : false
											}/*, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false
											}*/]
								},{
									xtype : 'fieldset',
									title : 'Lieferadresse',
									flex : 1,
									items : [{
												xtype : 'textfield',
												anchor : '100%',
												name : 'dname1',
												fieldLabel : 'Firma',
												allowBlank : true
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'dname2',
												fieldLabel : 'Name',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'dstreet',
												fieldLabel : 'Strasse',
												allowBlank : false
											}, {
												xtype : 'numberfield',
												anchor : '100%',
												name : 'dpostalCode',
												fieldLabel : 'PLZ',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'dcity',
												fieldLabel : 'Stadt',
												allowBlank : false
											}/*, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false
											}*/]
								}, {
									xtype : 'fieldset',
									title : 'zus&auml;tzliche Informationen',
									flex : 1,
									items : [{
												xtype : 'textfield',
												name : 'paymentConditions',
												fieldLabel : 'Zahlungskondn.'
											},{
												xtype : 'textfield',
												name : 'vatIdNo',
												fieldLabel : 'Umsatzst. Id'
											},{
												xtype : 'textfield',
												name : 'vendorNumber',
												fieldLabel : 'Lieferantennr.'
											}]
								}],

						dockedItems : [{
									xtype : 'toolbar',
									dock : 'bottom',
									ui : 'footer',
									items : ['->', {
												iconCls : 'icon-save',
												itemId : 'save',
												text : 'Speichern',
												disabled : false,
												scope : this,
												handler : this.onSave
											}]
								}]
					}
			]
		});

		me.callParent(arguments);
		if (this.record == null)
			console.error('no record set!');
		
		var kunde = this.createCustomerRecord();

		this.down('form').getForm().loadRecord(kunde);
		
		// set the listeners to update record onChange
		this.down('form').items.each(function(item) {
			item.items.each(function(ii) {
				ii.on({
					change : function() {
						var window = Ext.getCmp("CreateCustomerWindow");
						window.down('form').getForm()
								.updateRecord(window.record);
					}
				});
			});
		});
	},

	onSave : function() {
		console.error("function not overriden"); 
	},
	
	createCustomerRecord : function(){
		var record = Ext.create('MyApp.model.KundeData', {
			// this code is made to load a record into the window fields
			// don't know why this doesn't work without this 
					customerNumber : this.record.data.customerNumber,
					firstName : this.record.data.firstName,
					lastName : this.record.data.lastName,
					email : this.record.data.email,
					phone : this.record.data.phone,
					name1 : this.record.data.name1,
					name2 : this.record.data.name2,
					street : this.record.data.street,
					postalCode : this.record.data.postalCode,
					city : this.record.data.city,
					country : this.record.data.country,
					vendorNumber : this.record.data.vendorNumber,
					vatIdNo : this.record.data.vatIdNo,
					paymentConditions : this.record.data.paymentConditions,
					
					dname1 : this.record.data.dname1,
					dname2 : this.record.data.dname2,
					dstreet : this.record.data.dstreet,
					dpostalCode : this.record.data.dpostalCode,
					dcity : this.record.data.dcity,
					dcountry : this.record.data.dcountry
				});
		return record;
	}

});