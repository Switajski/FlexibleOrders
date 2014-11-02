Ext.define('MyApp.store.ArtikelDataStore', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.ArtikelData'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'MyApp.model.ArtikelData',
            storeId: 'ArtikelDataStore',
            pageSize: 100,
            proxy: {
                type: 'ajax',
                url: '/FlexibleOrders/products/listFromMagento',
                headers: {
                    accept: 'application/json'
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            }
        }, cfg)]);
    }
});