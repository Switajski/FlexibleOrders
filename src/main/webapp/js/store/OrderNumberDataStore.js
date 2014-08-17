Ext.define('MyApp.store.OrderNumberDataStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            //autoLoad: false,
            //model: 'MyApp.model.OrderNumberData',
        	fields: ['orderNumber'],
            storeId: 'OrderNumberStore',
            pageSize: 1000,
            proxy: {
            	actionMethods: {
                    read: 'GET',
                    update: 'PUT',
                    destroy: 'DELETE',
                    create: 'POST'
                },
                type: 'ajax',
                url: '/FlexibleOrders/reports/orders/listOrderNumbers',
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