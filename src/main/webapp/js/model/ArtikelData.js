Ext.define('MyApp.model.ArtikelData', {
    extend: 'Ext.data.Model',

    idProperty: 'productNumber',

    fields: [
        {
            name: 'productNumber'
        },
        {
            name: 'name'
        },
        {
            name: 'productType'
        },
        {
            name: 'description'
        },
        {
            name: 'category'
        },
        {
            name: 'active'
        },
        {
        	name: 'recommendedPriceNet'
        }
    ]
});