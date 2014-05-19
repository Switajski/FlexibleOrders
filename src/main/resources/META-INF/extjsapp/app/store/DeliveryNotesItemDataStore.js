Ext.define('MyApp.store.DeliveryNotesItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.DeliveryNotesItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
	customstoreid: 'DeliveryNotesItemDataStore',
	groupField: 'documentNumber',
});