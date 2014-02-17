Ext.define('MyApp.view.DeliverWindow', {
	extend : 'Ext.window.Window',
	title : 'Auftrag liefern',
	itemid : 'DeliverWindow',
	alias : 'widget.DeliverWindow',
	layout : 'fit',
	defaultInvoiceNumber : 0,
	record : null,
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			layout : 'anchor',
			items : [{
						xtype : 'form',
						id : 'DeliverForm',
						bodyPadding : 10,
						items : [{
									xtype : 'fieldset',
									//title : 'Kunde',
									flex : 1,
									items : [{
												xtype : 'displayfield',
												anchor : '100%',
												name : 'shortName',
												fieldLabel : 'Kurzname'
											}, {
												xtype : 'displayfield',
												anchor : '100%',
												name : 'id',
												fieldLabel : 'Kundennr'
											},{
												//itemid : 'invoiceNumber',
												//xtype : 'invoicenumbercombobox',
												xtype : 'textfield',
												fieldLabel : 'Rechnungsnr',
												name : 'invoiceNumber',
												id : 'invoiceNumber',
												allowBlank : false,
												onChange : function(oldValue, newValue){
													this.up('form').setValues({invoiceNumber : newValue})
												}
											}, {
												xtype : 'numberfield',
												fieldLabel : 'Paketnr',
												name : 'packageNumber',
												allowBlank : true,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1,
												onChange : function(oldValue, newValue){
													this.up('form').setValues({packageNumber : newValue})
												}
											}, {
												xtype : 'numberfield',
												fieldLabel : 'Sendungsnr',
												name : 'trackNumber',
												allowBlank : true,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1,
												onChange : function(oldValue, newValue){
													this.up('form').setValues({trackNumber: newValue})
												}
											}/*, {
												xtype : 'numberfield',
												fieldLabel : 'Menge',
												name : 'quantity',
												allowBlank : false,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1,
												maxValue : this.record.data.quantity
											}*/]
								}, {
									xtype : 'fieldset',
									title : 'Lieferadresse',
									flex : 1,
									items : [{
												xtype : 'textfield',
												anchor : '100%',
												name : 'name1',
												fieldLabel : 'Name 1',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'name2',
												fieldLabel : 'Name 2',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'street',
												fieldLabel : 'Strasse',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'postalCode',
												fieldLabel : 'PLZ',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'city',
												fieldLabel : 'Stadt',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'country',
												fieldLabel : 'Land',
												allowBlank : false,
												onChange: function(){
													var form = this.up('form').getForm();
													form.updateRecord(form.record);
												}
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
								}, {
									xtype : 'InvoiceItemGrid',
									dock : 'bottom',
									id: 'CreateInvoiceItemGrid',
									flex : 1,
									store : 'CreateInvoiceItemDataStore',
									//store : 'InvoiceItemDataStore',
									title : "Rechnungs-/Lieferscheinpositionen",
									features : null,
									columns : [{
												xtype : 'gridcolumn',
												dataIndex : 'product',
												text : 'Artikel',
												width : 75,
												displayField : 'name',
												valueField : 'productNumber'
											}, {
												xtype : 'gridcolumn',
												dataIndex : 'productName',
												width : 150,
												text : 'Artikel Name'
											}, {
												xtype : 'gridcolumn',
												dataIndex : 'quantity',
												width : 50,
												text : 'Menge'
											}, {
												xtype : 'numbercolumn',
												dataIndex : 'priceNet',
												width : 50,
												text : 'Preis',
												renderer : Ext.util.Format.euMoney
											}]
								}]
					}
			]
		});

		me.callParent(arguments);
		if (this.record == null)
			console.error('no record set!');

		// set the listeners to update record onChange
		/*this.down('form').items.each(function(item) {
			item.items.each(function(ii) {
				console.log(ii);
				ii.on({
					change : function() {
						var window = Ext.ComponentQuery.query('DeliverWindow')[0];
						window.down('form').getForm()
								.updateRecord(window.record);
					}
				});
			});
		});*/
	},
	/**
	 * this method is to override by the using Component (usually Panel)
	 */
	updateRecord : function() {
		console.log('Override me!');
	},

	// TODO: remove setter and getter
	setInvoiceNumber : function(invoiceNumber) {
		this.down('form').down('invoicenumbercombobox').setValue(invoiceNumber);
		this.record.data.invoiceNumber = invoiceNumber;
	},
	setQuantity : function(quantity) {
		this.down('form').down('numberfield[name=quantity]').setValue(quantity);
	},
	setPackageNumber : function(packageNumber) {
		this.down('form').down('numberfield[name=packageNumber]')
				.setValue(packageNumber);
	},
	setTrackNumber : function(trackNumber) {
		this.down('form').down('numberfield[name=trackNumber}')
				.setValue(trackNumber);
	},
	setProduct : function(product) {
		this.down('form').down('displayfield[name=product]').setValue(product);
	},
	setCustomer : function(customer) {
		this.down('form').down('displayfield[name=customer]')
				.setValue(customer);
		// alternativ:
		// Ext.ComponentQuery.query('DeliverWindow
		// displayfield[name=customer]')[0].setValue(product);
	},
	getInvoiceNumber : function(invoiceNumber) {
		this.down('form').down('invoicenumbercombobox').getValue(invoiceNumber);
	},
	getQuantity : function(quantity) {
		this.down('form').down('numberfield[name=quantity]').getValue(quantity);
	},
	getPackageNumber : function(packageNumber) {
		this.down('form').down('numberfield[name=packageNumber]')
				.getValue(packageNumber);
	},
	getTrackNumber : function(trackNumber) {
		this.down('form').down('numberfield[name=trackNumber}')
				.getValue(trackNumber);
	},
	getProduct : function(product) {
		this.down('form').down('displayfield[name=product]').getValue(product);
	},
	getCustomer : function(customer) {
		this.down('form').down('displayfield[name=customer]')
				.getValue(customer);
	},
	/**
	 * this method listens to the save button and is usually overridden by a
	 * panel. see {@Link MyController.deliver}
	 */
	onSave : function() {
		var active = this.activeRecord, form = this.getForm();

		if (!active) {
			return;
		}
		if (form.isValid()) {
			form.updateRecord(active);
			this.onReset();
		}
	},
	onComboboxChange : function() {
		if (record != null) {
			var combobox = Ext.ComponentQuery.query('invoicenumbercombobox')[0];
			this.record.data.invoiceNumber = combobox.getValue();
			console.log('new value' + combobox.getValue());
		}
	}

});