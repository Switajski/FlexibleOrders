Ext.define('MyApp.controller.MarkPaidController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'MarkPaidController',
	models : ['ItemData'],

	init : function(application) {
		this.control({
				});
	},
	
	markPaid : function(event, anr, record) {
		record.data.accountNumber = record.data.invoiceNumber
				.replace(/R/g, "Q");
		if (event == "ok") {

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/markPaid',
						params : {
							id : record.data.id,
							productNumber : record.data.product,
							quantity : record.data.quantity,
							invoiceNumber : record.data.invoiceNumber,
							accountNumber : record.data.accountNumber
						},
						jsonData : {
							invoiceNumber : record.data.invoiceNumber
						},
						success : function(response) {
							var text = response.responseText;
						}
					});

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	}
	
});