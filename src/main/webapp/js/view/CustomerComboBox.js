Ext.define('MyApp.view.CustomerComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.customercombobox',
	xtype : 'combobox',
	// anchor:'100%',
	name : 'customer',
	itemid : 'customer',
	// fieldLabel : 'Kunde',
	displayField : 'customerNumber',
	valueField : 'id',
	width : 350,
	// allowBlank: false,
	enableRegEx : true,
	queryMode : 'local',
	store : 'KundeDataStore',
	tpl : Ext
			.create(
					'Ext.XTemplate',
					'<tpl for=".">',
					'<div class="x-boundlist-item">{customerNumber} - {firstName} {lastName}</div>',
					'</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{customerNumber} - {firstName} {lastName}', '</tpl>')

});