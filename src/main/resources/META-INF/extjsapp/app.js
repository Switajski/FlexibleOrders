
//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
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
    autoCreateViewport: true,
    controllers: [
        'MyController'
    ],
    name: 'MyApp'
});


