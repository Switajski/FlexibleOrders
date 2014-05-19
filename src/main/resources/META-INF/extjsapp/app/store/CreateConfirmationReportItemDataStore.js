Ext.define('MyApp.store.CreateConfirmationReportItemDataStore', {
    extend: 'MyApp.store.ItemDataStore',
    alias: 'widget.CreateConfirmationReportItemDataStore',
	customurl: '/FlexibleOrders/reportitems/ordered',
    groupField: 'orderNumber'
});