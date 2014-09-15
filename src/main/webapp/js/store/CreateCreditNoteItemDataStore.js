Ext.define('MyApp.store.CreateInvoiceItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreateInvoiceItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
    groupField: 'documentNumber'
});