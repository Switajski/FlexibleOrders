Ext.define('MyApp.view.CountryComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.countrycombobox',
	xtype : 'combobox',
	name : 'countrymethod',
	itemid : 'countrymethod',
	displayField : 'country',
	valueField : 'id',
	enableRegEx : true,
	forceSelection : true,
	queryMode : 'local',
	store : 'CountryDataStore',
	getModelData : function(){
		var obj = {};
		obj[this.name] = this.value;
		return obj;
	},
	tpl : Ext
	.create(
			'Ext.XTemplate',
			'<tpl for=".">',
			'<div class="x-boundlist-item">{country}</div>',
	'</tpl>'),
	displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
			'{country}', '</tpl>')
});