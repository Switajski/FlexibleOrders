
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
        'ArtikelDataStore',
        'ArchiveItemDataStore',
        'BestellungDataStore',
        'ItemDataStore',
        'InvoiceItemDataStore',
        'CreditNoteItemDataStore',
        'KundeDataStore',
        'ShippingItemDataStore'
    ],
    views: [
        'MainPanel',
        'TransitionWindow',
		'ConfirmWindow',
		'IssueWindow',
        'DeliverWindow',
        'PositionGridPanel',
        'AgreementItemGridPanel',
        'OrderItemGridPanel',
        'CreditNoteItemGridPanel',
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
        'IssueController',
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


