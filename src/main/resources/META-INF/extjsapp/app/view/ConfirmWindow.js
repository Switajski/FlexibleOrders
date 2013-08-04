Ext.define('MyApp.view.ConfirmWindow', {
	extend : 'MyApp.view.TransitionWindow',
	title : 'Bestellung zum Auftrag best&auml;tigen',
	itemid: 'ConfirmWindow',

	customfirstreport : 'Bestell',
	customsecondreport : 'Auftragsbestaetigungs',
	
	customfirststore : 'BestellpositionDataStore',
	customsecondstore : 'MyApp.store.ShippingItemDataStore',

	customfirstgrid:'OrderItemGrid',
	customsecondgrid:'ShippingItemGrid',
	
	customtransitionfunction:function(view, a, b, column, event, record, f){
		console.log('ConfirmWindow - customtransitionfunction');
		var secondStore = Ext.ComponentQuery.query('grid[itemid=secondGrid]')[0].getStore();
		if (secondStore.totalCount==0 || secondStore.totalCount==null)
			Ext.MessageBox.prompt('Auftragsbest&auml;tigungsnummer', 'Bitte gew&uuml;nschte Auftragsbest&auml;tigungsnr eingeben:',
    			Ext.Function.bind(
    			MyApp.getApplication().getController('MyController').confirm, 
    			null, [record], 2));
		else {
			var ocnr = secondStore.data.items[0].data.orderNumber;
			MyApp.getApplication().getController('MyController').confirm(record, ocnr);
		}
	}
});