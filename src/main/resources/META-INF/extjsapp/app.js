
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
        'PositionGridPanel',
		'ConfirmWindow',
        'CompleteWindow',
        'DeliverWindow',
        'TransitionWindow'
    ],
    autoCreateViewport: true,
    controllers: [
        'MyController'
    ],
    name: 'MyApp'
});


