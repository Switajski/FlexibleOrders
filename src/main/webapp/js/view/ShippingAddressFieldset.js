/**
 * 
 */
Ext.define('MyApp.view.ShippingAddressFieldset', {
	extend : 'Ext.form.FieldSet',
	alias : 'widget.shippingAddressFieldset',
	defaults: {
        anchor: '100%',
        labelWidth : 70
    },
    items: [{
        xtype: 'fieldcontainer',
        layout: 'hbox',
        fieldLabel: 'Empf&auml;nger',
        combineErrors: true,
        defaultType: 'textfield',
        defaults: {
        	flex: 1,
            labelWidth : 70
        },
        items: [{
				name : 'dname1',
				hideLabel : true,
				emptyText : '1. Zeile',
				allowBlank : false
				
			}, {
				name : 'dname2',
				hideLabel : true,
				margins: '0 0 0 6',
				emptyText : '2. Zeile',
				allowBlank : true
			}]
    },{
    	xtype: 'fieldcontainer',
    	layout: 'hbox',
    	defaultType: 'textfield',
    	defaults: {
            labelWidth : 70
        },
    	items: [{
				name : 'dstreet',
				flex: 1,
				fieldLabel : 'Strasse',
				allowBlank : false
			}]
    },{
    	xtype: 'fieldcontainer',
    	layout: 'hbox',
    	defaultType: 'textfield',
    	defaults: {
            labelWidth : 70,
            flex : 1
        },
    	items: [{
				xtype : 'numberfield',
				name : 'dpostalCode',
				fieldLabel : 'PLZ',
                billingFieldName: 'dpostalCode',
				allowBlank : false
			}, {
				xtype : 'textfield',
				name : 'dcity',
				fieldLabel : 'Stadt',
				labelWidth: 35,
				margins: '0 0 0 6',
                billingFieldName: 'dcity',
				allowBlank : false
			}]
    }]
});