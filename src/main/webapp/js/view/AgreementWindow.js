Ext.define('MyApp.view.AgreementWindow', {
	extend : 'Ext.window.Window',
	title : 'Auftrag annehmen',
	itemid : 'AgreementWindow',
	alias : 'widget.AgreementWindow',
	layout : 'fit',
	defaultInvoiceNumber : 0,
	// width : 378,
	record : null,
	closeAction : 'destroy',

	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
					layout : 'anchor',
					items : [{
								xtype : 'form',
								id : 'AgreementForm',
								bodyPadding : 10,
								items : [{
											xtype : 'fieldset',
											// title : 'Kunde',
											flex : 1,
											items : [{
														xtype : 'displayfield',
														anchor : '100%',
														name : 'customerNumber',
														fieldLabel : 'Kundennr'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														name : 'lastName',
														fieldLabel : 'Name'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														fieldLabel : 'Liefertermin',
														name : 'expectedDelivery'
													}, {
														xtype : 'displayfield',
														anchor : '100%',
														name : 'orderConfirmationNumber',
														fieldLabel : 'ABNr'
													}, {
														xtype : 'textfield',
														anchor : '100%',
														name : 'orderAgreementNumber',
														fieldLabel : 'Auftragsnr'
													}]
										}],
								dockedItems : [{
											xtype : 'toolbar',
											dock : 'bottom',
											ui : 'footer',
											items : ['->', {
														iconCls : 'icon-save',
														itemId : 'save',
														text : 'annehmen',
														disabled : false,
														scope : this,
														handler : this.onSave
													}]
										}]
							}]
				});

		me.callParent(arguments);

	}

		/*
		 * onSave : function() { var active = this.activeRecord, form =
		 * this.getForm();
		 * 
		 * if (!active) { return; } if (form.isValid()) {
		 * form.updateRecord(active); this.onReset(); } }
		 */

});