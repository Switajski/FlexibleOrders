Ext.define('MyApp.view.OrderComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.ordercombobox',
	xtype : 'combobox',
	name : 'order',
	itemid : 'orderNumber',
	displayField : 'orderNumber',
	valueField : 'orderNumber',
	enableRegEx : true,
	forceSelection : false,
	store : 'BestellungDataStore',
	tpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'<div class="x-boundlist-item">{orderNumber} - {customer}</div>',
			'</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{orderNumber} - {customer}', '</tpl>')

});