
//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
	appFolder: '/FlexibleOrders/resources/app',
    models: [
        'BestellungData',
        'ItemData',
        'ArtikelData',
        'KundeData'
    ],
    stores: [
        'BestellungDataStore',
        'ItemDataStore',
        'ShippingItemDataStore',
        'InvoiceItemDataStore',
        'ArchiveItemDataStore',
        'ArtikelDataStore',
        'KundeDataStore'
    ],
    views: [
        'MainPanel',
        'TransitionWindow',
		'ConfirmWindow',
        'DeliverWindow',
        'PositionGridPanel',
        'AgreementItemGridPanel',
        'OrderItemGridPanel',
        'ShippingItemGridPanel',
        'InvoiceItemGridPanel',
        'ArchiveItemGridPanel',
        'CustomerComboBox'
    ],
    autoCreateViewport: false,
    controllers: [
        'MyController',
        'AgreementController',
        'CustomerController',
        'ConfirmController',
        'DeliverController',
        'InvoiceController',
        'OrderController'
    ],
    name: 'MyApp',
    //autoCreateViewport:true,
    launch: function() {
        Ext.create('MyApp.view.MainPanel', {
            layout: 'fit',
            renderTo: Ext.get('extjs_listitems')
        });
    }
});


