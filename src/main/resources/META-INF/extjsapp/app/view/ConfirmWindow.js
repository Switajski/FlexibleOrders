Ext.define('MyApp.view.ConfirmWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Bestellung zum Auftrag best&auml;tigen',
	itemid: 'ConfirmWindow',

	customfirstreport : 'Bestell',
	customsecondreport : 'Auftragsbestaetigungs',
	
	customfirststore : 'MyApp.store.BestellpositionDataStore',
	customsecondstore : 'MyApp.store.ShippingItemDataStore',

	customfirstgrid:'OrderItemGrid',
	customsecondgrid:'ShippingItemGrid',
	
	customtransitionfunction:function(view, a, b, column, event, record, f){
		MyApp.getApplication().getController('MyController').confirm(record);
	}
});