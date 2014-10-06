Ext.define('MyApp.controller.ConfirmController', { 
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'ConfirmController',
	models : ['ItemData'],
	stores : ['CreateConfirmationReportItemDataStore'],
	views : ['ConfirmWindow'],

	init : function(application) {
		this.control({
					//'#CreateCustomerButton' : {
					//	click : this.onCreateCustomer
					//}
				});
	},
	
	onConfirm : function(record) {
		confirmationReportNumber = record.data.orderNumber.replace(/B/g, "AB");

		record.data.confirmationReportNumber = record.data.documentNumber;
		var createConfirmationReportStore = MyApp.getApplication()
				.getStore('CreateConfirmationReportItemDataStore');
		//FIXME: Got problems with creating a store dynamically. This Snippet only works the second time the window is opened
		//var createConfirmationReportStore = Ext.create('MyApp.store.CreateConfirmationReportItemDataStore', {
		//	id: "CreateConfirmationReportItemDataStore",
		//	listeners : {
		//        load : function(store, recs) {
		//            console.log(recs.length, store.getCount()); //both are 2
		//        }
		//	}
		//});
		//
		// See solution how DeliveryHistoryStore works
		createConfirmationReportStore.clearFilter(true);
		createConfirmationReportStore.filter([{property: 'customerNumber', value:record.data.customerNumber},
			{property: 'status', value:'ordered'}]);

		var confirmWindow = Ext.create('MyApp.view.ConfirmWindow', {
					id : "ConfirmWindow",
					onSave : function() {
						MyApp.getApplication().getController('ConfirmController')
								.confirm("ok", kunde,
										createConfirmationReportStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("customerNumber",
				record.data.customerNumber);
		email = kunde.data.email;

		confirmWindow.show();
		confirmWindow.down('form').getForm().setValues({
					email : kunde.data.email,
					firstName : kunde.data.firstName,
					id : kunde.data.id,
					lastName : kunde.data.lastName,
					phone : kunde.data.phone,
					customerNumber : kunde.data.customerNumber,

					name1 : kunde.data.name1,
					name2 : kunde.data.name2,
					street : kunde.data.street,
					postalCode : kunde.data.postalCode,
					city : kunde.data.city,
					country : kunde.data.country,
					
					dname1 : kunde.data.dname1,
					dname2 : kunde.data.dname2,
					dstreet : kunde.data.dstreet,
					dpostalCode :kunde.data.dpostalCode,
					dcity : kunde.data.dcity,
					dcountry : kunde.data.dcountry,

				    contact1 : kunde.data.contact1,
				    contact2 : kunde.data.contact2,
				    contact3 : kunde.data.contact3,
				    contact4 : kunde.data.contact4,
			
			        mark : kunde.data.mark,
			        paymentConditions : kunde.data.paymentConditions,
			        saleRepresentative : kunde.data.saleRepresentative,
			        vatIdNo : kunde.data.vatIdNo,
			        vendorNumber : kunde.data.vendorNumber
				});
		// somehow the id is deleted onShow
		// Ext.getCmp('confirmationReportNumber')
		// .setValue(confirmationReportNumber);
		// Ext.getStore('KundeDataStore').findRecord("email", email).data.id =
		// kundeId;
		Ext.getCmp('newOrderConfirmationNumber')
				.setValue(confirmationReportNumber);
	},

	confirm : function(event, record, createConfirmationReportStore) {
		var form = Ext.getCmp('ConfirmWindow').down('form').getForm();
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/confirm',
				jsonData : {
					orderNumber : form.getValues().orderNumber,
					orderConfirmationNumber : form.getValues().orderConfirmationNumber,
					customerId : form.getValues().id,
					expectedDelivery : form.getValues().expectedDelivery,
					
					name1 : form.getValues().name1,
					name2 : form.getValues().name2,
					street : form.getValues().street,
					postalCode : form.getValues().postalCode,
					city : form.getValues().city,
					country : form.getValues().country,
					
					dname1 : form.getValues().dname1,
					dname2 : form.getValues().dname2,
					dstreet : form.getValues().dstreet,
					dpostalCode :form.getValues().dpostalCode,
					dcity : form.getValues().dcity,
					dcountry : form.getValues().dcountry,
					
					contact1 : form.getValues().contact1,
				    contact2 : form.getValues().contact2,
				    contact3 : form.getValues().contact3,
				    contact4 : form.getValues().contact4,
			
			        mark : form.getValues().mark,
			        paymentConditions : form.getValues().paymentConditions,
			        saleRepresentative : form.getValues().saleRepresentative,
			        valueAddedTaxIdNo : form.getValues().vatIdNo,
			        vendorNumber : form.getValues().vendorNumber,
			        
			        deliveryMethodNo : form.getValues().deliverymethod,
					
					items : Ext.pluck(createConfirmationReportStore.data.items,
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
					Ext.getCmp("ConfirmWindow").close();
				}
			});
		}
	}

});
