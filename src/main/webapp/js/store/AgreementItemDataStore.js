Ext.define('MyApp.store.AgreementItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.AgreementItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
	customstoreid: 'AgreementItemDataStore',
	groupField: 'documentNumber'
});