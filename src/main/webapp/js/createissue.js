
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
        'ItemDataStore',
        'InvoiceItemDataStore',
        'CreditNoteItemDataStore',
        'KundeDataStore'
    ],
    views: [
        'IssuePanel',
        'TransitionWindow',
		'IssueWindow',
        'PositionGridPanel',
        'CreditNoteItemGridPanel',
        'CustomerComboBox'
    ],
    autoCreateViewport: false,
    controllers: [
        'MyController',
        'CustomerController',
        'IssueController'
    ],
    name: 'MyApp',
    //autoCreateViewport:true,
    launch: function() {
        Ext.create('MyApp.view.IssuePanel', {
            layout: 'fit',
            renderTo: Ext.get('extjs_createissue')
        });
    }
});


