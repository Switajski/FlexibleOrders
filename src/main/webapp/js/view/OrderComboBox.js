Ext.define('MyApp.view.OrderComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.ordercombobox',
	xtype : 'combobox',
	// anchor:'100%',
	name : 'order',
	itemid : 'orderNumber',
	//fieldLabel : 'Kunde',
	displayField : 'orderNumber',
	valueField : 'orderNumber',
	// allowBlank: false,
	enableRegEx : true,
	forceSelection : false,
	//queryMode : 'local',
	store : 'BestellungDataStore',
	tpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'<div class="x-boundlist-item">{orderNumber} - {customer}</div>', '</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{orderNumber} - {customer}', '</tpl>')

});