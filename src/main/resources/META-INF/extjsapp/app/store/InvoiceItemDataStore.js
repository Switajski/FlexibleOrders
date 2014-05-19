Ext.define('MyApp.store.InvoiceItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.InvoiceItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
	customstoreid: 'InvoiceItemDataStore',
	groupField: 'documentNumber'
});