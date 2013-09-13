Ext.define('MyApp.view.OrderNumberComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.ordernumbercombobox',
	// anchor:'100%',
	name : 'order',
	itemid : 'orderNumber',
	displayField : 'orderNumber',
	valueField : 'orderNumber',
	//enableRegEx : true,
	//forceSelection : false,
	queryMode: 'remote',
	store : 'OrderNumberDataStore',
	minChars:1
	
});