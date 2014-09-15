Ext.define('MyApp.store.CreateCreditNoteItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreateCreditNoteItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
    groupField: 'documentNumber'
});