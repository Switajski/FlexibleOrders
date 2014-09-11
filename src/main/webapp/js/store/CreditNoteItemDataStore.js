Ext.define('MyApp.store.CreditNoteItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreditNoteItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
	customstoreid: 'CreditNoteItemDataStore',
	groupField: 'documentNumber'
});