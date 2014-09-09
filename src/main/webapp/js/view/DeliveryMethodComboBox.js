Ext.define('MyApp.view.DeliveryMethodComboBox', {
			extend : 'Ext.form.field.ComboBox',
			alias : 'widget.deliverymethodcombobox',
			xtype : 'combobox',
			// anchor:'100%',
			name : 'deliverymethod',
			itemid : 'deliverymethod',
			displayField : 'deliverymethodNo',
			valueField : 'id',
			// width : 350,
			// allowBlank: false,
			enableRegEx : true,
			forceSelection : true,
			queryMode : 'local',
			store : 'DeliveryMethodDataStore',
			tpl : Ext
					.create(
							'Ext.XTemplate',
							'<tpl for=".">',
							'<div class="x-boundlist-item">{deliveryMethodNo} - {name} {name1}</div>',
							'</tpl>'),
			displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
					'{deliveryMethodNo} - {name} {name1}', '</tpl>')

		});