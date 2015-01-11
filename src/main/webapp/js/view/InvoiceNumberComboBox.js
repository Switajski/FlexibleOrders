Ext.define('MyApp.view.InvoiceNumberComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.invoicenumbercombobox',
	name : 'invoice',
	itemid : 'invoiceNumber',
	displayField : 'invoiceNumber',
	valueField : 'invoiceNumber',
	queryMode : 'remote',
	store : 'InvoiceNumberDataStore',
	minChars : 1
});