Ext.define('MyApp.store.KundeDataStore', {
    extend: 'Ext.data.Store',
    customurl: '/FlexibleOrders/customers/json',
    requires: [
        'MyApp.model.KundeData'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'MyApp.model.KundeData',
            storeId: 'KundeDataStore',
            pageSize: 1000,
            proxy: {
                type: 'ajax',
                url: '/FlexibleOrders/customers/json',
                headers: {
                    accept: 'application/json'
                },
                reader: {
                    type: 'json',
                    root: 'data'
                },
                api:{
                    read: this.customurl,
                    update: "/FlexibleOrders/customers/udpate",
                    destroy: this.customurl,
                    create: "/FlexibleOrders/customers/create"
                },
                writer : {
                	type: 'json',
                	allowSingle: true,
                	root: 'data'
                }
            }
        }, cfg)]);
    }
});