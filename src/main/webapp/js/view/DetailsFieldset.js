/**
 * 
 */
Ext.define('MyApp.view.DetailsFieldset', {
	extend : 'Ext.form.FieldSet',
	alias : 'widget.detailsFieldset',
	xtype : 'detailsFieldset',
	title : 'Zus&auml;tzliche Informationen',
	defaults : {
		anchor : '100%',
		labelWidth : 70
	},
	items : [ {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		defaultType : 'textfield',
		items : [ {
			xtype : 'textarea',
			name : 'paymentConditions',
			flex : 1,
			fieldLabel : 'Zahlungskonditionen',
			labelWidth : 125
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		defaultType : 'textfield',
		defaults : {
			labelWidth : 70
		},
		items : [ {
			xtype : 'textfield',
			name : 'vatIdNo',
			flex : 1,
			fieldLabel : 'Umsatzst.Id'
		}, {
			xtype : 'textfield',
			name : 'vendorNumber',
			flex : 1,
			fieldLabel : 'Lieferantennr.'
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		defaultType : 'textfield',
		defaults : {
			labelWidth : 70
		},
		items : [ {
			xtype : 'textfield',
			name : 'saleRepresentative',
			flex : 1,
			fieldLabel : 'Vertreter'
		}, {
			xtype : 'textfield',
			name : 'mark',
			flex : 1,
			fieldLabel : 'Ihr Zeichen'
		} ]
	}, {
		xtype : 'fieldcontainer',
		layout : 'hbox',
		fieldLabel : 'Kontakt',
		defaultType : 'textfield',
		defaults : {
			labelWidth : 70
		},
		items : [ {
			xtype : 'textfield',
			name : 'contact1',
			flex : 1,
			emptyText : '1. Zeile'
		}, {
			xtype : 'textfield',
			name : 'contact2',
			flex : 1,
			margins : '0 0 0 6',
			emptyText : '2. Zeile'
		}, {
			xtype : 'textfield',
			name : 'contact3',
			flex : 1,
			margins : '0 0 0 6',
			emptyText : '3. Zeile'
		} ]
	} ]
});