<<<<<<< HEAD:src/main/resources/META-INF/extjsapp/app/store/CarrierDataStore.js
Ext.define('MyApp.store.DeliveryMethodDataStore', {
    extend: 'Ext.data.Store',
    customurl: '/FlexibleOrders/customers/json',
    requires: [
        'MyApp.model.DeliveryMethodData'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            model: 'MyApp.model.DeliveryMethodData',
            storeId: 'DeliveryMethodDataStore',
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
                    update: "/FlexibleOrders/deliverymethods/udpate",
                    destroy: this.customurl,
                    create: "/FlexibleOrders/deliverymethods/create"
                },
                writer : {
                	type: 'json',
                	allowSingle: true,
                	root: 'data'
                }
            }
        }, cfg)]);
    }
=======
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
>>>>>>> master/master:src/main/webapp/js/store/CarrierDataStore.js
});