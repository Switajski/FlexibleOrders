Ext.define('MyApp.controller.InvoiceController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'InvoiceController',
	models : ['ItemData'],
	stores : ['CreateInvoiceItemDataStore'],
	views : ['InvoiceWindow'],

	init : function(application) {
		this.control({
				});
	},
	
	invoice : function(event, record) {
		deliveryNotesNumber = record.data.deliveryNotesNumber
				.replace(/L/g, "R");

		record.data.deliveryNotesNumber = record.data.deliveryNotesNumber;
		var createInvoiceStore = MyApp.getApplication()
				.getStore('CreateInvoiceItemDataStore');
		createInvoiceStore.filter([{
					property : "customerNumber",
					value : record.data.customerNumber
				}, {
					property : "status",
					value : "shipped"
				}]);

		var invoiceWindow = Ext.create('MyApp.view.InvoiceWindow', {
					id : "InvoiceWindow",
					onSave : function() {
						MyApp.getApplication().getController('InvoiceController')
								.invoice2("ok", kunde, createInvoiceStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("customerNumber",
				record.data.customerNumber);
		kundeId = kunde.data.customerNumber;
		email = kunde.data.email;

		invoiceWindow.show();
		invoiceWindow.down('form').getForm().setValues({
			name1 : kunde.data.name1,
			name2 : kunde.data.name2,
			city : kunde.data.city,
			country : kunde.data.country,
			email : kunde.data.email,
			firstName : kunde.data.firstName,
			id : kunde.data.id,
			lastName : kunde.data.lastName,
			phone : kunde.data.phone,
			postalCode : kunde.data.postalCode,
			customerNumber : kunde.data.customerNumber,
			street : kunde.data.street,
			paymentConditions : "Rechnungsbetrag ist zahlbar innerhalb von 30 Tagen. 3% Skonto bei Zahlung innerhalb von 8 Tagen."
		});
		// somehow the id is deleted onShow
		Ext.getCmp('invoiceNumber').setValue(deliveryNotesNumber);
		Ext.getStore('KundeDataStore').findRecord("email", email).data.id = kundeId;
	},

	invoice2 : function(event, record, createInvoiceStore) {
		var form = Ext.getCmp('InvoiceWindow').down('form').getForm();
		if (event == "ok") {
			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/invoice',
						jsonData : {
							customerId : form.getValues().id,
							billing : form.getValues().billing,
							created : form.getValues().created,
							name1 : form.getValues().name1,
							name2 : form.getValues().name2,
							street : form.getValues().street,
							postalCode : form.getValues().postalCode,
							city : form.getValues().city,
							country : form.getValues().country,
							invoiceNumber : form.getValues().invoiceNumber,
							paymentConditions : form.getValues().paymentConditions,
							items : Ext.pluck(createInvoiceStore.data.items,
									'data')
						},
						success : function(response) {
							var text = response.responseText;
							// Sync
							MyApp.getApplication()
									.getController('MyController').sleep(500);
							var allGrids = Ext.ComponentQuery
									.query('PositionGrid');
							allGrids.forEach(function(grid) {
										grid.getStore().load();
									});
							Ext.getCmp("InvoiceWindow").close();
						}
					});
		}
	}
	
	
});