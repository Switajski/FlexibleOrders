
//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
	appFolder: '/FlexibleOrders/resources',
    models: [
        'BestellungData',
        'ItemData',
        'ArtikelData',
        'KundeData'
    ],
    stores: [
        'BestellungDataStore',
        'itemDataStore',
        'ShippingItemDataStore',
        'InvoiceItemDataStore',
        'ArchiveItemDataStore',
        'ArtikelDataStore',
        'KundeDataStore',
        'OrderNumberDataStore'
    ],
    views: [
        'ArchiveItemGridPanel',
        'CustomerComboBox',
		'ConfirmWindow',
        'CreateCustomerWindow',
        'DeliverWindow',
        'InvoiceItemGridPanel',
        'InvoiceWindow',
        'MainPanel',
        'PositionGridPanel',
        'ShippingItemGridPanel',
        'OrderItemGridPanel',
        'OrderNumberComboBox',
        'OrderWindow'
    ],
    autoCreateViewport: false,
    controllers: [
        'MyController'
    ],
    name: 'MyApp',
    //autoCreateViewport:true,
    launch: function() {
        Ext.create('MyApp.view.MainPanel', {
            layout: 'fit',
            renderTo: Ext.get('_title_pl_de_switajski_priebes_flexibleorders_domain_OrderItem_id_pane')
        });
    }
});


