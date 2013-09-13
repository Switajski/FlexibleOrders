Ext.define('MyApp.view.InvoiceNumberComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.invoicenumbercombobox',
	// anchor:'100%',
	name : 'invoice',
	itemid : 'invoiceNumber',
	displayField : 'invoiceNumber',
	valueField : 'invoiceNumber',
	//enableRegEx : true,
	//forceSelection : false,
	queryMode: 'remote',
	store : 'InvoiceNumberDataStore',
	minChars:1
	
});