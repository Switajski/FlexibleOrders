Ext.define('MyApp.store.CarrierDataStore', {
    extend: 'Ext.data.Store',
    customurl: '/FlexibleOrders/customers/json',
    requires: [
        'MyApp.model.CarrierData'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'MyApp.model.CarrierData',
            storeId: 'CarrierDataStore',
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
                    update: "/FlexibleOrders/carriers/udpate",
                    destroy: this.customurl,
                    create: "/FlexibleOrders/carriers/create"
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