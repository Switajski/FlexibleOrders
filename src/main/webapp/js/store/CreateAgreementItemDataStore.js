Ext.define('MyApp.store.CreateAgreementItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreateAgreementItemDataStore',
	customurl: '/FlexibleOrders/reportitems/listAllToBeProcessed',
    groupField: 'documentNumber'
});