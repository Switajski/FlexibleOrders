Ext.define('MyApp.view.CreateCustomerWindow', {
	extend : 'Ext.window.Window',
	title : 'Kunden erstellen',
	itemid : 'CreateCustomerWindow',
	alias : 'widget.CreateCustomerWindow',
	customerNumberEditable : true,
	defaultInvoiceNumber : 0,
	record : Ext.create('MyApp.model.KundeData', {}),
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			width: 550,
            fieldDefaults: {
                labelAlign: 'right',
                labelWidth: 90,
                msgTarget: 'qtip'
            },
			items : [{
						xtype : 'form',
						id : 'CustomerForm',
						bodyPadding : 10,
						items : [{
									xtype : 'fieldset',
									title : 'Kontaktinformation',
									layout: 'anchor',
									defaults: {
					                    anchor: '100%'
					                },
					                items: [{
					                    xtype: 'fieldcontainer',
					                    layout: 'hbox',
					                    combineErrors: true,
					                    defaultType: 'textfield',
										items : [{
													xtype : 'numberfield',
													name : 'customerNumber',
													flex: 1,
													fieldLabel : 'Kundennr'
												}, {
													xtype : 'textfield',
													name : 'email',
													flex: 2,
													fieldLabel : 'E-Mail',
													labelWidth: 60,
													vtype: 'email'
												}]
					                }, {
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	fieldLabel: 'Name',
					                	defaults: {
					                         hideLabel: 'true'
					                    },
					                	items: [{
													xtype : 'textfield',
													flex: 1,
													name : 'firstName',
													emptyText : 'Vorname'
												}, {
													xtype : 'textfield',
													flex: 2,
													margins: '0 0 0 6',
													name : 'lastName',
													emptyText : 'Nachname'
												}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	fieldLabel : 'Telefon',
					                	items: [{
													xtype : 'textfield',
													flex: 1,
													name : 'telephoneNumber1',
													emptyText : 'Festnetz',
													hideLabel : 'true'
												}, {
													xtype : 'textfield',
													flex: 1,
													labelWidth: 70,
													margins: '0 0 0 6',
													name : 'telephoneNumber2',
													emptyText : 'Mobil',
													hideLabel : 'true'
												}]
					                }]
								}, {
									xtype : 'fieldset',
									title : 'Rechnungsadresse',
									defaults: {
					                    anchor: '100%'
					                },
					                items: [{
					                    xtype: 'fieldcontainer',
					                    layout: 'hbox',
					                    fieldLabel: 'Empf&auml;nger',
					                    combineErrors: true,
					                    defaultType: 'textfield',
					                    items: [{
												name : 'name1',
												flex: 1,
												hideLabel : true,
												emptyText : 'erste Zeile',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dname1',
												allowBlank : true
												
											}, {
												name : 'name2',
												hideLabel : true,
												flex: 1,
												margins: '0 0 0 6',
												emptyText : 'zweite Zeile',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dname2',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaultType: 'textfield',
					                	items: [{
												name : 'street',
												flex: 1,
												fieldLabel : 'Strasse',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dstreet',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaultType: 'textfield',
					                	items: [{
												xtype : 'numberfield',
												flex: 1,
												name : 'postalCode',
												fieldLabel : 'PLZ',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dpostalCode',
												allowBlank : false
											}, {
												xtype : 'textfield',
												name : 'city',
												flex: 1,
												fieldLabel : 'Stadt',
												labelWidth: 60,
												margins: '0 0 0 6',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dcity',
												allowBlank : false
											}/*, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false
											}*/]
					                }]
								},{
									xtype : 'fieldset',
									title : 'LieferAdresse',
									defaults: {
					                    anchor: '100%'
					                },
					                items: [{
					                    xtype: 'checkbox',
					                    name: 'shippingSameAsInvoicingAddress',
					                    fieldLabel: 'Gleiche Lieferadresse?',
					                    labelWidth : 150,
					                    checked: false,
					                    margin: '0 0 10 0',
					                    scope: this,
					                    handler: this.onSameAddressChange
					                },{
					                    xtype: 'fieldcontainer',
					                    layout: 'hbox',
					                    combineErrors: true,
					                    defaultType: 'textfield',
					                    items: [{
					                    		fieldLabel: 'Empf&auml;nger',
												name : 'dname1',
												flex: 1,
												emptyText : 'erste Zeile',
												allowBlank : true
												
											}, {
												name : 'dname2',
												hideLabel : true,
												flex: 1,
												margins: '0 0 0 6',
												emptyText : 'zweite Zeile',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaultType: 'textfield',
					                	items: [{
												name : 'dstreet',
												flex: 1,
												fieldLabel : 'Strasse',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaultType: 'textfield',
					                	items: [{
												xtype : 'numberfield',
												flex: 1,
												name : 'dpostalCode',
												fieldLabel : 'PLZ',
												allowBlank : false
											}, {
												xtype : 'textfield',
												name : 'dcity',
												flex: 1,
												fieldLabel : 'Stadt',
												labelWidth: 60,
												margins: '0 0 0 6',
												allowBlank : false
											}/*, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false
											}*/]
					                }]
								}, {
									xtype : 'fieldset',
									title : 'zus&auml;tzliche Informationen',
									defaults: {
					                    anchor: '100%'
					                },
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
											},{
												xtype : 'textfield',
												name : 'saleRepresentative',
												fieldLabel : 'Vertreter'
											},{
												xtype : 'textfield',
												name : 'mark',
												fieldLabel : 'Ihr Zeichen'
											},{
												xtype : 'textfield',
												name : 'contact1',
												fieldLabel : 'Kontakt 1'
											},{
												xtype : 'textfield',
												name : 'contact2',
												fieldLabel : 'Kontakt 2'
											},{
												xtype : 'textfield',
												name : 'contact3',
												fieldLabel : 'Kontakt 3'
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
					dcountry : this.record.data.dcountry,
					
					saleRepresentative : this.record.data.saleRepresentative, 
					mark : this.record.data.mark, 
					contact1 : this.record.data.contact1, 
					contact2 : this.record.data.contact2, 
					contact3 : this.record.data.contact3
				});
		return record;
	},
	
	onMailingAddrFieldChange: function(field){
        var copyToBilling = this.down('[name=shippingSameAsInvoicingAddress]').getValue(),
            copyField = this.down('[name=' + field.billingFieldName + ']');

        if (copyToBilling) {
            copyField.setValue(field.getValue());
        } else {
            copyField.clearInvalid();
        }
    },
	
	/**
     * Enables or disables the billing address fields according to whether the checkbox is checked.
     * In addition to disabling the fields, they are animated to a low opacity so they don't take
     * up visual attention.
     */
    onSameAddressChange: function(box, checked){
        var fieldset = box.ownerCt;
        Ext.Array.forEach(fieldset.previousSibling().query('textfield'), this.onMailingAddrFieldChange, this);
        Ext.Array.forEach(fieldset.query('textfield'), function(field) {
            field.setDisabled(checked);
            // Animate the opacity on each field. Would be more efficient to wrap them in a container
            // and animate the opacity on just the single container element, but IE has a bug where
            // the alpha filter does not get applied on position:relative children.
            // This must only be applied when it is not IE6, as it has issues with opacity when cleartype
            // is enabled
            if (!Ext.isIE6) {
                field.el.animate({opacity: checked ? 0.3 : 1});
            }
        });
    }

});