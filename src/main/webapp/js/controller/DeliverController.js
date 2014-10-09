Ext.define('MyApp.controller.DeliverController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'DeliverController',
	models : ['ItemData'],
	stores : ['CreateDeliveryNotesItemDataStore'],
	views : ['ConfirmWindow'],

	init : function(application) {
		this.control({
				});
	},
	
	deliver : function(event, record) {
		deliveryNotesNumber = record.data.documentNumber.replace(/AB/g, "L");

		record.data.deliveryNotesNumber = record.data.documentNumber;
		var createDeliveryNotesStore = MyApp.getApplication()
				.getStore('CreateDeliveryNotesItemDataStore');
		createDeliveryNotesStore.filter([{
					property : "customerNumber",
					value : record.data.customerNumber
				}, {
					property : "status",
					value : "agreed"
				}, {
					property : "asdf",
					value : "asdf"
				}]);

		var deliverWindow = Ext.create('MyApp.view.DeliverWindow', {
					id : "DeliverWindow",
					onSave : function() {
						MyApp
								.getApplication()
								.getController('DeliverController')
								.deliver2("ok", kunde, createDeliveryNotesStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("customerNumber",
				record.data.customerNumber);
		kundeId = kunde.data.id;
		email = kunde.data.email;

		deliverWindow.show();
		deliverWindow.down('form').getForm().setValues({
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
					street : kunde.data.street
				});
		// somehow the id is deleted onShow
		Ext.getCmp('deliveryNotesNumber').setValue(deliveryNotesNumber);
		Ext.getStore('KundeDataStore').findRecord("email", email).data.id = kundeId;
	},

	deliver2 : function(event, record, createDeliveryNotesStore) {
		var form = Ext.getCmp('DeliverWindow').down('form').getForm();
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/deliver',
				// headers: { 'Content-Type': 'application/json' },
				jsonData : {
					orderConfirmationNumber : form.getValues().confirmationNumber,
					customerId : form.getValues().id,
					name1 : form.getValues().name1,
					name2 : form.getValues().name2,
					street : form.getValues().street,
					postalCode : form.getValues().postalCode,
					city : form.getValues().city,
					country : form.getValues().country,
					deliveryNotesNumber : form.getValues().deliveryNotesNumber,
					shipment : form.getValues().shipment,
					packageNumber : form.getValues().packageNumber,
					trackNumber : form.getValues().trackNumber,
					created : form.getValues().created,
					items : Ext.pluck(createDeliveryNotesStore.data.items,
							'data')
				},
				success : function(response) {
					var text = response.responseText;
					// Sync
					MyApp.getApplication().getController('MyController')
							.sleep(500);
					var allGrids = Ext.ComponentQuery.query('PositionGrid');
					allGrids.forEach(function(grid) {
								grid.getStore().load();
							});
					Ext.getCmp("DeliverWindow").close();
				}
			});
		}
	}
	
	
});