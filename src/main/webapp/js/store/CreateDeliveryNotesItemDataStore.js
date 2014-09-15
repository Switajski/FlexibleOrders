Ext.define('MyApp.store.CreateDeliveryNotesItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreateDeliveryNotesItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
    groupField: 'documentNumber'
});