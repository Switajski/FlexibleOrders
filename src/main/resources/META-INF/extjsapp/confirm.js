
//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
	appFolder: '/FlexibleOrders/resources/app',
    models: [
        'BestellungData',
        'BestellpositionData',
        'ShippingItemData',
        'ArtikelData',
        'KundeData'
    ],
    stores: [
        'BestellungDataStore',
        'BestellpositionDataStore',
        'ShippingItemDataStore',
        'ArtikelDataStore',
        'KundeDataStore',
        'OrderNumberDataStore'
    ],
    views: [
        'MainPanel',
        'BpForm',
        'ErstelleBestellungWindow',
        'BestellungWindow',
		'ConfirmWindow',
        'DeliverWindow',
        'PositionGridPanel',
        'OrderItemGridPanel',
        'ShippingItemGridPanel',
        'BestellungGridPanel',
        'CustomerComboBox',
        'OrderNumberComboBox'
    ],
    autoCreateViewport: false,
    controllers: [
        'MyController'
    ],
    name: 'MyApp',
    //autoCreateViewport:true,
    launch: function() {
        Ext.create('MyApp.view.ConfirmPanel', {
            layout: 'fit',
            renderTo: Ext.get('extjs_confirm')
        });
    }
});


