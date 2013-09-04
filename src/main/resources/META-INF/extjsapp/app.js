
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
        'InvoiceItemData',
        'ArchiveItemData',
        'ArtikelData',
        'KundeData'
    ],
    stores: [
        'BestellungDataStore',
        'BestellpositionDataStore',
        'ShippingItemDataStore',
        'InvoiceItemDataStore',
        'ArchiveItemDataStore',
        'ArtikelDataStore',
        'KundeDataStore'
    ],
    views: [
        'MainPanel',
        'BpForm',
        'ErstelleBestellungWindow',
        'BestellungWindow',
		'ConfirmWindow',
        'CompleteWindow',
        'DeliverWindow',
        'TransitionWindow',
        'PositionGridPanel',
        'OrderItemGridPanel',
        'ShippingItemGridPanel',
        'InvoiceItemGridPanel',
        'ArchiveItemGridPanel',
        'BestellungGridPanel',
        'CustomerComboBox'
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


