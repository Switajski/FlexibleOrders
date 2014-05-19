Ext.define('MyApp.model.BestellungData', {
    extend: 'Ext.data.Model',
    fields: [
		{
		    name: 'orderNumber'
		},
        {
            name: 'customer'
        },
        {
            name: 'created'
        },
        {
            name: 'netAmount'
        },
        {
            name: 'grossAmount'
        },
        {
        	name: 'tax'
        },
        {
        	name: 'status'
        },
        {
        	name: 'size'
        }
        
    ]
});