/**
 * 
 */
Ext.define('MyApp.view.AddressFieldset', {
	extend : 'Ext.form.FieldSet',
	alias : 'widget.addressFieldset',
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
				name : 'name1',
				hideLabel : true,
				emptyText : '1. Zeile',
                billingFieldName: 'dname1',
				allowBlank : false
				
			}, {
				name : 'name2',
				hideLabel : true,
				margins: '0 0 0 6',
				emptyText : '2. Zeile',
                billingFieldName: 'dname2',
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
				name : 'street',
				flex: 1,
				fieldLabel : 'Strasse',
                billingFieldName: 'dstreet',
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
				name : 'postalCode',
				fieldLabel : 'PLZ',
                billingFieldName: 'dpostalCode',
				allowBlank : false
			}, {
				xtype : 'textfield',
				name : 'city',
				fieldLabel : 'Stadt',
				labelWidth: 35,
				margins: '0 0 0 6',
                billingFieldName: 'dcity',
				allowBlank : false
			}]
    }]
});