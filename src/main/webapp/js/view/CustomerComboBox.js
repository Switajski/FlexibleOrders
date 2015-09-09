Ext.define('MyApp.view.CustomerComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.customercombobox',
	xtype : 'combobox',
	name : 'customer',
	itemid : 'customer',
	displayField : 'customerNumber',
	valueField : 'customerNumber',
	width : 350,
	enableRegEx : true,
	queryMode : 'remote',
	allowBlank : false,
	store : 'KundeDataStore',
	tpl : Ext.create(
					'Ext.XTemplate',
					'<tpl for=".">',
					'<div class="x-boundlist-item">{customerNumber} - {companyName} {lastName}</div>',
					'</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{customerNumber} - {companyName} {lastName}', '</tpl>')

});