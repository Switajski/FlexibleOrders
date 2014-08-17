Ext.define('MyApp.store.ArchiveItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.ArchiveItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
	custommodel: 'MyApp.model.ItemData',
	customstoreid: 'ArchiveItemDataStore',
	groupField: 'documentNumber',
    requires: [
        'MyApp.model.ItemData'
    ]
});