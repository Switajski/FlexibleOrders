Ext.define('MyApp.view.OrderNumberComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.ordernumbercombobox',
	name : 'order',
	itemid : 'orderNumber',
	displayField : 'orderNumber',
	valueField : 'orderNumber',
	queryMode : 'remote',
	store : 'OrderNumberDataStore',
	minChars : 1

});