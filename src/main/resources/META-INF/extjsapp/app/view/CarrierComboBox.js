Ext.define('MyApp.view.CarrierComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.carriercombobox',
	xtype : 'combobox',
	// anchor:'100%',
	name : 'carrier',
	itemid : 'carrier',
	//fieldLabel : 'Kunde',
	displayField : 'carrierNumber',
	valueField : 'id',
	width: 350,
	// allowBlank: false,
	enableRegEx : true,
	forceSelection : true,
	queryMode : 'local',
	store : 'CarrierDataStore',
	tpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'<div class="x-boundlist-item">{carrierNumber} - {name}</div>', '</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{carrierNumber} - {name}', '</tpl>')

});