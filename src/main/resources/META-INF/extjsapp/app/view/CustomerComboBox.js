Ext.define('MyApp.view.CustomerComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.customercombobox',
	xtype : 'combobox',
	// anchor:'100%',
	name : 'customer',
	itemid : 'customer',
	//fieldLabel : 'Kunde',
	displayField : 'shortName',
	valueField : 'id',
	// allowBlank: false,
	enableRegEx : true,
	forceSelection : true,
	queryMode : 'local',
	store : 'KundeDataStore',
	tpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'<div class="x-boundlist-item">{id} - {shortName}</div>', '</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{id} - {shortName}', '</tpl>')

});