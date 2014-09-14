Ext.define('MyApp.controller.IssueController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'IssueController',
	models : ['ItemData'],
	stores : ['CreateCreditNoteItemDataStore'],
	views : ['IssueWindow'],

	init : function(application) {
		this.control({
				});
	},
	
	onIssue : function(event, record) {
		deliveryNotesNumber = "GS-" + record.data.documentNumber;

		record.data.deliveryNotesNumber = record.data.documentNumber;
		var createCreditNoteStore = MyApp.getApplication()
				.getStore('CreateDeliveryNotesItemDataStore');
		createCreditNoteStore.filter([{
					property : "customerNumber",
					value : record.data.customerNumber
				}, {
					property : "status",
					value : "confirmed"
				}]);

		var deliverWindow = Ext.create('MyApp.view.IssueWindow', {
					id : "IssueWindow",
					onSave : function() {
						MyApp
								.getApplication()
								.getController('IssueController')
								.issue("ok", kunde, createCreditNoteStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("id",
				record.data.customer);
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

	issue : function(event, record, createCreditNoteStore) {
		var form = Ext.getCmp('IssueWindow').down('form').getForm();
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/issue',
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
					items : Ext.pluck(createCreditNoteStore.data.items,
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
					Ext.getCmp("IssueWindow").close();
				}
			});
		}
	}
	
	
});