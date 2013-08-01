
Ext.define('MyApp.store.BestellpositionDataStore', {
    extend: 'Ext.data.Store',
	customurl: '/FlexibleOrders/orderitems/json/',
	custommodel: 'MyApp.model.BestellpositionData',
	customstoreid: 'BestellpositionDataStore',
    requires: [
        'MyApp.model.BestellpositionData'
    ],

    reload: function(activeBestellnr){
    	this.load({
            params: {
                orderNumber: activeBestellnr
            },
            url: this.customurl
        });
    },
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            model: this.custommodel,
            storeId: this.customstoreid,
            autoLoad: true,
            pageSize:20,
            remoteFilter: true,
            proxy: {
                type: 'ajax',
                actionMethods: {
                    read: 'GET',
                    update: 'PUT',
                    destroy: 'DELETE',
                    create: 'POST'
                },
                api:{
                    read: this.customurl,
                    update: this.customurl,
                    destroy: this.customurl,
                    create: this.customurl
                },
                headers: {
                    Accept: 'application/json'
                },
                reader: {
                    type: 'json',
                    successProperty: 'success',
                    root: 'data',
                    messageProperty: 'message'
                },
//                autoSync: true,
                afterRequest:function(request,success){
                    console.log(request);	                    
                },
                listeners: {
                    exception: function(proxy, response, operation){
                        Ext.MessageBox.show({
                            title: 'Server Fehler',
                            msg: operation.getError(),
                            icon: Ext.MessageBox.ERROR,
                            buttons: Ext.Msg.OK
                        });
                    }
                }
                /*,
                filters: {
                    property: 'status',
                    value: {
                        Bestellung: 'NICHTBESTAETIGT'
                    }
                }  */
                
            }
        }, cfg)]);
    }
});