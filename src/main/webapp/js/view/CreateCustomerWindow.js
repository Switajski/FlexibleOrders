Ext.define('MyApp.view.CreateCustomerWindow', {
	extend : 'Ext.window.Window',
	requires : [
	            'MyApp.view.DetailsFieldset',
	],
	title : 'Kunden erstellen',
	itemid : 'CreateCustomerWindow',
	alias : 'widget.CreateCustomerWindow',
	customerNumberEditable : true,
	defaultInvoiceNumber : 0,
	closeAction : 'destroy',
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
					                    anchor: '100%',
					                    labelWidth : 70
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
													labelWidth : 70,
													fieldLabel : 'Kundennr'
												}, {
													xtype : 'textfield',
													name : 'email',
													flex: 2,
													fieldLabel : 'E-Mail',
													labelWidth: 50,
													vtype: 'email'
												}]
					                }, {
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaults: {
					                         labelWidth : 70
					                    },
					                	items: [{
													xtype : 'textfield',
													flex: 1,
													name : 'companyName',
													fieldLabel : 'Firma'
												}]
					                }, {
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	fieldLabel: 'Name',
					                	defaults: {
					                         hideLabel: 'true',
					                         labelWidth : 70
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
					                	defaults: {
					                         labelWidth: 70,
					                         flex : 1,
					                    },
					                	items: [{
													xtype : 'textfield',
													name : 'phone',
													fieldLabel : 'Telefon'
												}, {
													xtype : 'textfield',
													margins: '0 0 0 6',
													name : 'fax',
													labelWidth : 30,
													fieldLabel : 'Fax'
												}]
					                }]
								}, {
									xtype : 'fieldset',
									title : 'Rechnungsadresse',
									defaults: {
					                    anchor: '100%',
					                    labelWidth : 70
					                },
					                items: [{
					                    xtype: 'fieldcontainer',
					                    layout: 'hbox',
					                    fieldLabel: 'Empf&auml;nger',
					                    combineErrors: true,
					                    defaultType: 'textfield',
					                    defaults: {
					                    	flex: 1,
						                    labelWidth : 70
						                },
					                    items: [{
												name : 'name1',
												hideLabel : true,
												emptyText : '1. Zeile',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dname1',
												allowBlank : true
												
											}, {
												name : 'name2',
												hideLabel : true,
												margins: '0 0 0 6',
												emptyText : '2. Zeile',
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
					                	defaults: {
						                    labelWidth : 70
						                },
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
					                	defaults: {
						                    labelWidth : 70,
						                    flex : 1
						                },
					                	items: [{
												xtype : 'numberfield',
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
												fieldLabel : 'Stadt',
												labelWidth: 35,
												margins: '0 0 0 6',
												listeners: {
							                        scope: this,
							                        change: this.onMailingAddrFieldChange
							                    },
							                    billingFieldName: 'dcity',
												allowBlank : false
											}]
					                }]
								},{
									xtype : 'fieldset',
									title : 'Lieferadresse',
									defaults: {
					                    anchor: '100%',
					                    labelWidth : 70
					                },
					                items: [{
					                    xtype: 'checkbox',
					                    name: 'shippingSameAsInvoicingAddress',
					                    fieldLabel: 'Lieferadresse gleich Rechnungsadresse?',
					                    labelWidth : 245,
					                    checked: false,
					                    margin: '0 0 10 0',
					                    scope: this,
					                    handler: this.onSameAddressChange
					                },{
					                    xtype: 'fieldcontainer',
					                    layout: 'hbox',
					                    combineErrors: true,
					                    defaultType: 'textfield',
					                    defaults: {
						                    labelWidth : 70,
						                    flex: 1
						                },
					                    items: [{
					                    		fieldLabel: 'Empf&auml;nger',
												name : 'dname1',
												emptyText : '1. Zeile',
												allowBlank : true
											}, {
												name : 'dname2',
												hideLabel : true,
												margins: '0 0 0 6',
												emptyText : '2. Zeile',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                    defaults: {
					                    	xtype : 'textfield',
						                    labelWidth : 70
						                },
					                	items: [{
												name : 'dstreet',
												flex: 1,
												fieldLabel : 'Strasse',
												allowBlank : false
											}]
					                },{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                    defaults: {
						                    labelWidth : 70,
						                    flex: 1,
						                },
					                	items: [{
												xtype : 'numberfield',
												name : 'dpostalCode',
												fieldLabel : 'PLZ',
												allowBlank : false
											}, {
												xtype : 'textfield',
												name : 'dcity',
												fieldLabel : 'Stadt',
												labelWidth: 35,
												margins: '0 0 0 6',
												allowBlank : false
											}]
					                }]
								}, {
									xtype: 'detailsFieldset'
								},
								{
									xtype : 'fieldset',
									title : 'Eigene Informationen - werden auf Dokumenten nicht angezeigt',
									name : 'notesSet',
									defaults: {
					                    anchor: '100%',
					                    labelWidth : 70
					                },
					                items : [{
										xtype : 'textarea',
										name : 'notes',
										flex: 1,
										fieldLabel : 'Notizen'
									},{
					                	xtype: 'fieldcontainer',
					                	layout: 'hbox',
					                	defaultType: 'displayfield',
					                	hideLabels : 'true',
					                	fieldLabel : 'Offen',
					                	labelWidth : 40,
					                	defaults: {
						                    labelWidth : 75,
						                    margins: '0 3 0 0',
						                    labelSeparator : "&nbsp;&nbsp;",
						                    style : "text-weight: bold"
						                },
					                	items: [{
					                		fieldLabel : 'Auftr&aumlge',
					                		flex : 1,
					                		labelWidth : 50,
					                		name : 'openOrderConfirmation'
					                	},{
					                		fieldLabel : 'Lieferscheine',
					                		flex : 1,
					                		name : 'openDeliveryNotes'
					                	},{
					                		fieldLabel : 'Rechnungen',
					                		flex : 1,
					                		name : 'openInvoices'
					                	}]
									}
									]
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
		
		customer = Ext.create('MyApp.model.KundeData', {});
		this.down('form').getForm().loadRecord(customer);
	},

	onSave : function() {
		console.error("function not overriden"); 
	},
	
	onMailingAddrFieldChange: function(field){
        var copyToBilling = this.down('[name=shippingSameAsInvoicingAddress]').getValue(),
            copyField = this.down('[name=' + field.billingFieldName + ']');

        if (copyToBilling) {
            copyField.setValue(field.getValue());
            form = this.down('form').getForm();
            form.updateRecord();
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