
Ext.define('MyApp.store.BestellpositionDataStore', {
    extend: 'Ext.data.Store',
	customurl: '/FlexibleOrders/reportitems/ordered',
	custommodel: 'MyApp.model.BestellpositionData',
	customstoreid: 'BestellpositionDataStore',
    requires: [
        'MyApp.model.BestellpositionData'
    ],
    alias: 'widget.BestellpositionDataStore',
	groupField: 'orderNumber',
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: this.custommodel,
            storeId: this.customstoreid,
            autoLoad: false,
            pageSize:5,
            remoteFilter: true,
            proxy: {
                type: 'ajax',
                actionMethods: {
                    read: 'GET',
                    update: 'PUT',
                    destroy: 'POST',
                    create: 'POST'
                },
                api:{
                    read: this.customurl,
                    update: this.customurl,
                    destroy: '/FlexibleOrders/transitions/deleteOrder',
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
                writer: {
                	allowSingle: false
                },
//                autoSync: true,
                afterRequest:function(request,success){
                    console.log(request);	                    
                }/*,
                listeners: {
                    exception: function(proxy, response, operation){
                    	console.error(response);
                        Ext.MessageBox.show({
                            title: 'Server Fehler',
                            msg: operation.getError(),
                            icon: Ext.MessageBox.ERROR,
                            buttons: Ext.Msg.OK
                        });
                    }
                }*/
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