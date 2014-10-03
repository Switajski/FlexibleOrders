// @require @packageOverrides
Ext.Loader.setConfig({
			enabled : true
		});

Ext.application({
	appFolder : '/FlexibleOrders/resources',
	models : ['BestellungData', 'ItemData', 'ArtikelData', 'KundeData'],
	stores : ['ArtikelDataStore', 'ArchiveItemDataStore',
			'BestellungDataStore', 'ItemDataStore', 'InvoiceItemDataStore',
			'KundeDataStore', 'ShippingItemDataStore'],
	views : ['MainPanel', 'TransitionWindow', 'ConfirmWindow', 'IssueWindow',
			'DeliverWindow', 'PositionGridPanel', 'AgreementItemGridPanel',
			'OrderItemGridPanel', 'ShippingItemGridPanel',
			'InvoiceItemGridPanel', 'ArchiveItemGridPanel', 'CustomerComboBox'],
	autoCreateViewport : false,
	controllers : ['MyController', 'AgreementController',
			'CustomerController',
			'IssueController', // FIXME: removing that useless line of code
			// makes the extjs application not starting
			'ConfirmController', 'DeliverController', 'InvoiceController',
			'MarkPaidController', 'OrderController'],
	name : 'MyApp',
	// autoCreateViewport:true,
	launch : function() {
		Ext.create('MyApp.view.MainPanel', {
					layout : 'fit',
					renderTo : Ext.get('extjs_listitems')
				});
		MyApp.constants = {};

		Ext.apply(MyApp.constants, {
					FILTER_ON_CUSTOMER : 'customerNumber',
					FILTER_ON_STATUS : 'status',
					FILTER_STATUS_STATES : {
						ORDERED : 'ordered',
						CONFIRMED : 'confirmed',
						AGREED : 'agreed',
						SHIPPED : 'shipped',
						INVOICED : 'invoiced'
					}
				});
	}
});
