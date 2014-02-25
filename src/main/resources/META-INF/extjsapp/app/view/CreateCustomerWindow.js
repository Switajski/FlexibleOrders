Ext.define('MyApp.view.CreateCustomerWindow', {
	extend : 'Ext.window.Window',
	title : 'Kunden erstellen',
	itemid : 'CreateCustomerWindow',
	alias : 'widget.CreateCustomerWindow',
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
												xtype : 'textfield',
												anchor : '100%',
												name : 'shortName',
												fieldLabel : 'Kurzname'
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
									title : 'Lieferadresse',
									flex : 1,
									items : [{
												xtype : 'textfield',
												anchor : '100%',
												name : 'name1',
												fieldLabel : 'Name 1',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'name2',
												fieldLabel : 'Name 2',
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
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false
											}]
								}/*, {
									xtype : 'fieldset',
									title : 'zus&auml;tzliche Informationen',
									flex : 1,
									items : [{
												itemid : 'invoiceNumber',
												xtype : 'invoicenumbercombobox',
												name : 'invoiceNumber',
												fieldLabel : 'Zahlungskondn.',
												allowBlank : false,
												allowDecimals : false,
												allowExponential : false
											}]
								}*/],

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
		
		var kunde = Ext.create('MyApp.model.KundeData', {
					shortName : this.record.data.shortName,
					firstName : this.record.data.firstName,
					lastName : this.record.data.lastName,
					email : this.record.data.email,
					phone : this.record.data.phone,
					name1 : this.record.data.name1,
					name2 : this.record.data.name2,
					street : this.record.data.street,
					postalCode : this.record.data.postalCode,
					city : this.record.data.city,
					country : this.record.data.country
				})

		this.down('form').getForm().loadRecord(kunde);
		
		// set the listeners to update record onChange
		this.down('form').items.each(function(item) {
			item.items.each(function(ii) {
				console.log(ii);
				ii.on({
					change : function() {
						var window = Ext.ComponentQuery.query('CreateCustomerWindow')[0];
						window.down('form').getForm()
								.updateRecord(window.record);
					}
				});
			});
		});
	},

	/**
	 * this method listens to the save button and is usually overridden by a
	 * panel. see {@Link MyController.deliver}
	 */
	onSave : function() {
		console.log("onSave"); 
		form = Ext.getCmp('CustomerForm');
		var active = form.getForm().getRecord(), form;

		if (!active) {
			return;
		}
		if (form.isValid()) {
			form.updateRecord(active);
			var customerStore = Ext.data.StoreMgr.lookup('KundeDataStore');
			customerStore.add(form.getForm().getRecord());
			var success = customerStore.sync();
			//TODO: reset and hide only on success!
			form.getForm().reset();
			Ext.ComponentQuery.query('CreateCustomerWindow')[0].hide();
		}
	}

});